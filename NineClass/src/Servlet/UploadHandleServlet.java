package Servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class UploadHandleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String uploadFilename = req.getParameter("fileName");
        String stu_id = (String)req.getSession().getAttribute("userId");//学号
        String homework_id = req.getParameter("detailIndex");

//        File file = new File(this.getServletContext().getRealPath("/WEB-INF/upload"));
//        if (!file.exists() && !file.isDirectory()) file.mkdir();
//        //得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
//        String savePath = this.getServletContext().getRealPath("/WEB-INF/upload/"+detailIndex);
//        file = new File(savePath);
//        //判断上传文件的保存目录是否存在
//        if (!file.exists() && !file.isDirectory()) {
//            System.out.println(savePath+"目录不存在，需要创建");
//            //创建目录
//            file.mkdir();
//        }
//        //消息提示
        String message = "";
        try{
            //使用Apache文件上传组件处理文件上传步骤：
            //1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //2、创建一个文件上传解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
            //解决上传文件名的中文乱码
            upload.setHeaderEncoding("UTF-8");
            upload.setFileSizeMax(1024*1024*100);
            upload.setSizeMax(1024*1024*1024);//设置同时上传的文件大小最大值
            //3、判断提交上来的数据是否是上传表单的数据
            if(!ServletFileUpload.isMultipartContent(req)){
                //按照传统方式获取数据
                return;
            }
            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = upload.parseRequest(req);
            for(FileItem item : list){
                //如果fileitem中封装的是普通输入项的数据
                if(!item.isFormField()){
                    //得到上传的文件名称，
                    String filename = item.getName();
                    System.out.println(filename);
                    if(filename==null || filename.trim().equals("")){
                        continue;
                    }
//                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
//                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
//                    filename = filename.substring(filename.lastIndexOf("\\")+1);
                    //获取item中的上传文件的输入流
                    String[] tail = filename.split("\\.");
                    InputStream in = item.getInputStream();
                    String homework_name;
                    if(tail==null || tail.length==0){
                        homework_name = uploadFilename;
                    }
                    else
                        homework_name = uploadFilename+"."+tail[tail.length-1];
                    DB.Verify.saveFile(stu_id,homework_id,homework_name,in);

                    //关闭输入流
                    in.close();
                    //删除处理文件上传时生成的临时文件
                    item.delete();
                    message = "true";
                }
            }
        }catch (Exception e) {
            message= "false";
            e.printStackTrace();
        }
        RequestDispatcher rd = req.getRequestDispatcher("uploadFile2.jsp");
        req.setAttribute("message",message);
        rd.forward(req,resp);
    }
}
