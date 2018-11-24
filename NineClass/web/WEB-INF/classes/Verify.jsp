<%@ page language="java" import="java.util.*,java.io.*"%>
<%!
public static class Verify{
    private static String IdNamePath = Verify.class.getClassLoader().
            getResource("Resources/").getPath()+"IdName.txt";
    private  static String HomeWorkPath = Verify.class.getClassLoader().
            getResource("Resources/").getPath()+"HomeWork.txt";

    private static boolean CheckIdAndName(String id, String name){
        boolean isLogin = false;
        File fl;
        try{
            fl = new File(IdNamePath);
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

    private static List<Map<String,String>> getHomeWorkString(){
        List<Map<String,String>> list = new ArrayList<>();
        File fl;
        try{
            fl = new File(HomeWorkPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fl),"utf-8"));
            do{
                Map<String,String> map = new HashMap<>();
                /*
                字段homework, deadline
                 */
                String _work = br.readLine();
                if(_work==null) break;
                String[] work = _work.split(":");
                if(work.length!=2) continue;
                map.put("homework",work[0]);
                map.put("deadline",work[1]);
                list.add(map);
            }while (true);
            br.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return list;
    }

    private static boolean getUploadStatus(String detailIndex, String id,String name){
        String _filename = "CS1609_"+id+"_"+name;
        String filename = Verify.class.getClassLoader().
                getResource("WEB-INF/upload/").getPath()+detailIndex+"/"+_filename;
        File file = new File(filename);
        return file.exists();
    }
}
%>
