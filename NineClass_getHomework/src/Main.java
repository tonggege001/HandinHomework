import java.io.*;
import java.sql.*;

public class Main {
    private static String driver =  "***";
    private static String url = "***";
    private static String user = "***";
    private static String pass = "***";
    private static Connection conn = null;

    static{
        try{
            Class.forName(driver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 将九班同学的个人信息导入到数据库
     */
    public static void AddAllStudent(){
        try{
            conn = DriverManager.getConnection(url,user,pass);
            PreparedStatement prepstmt = conn.prepareStatement("insert into student_info " +
                    "values(?,?)");
            BufferedReader br = new BufferedReader(new FileReader("src/res/IdName.txt"));
            while(true){
                String str = br.readLine(); //不读回车
                if(str==null||str.equals("")) break;
                String [] strArr = str.split(":");
                prepstmt.setString(1,strArr[0]);
                prepstmt.setString(2,strArr[1]);
                prepstmt.executeUpdate();
                System.out.println(strArr[0]+" "+strArr[1]);
            }
            br.close();
            prepstmt.close();
            conn.close();
            conn = null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取作业
     * @param homework_id 作业ID
     */
    public static void GetHomework(String homework_id){
        //建立文件
        File f = new File("C:\\Users\\Administor\\Desktop\\收作业");
        if(f.exists()){
            f.delete();
        }
        f.mkdirs();
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url,user,pass);
            PreparedStatement preStmt = conn.prepareStatement("select * from upload_info " +
                    "where homework_id=?");
            preStmt.setString(1,homework_id);//此处输入作业ID
            ResultSet rs = preStmt.executeQuery();
            while(rs.next()){
                String filename = rs.getString(3);
                Blob blob = rs.getBlob(4);
                InputStream is = blob.getBinaryStream();
                FileOutputStream fos = new FileOutputStream(new File(f.getAbsolutePath()+"/"+filename));
                byte [] b = is.readAllBytes();
                fos.write(b);
                is.close();
                fos.close();
                System.out.println(filename+" 已经下载成功");
            }
            rs.close();
            preStmt.close();
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*
    发布作业函数，传入作业和截止日期
     */
    public static void SendHomework(String name, String deadline){
        try{
            conn = DriverManager.getConnection(url,user,pass);
            PreparedStatement prepstmt = conn.prepareStatement("insert into homework_info(homework_name,deadline) " +
                    "values(?,?)");
            if(name!=null && deadline!=null && !name.equals("") && !deadline.equals("")){
                prepstmt.setString(1,name);
                prepstmt.setString(2,deadline);
                prepstmt.executeUpdate();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        //AddAllStudent();增加所有的人
        //SendHomework("算法设计实验打包", "11月25日晚八点");
        GetHomework("4");
    }
}
