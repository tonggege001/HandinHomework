package DB;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * 数据库认证类，负责数据库的增删改查
 */
public class Verify {
    private static String driver =  "***";
    private static String url = "***";
    private static String user = "***";
    private static String pass = "***";

    private static Connection conn = null;

/*
静态初始化块，加载MYSQL驱动
 */
    static{
        try{
            Class.forName(driver);
            conn = DriverManager.getConnection(url,user,pass);
        }catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
            conn = null;
        }
    }
    /*
    连接数据库，返回Connection句柄
     */
    public static Connection getConnection(){
        if(conn==null){
            try{
                conn = DriverManager.getConnection(url,user,pass);
            }catch (SQLException e){
                e.printStackTrace();
                conn = null;
            }
        }else{
            try{
                conn.close();
            }catch (Exception e){
                System.out.println("conn close failed");
            }
        }
        try{
            conn = DriverManager.getConnection(url,user,pass);
        }catch (SQLException e){
            e.printStackTrace();
            conn = null;
        }
        System.out.println("连接Connection"+conn.toString());
        return conn;
    }

    /**
     * 关闭数据库
     */
    public static void closeConnection(){
        if(conn==null)
            return;
        else{
            try{
                conn.close();
                conn=null;
            }catch (SQLException e){
                conn=null;
                e.printStackTrace();
            }
        }
    }
    //检查各个数据表是否完整
    public static boolean CheckTables(){
        boolean student_info = false;//学生信息表是否存在
        boolean homework_info = false;//发布的作业信息表是否存在
        boolean upload_info = false;//学生提交的作业数据表是否存在
        /**
         * upload_info包含如下列：
         * 学号、作业编号、作业内容
         * 其中（学号，作业编号）为主键
         */
        getConnection();
        try{
            DatabaseMetaData dbm = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = dbm.getTables(null, null, "%", types);
            while(rs.next()){
                String name = rs.getString("TABLE_NAME");
                if(name.equals("student_info")){
                    student_info = true;
                }
                if(name.equals("homework_info")){
                    homework_info = true;
                }
                if(name.equals("upload_info")){
                    upload_info = true;
                }
            }
            rs.close();
            Statement stmt = conn.createStatement();
            if(student_info==false){
                //创建表格
                stmt.executeUpdate("create table student_info " +
                        "(stu_id varchar(255) PRIMARY KEY ," +
                        "stu_name varchar(255) not null) default charset=utf8;");
            }
            if(homework_info==false){
                //创建表格
                stmt.executeUpdate("create table homework_info " +
                        "(homework_id int auto_increment PRIMARY KEY ," +
                        "homework_name varchar(255) unique not null, " +
                        "deadline varchar(255)) default charset=utf8;");
            }
            if(upload_info==false){
                //创建表格
                stmt.executeUpdate("create table upload_info " +
                        "(stu_id varchar(255) not null ," +
                        "homework_id int not null ," +
                        "homework_name varchar(255) not null ," +
                        "content mediumblob default null ," +
                        "constraint P_K primary key(stu_id,homework_id))default charset=utf8; ");
            }
            closeConnection();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //判断登录信息
    public static boolean CheckIdAndName(String id, String name){
        getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        boolean status = false;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from student_info where stu_id='"+id+"';");
            if(rs.next()){
                String stu_name = rs.getString("stu_name");
                status = stu_name.equals(name);
                System.out.println("User Name is"+stu_name);
            }
            stmt.close();
            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            closeConnection();
        }
        return status;
    }

    /*
    获取发布的作业消息字符串，返回给主界面进行调用
     */
    public static List<Map<String,String>> getHomeWorkString(){
        getConnection();
        List<Map<String,String>> list = new ArrayList<>();
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from homework_info ;" );
            while(rs.next()){
                Map<String, String> map = new HashMap<>();
                String homework_id = String.valueOf(rs.getInt("homework_id"));
                String homework_name = rs.getString("homework_name");
                String deadline = rs.getString("deadline");
                map.put("homework_id",homework_id);
                map.put("homework_name",homework_name);
                map.put("deadline",deadline);

                list.add(map);
            }
            stmt.close();
            rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            closeConnection();
        }
        return list;
    }
    public static boolean getStuHomeworkState(String stu_id, String homework_id){
        getConnection();
        Map<String, String> map = new HashMap<>();
        boolean stat = false;
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from upload_info where " +
                    "(stu_id = '"+stu_id+"')and (homework_id = '"+homework_id+"');" );
            if(rs.next()){
                stat = true;
            }
            rs.close();
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            closeConnection();
        }
        return stat;
    }

    /**
     * 保存文件
     * @param stu_id
     * @param homework_id
     * @param homework_name
     * @param is
     * @return
     */
    public static boolean saveFile(String stu_id, String homework_id, String homework_name, InputStream is){
        getConnection();
        try{
            //删除原来的记录
            PreparedStatement preStmt = conn.prepareStatement("delete from upload_info " +
                    "where stu_id=? and homework_id=?");
            preStmt.setString(1,stu_id);
            preStmt.setString(2,homework_id);
            preStmt.executeUpdate();
            preStmt.close();
            //插入新纪录
            preStmt = conn.prepareStatement("insert into upload_info " +
                    "values(?,?,?,?)");
            preStmt.setString(1,stu_id);
            preStmt.setString(2,homework_id);
            preStmt.setString(3,homework_name);
            preStmt.setBinaryStream(4,is);
            int affect = preStmt.executeUpdate();
            preStmt.close();
            if(affect!=1)
                return false;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            closeConnection();
        }
        return true;
    }
    /*
    获得作业的信息，包括人员的提交信息
     */
    public static  List<Map<String, String>> getHomeworkInfo(String UserID){
        //获取作业信息
        List<Map<String, String>> list = Verify.getHomeWorkString();
        //获取学生提交的作业信息
        for(Map<String, String> map:list){
            String homework_id = map.get("homework_id");
            if(Verify.getStuHomeworkState(UserID,homework_id))
                map.put("state","已上传");
            else
                map.put("state","未上传");
        }
        return list;
    }

    /*
    public static boolean CheckIdAndName(String id, String name){

        boolean isLogin = false;
        File fl;
        try{
            fl = new File("/name");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fl),"utf-8"));
            do{
                String _id_name = br.readLine();
                if(_id_name==null) break;
                String [] id_name = _id_name.split(":");
                if(id.equals(id_name[0]) && name.equals(id_name[1])){
                    isLogin = true;
                    break;
                }
            }while (true);
            br.close();
        } catch (IOException e){
            return false;
        }
        return isLogin;
    }
    */
}
