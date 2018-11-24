package Servlet;

import DB.Verify;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LoginInServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse respond) throws ServletException, IOException {
        System.out.println("LoginServlet, Session is"+request.getSession().toString());
        request.setCharacterEncoding("utf-8");
        String userId = request.getParameter("user_id");//学号
        String userName = request.getParameter("student_name");//姓名
        System.out.println(userId+"+"+userName);
        if(Verify.CheckIdAndName(userId,userName)){
            //处理登录成功的情况
            request.getSession().setAttribute("userId",userId);
            request.getSession().setAttribute("userName",userName);
            List<Map<String,String>> list = Verify.getHomeworkInfo(userId);
            System.out.println("登陆成功");
            request.setAttribute("homework_info",list);
            RequestDispatcher rd = request.getRequestDispatcher("homepage.jsp");
            rd.forward(request,respond);

        }
        //处理失败的情况
        else{
            System.out.println("登录失败");
            request.setAttribute("loginResult","false");
            RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
            rd.forward(request,respond);
        }

    }

}
