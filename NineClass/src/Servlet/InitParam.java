package Servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitParam implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("==========系统初始化==========");
        System.out.println(DB.Verify.CheckTables());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        DB.Verify.closeConnection();
    }
}
