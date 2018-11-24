package Servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.*;

public class DownloadHandleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String userID = req.getParameter("ID");
        String homeworkid = req.getParameter("homeworkid");

        Connection conn = DB.Verify.getConnection();
        try{
            PreparedStatement preStmt = conn.prepareStatement("select * from upload_info " +
                    "where homework_id=? and stu_id=?");
            preStmt.setString(1,homeworkid);//此处输入作业ID
            preStmt.setString(2,userID);//此处输入作业ID
            ResultSet rs = preStmt.executeQuery();
            while(rs.next()){
                String filename = rs.getString(3);
                Blob blob = rs.getBlob(4);
                InputStream is = blob.getBinaryStream();
//设置响应头，控制浏览器下载该文件
                resp.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
                //创建输出流
                OutputStream out = resp.getOutputStream();
                //输出缓冲区的内容到浏览器，实现文件下载
                int b;
                while((b=is.read())!= -1)
                {
                    out.write(b);
                }
                //关闭输出流
                out.close();
                is.close();
                System.out.println(filename+" 已经下载成功");

            }
            conn.close();
            conn=null;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
