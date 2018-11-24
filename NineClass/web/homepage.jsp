<%@ page language="java" import="java.util.*"
         contentType="text/html; charset=utf-8"%>
<%@ page import="java.io.File" %>
<%request.setCharacterEncoding("utf-8");%>
<%
    String userId = (String)session.getAttribute("userId");
    String userName = (String)session.getAttribute("userName");
%>
<!--界面-->
<html>
<head>
    <title>个人主页</title>
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <link rel="stylesheet" type="text/css" href="css/homepage.css">
    <script src="./js/jquery.min.js"></script>
    <script src="./js/jquery.form.js"></script>
    <meta charset="UTF-8">
</head>
<body>
<div id="header">
    <span id="siteName">CS1609班</span>
    <!--显示姓名提示-->
    <span id="jumpHerf"><%=userName%></span>
</div>
<div id="containerBig">
    <p>你好，${sessionScope.userName}</p>
    <h2 id="title">CS1609作业提交系统</h2>
    <div id="centerTable">
        <table class="bordered">
            <thead>
            <tr>
                <th>作业ID</th>
                <th>作业描述</th>
                <th>DeadLine</th>
                <th>详情</th>
            </tr>
            </thead>
            <%
                //输出提交信息
                List<Map<String,String>> list = (List<Map<String,String>>)request.getAttribute("homework_info");
                if(list==null) list = new ArrayList<>();
                for(Map<String,String> map:list){
                    String status = map.get("state");
                    Integer homework_id = Integer.parseInt(map.get("homework_id"));
                    String detail = String.format("<button onclick=\"onClickChangeShow(this, %d)\">详情</button>", homework_id);
                    out.println(String.format("<tr><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>", map.get("homework_id"),map.get("homework_name"), map.get("deadline"), detail));
                    out.println(String.format(
                            "<tr id=\"showOrHidden%d\" style=\"display: none\"><td colspan=\"4\">作业描述:<br/><br/>"
                                    + "<form action=\"UploadHandle?detailIndex=%d&fileName=%s\" method=\"post\" enctype=\"multipart/form-data\">"
                                    //+"<label for=\"filename\">输入文件名</label><input type=\"text\" name=\"filename\" id=\"filename\">"
                                    +"<label>上传状态：</label><label>%s</label><br/>"
                                    + "<input type=\"file\" name=\"file\"/>"
                                    + "<input type=\"submit\" value=\"Submit\" name=\"commit\"/>"
                                    + "</form>"
                                    +"</td></tr>",
                            //+ "<p>附件下载：<a href=\"%s\" target=\"_blank\" download >附件</a></p></td></tr>",
                            homework_id, homework_id,
                            "CS1609" + "_" + session.getAttribute("userId") + "_" + session.getAttribute("userName"),status));
                }
            %>
        </table>

        <!--文件下载-->

        <label for="homeworkid">作业下载</label>
        <select id="homeworkid" name = "homeworkid" form="downloadForm">
            <%     for(int i =0;i<list.size();i++){
                Map<String,String> map = list.get(i);
                out.println(String.format("<option  value=\"%s\"> %s </option>",map.get("homework_id"),map.get("homework_name")));
            }
            %>

        </select>
        <form action="Download?ID=${sessionScope.userId}" method="post" id="downloadForm">
            <input type="submit" value="下载">
        </form>

    </div>

</div>
<%
    String detailIndex = request.getParameter("detailIndex");
%>
<script type="text/javascript">
    function onClickChangeShow(e, index) {
        var temp = document.getElementById("showOrHidden" + index);
        if (temp.style.display === "none") {
            temp.style.display = "";
        } else {
            temp.style.display = "none";
        }
    }
    document.getElementById("showOrHidden<%=detailIndex%>").style.display = "";
</script>

<script language="javascript" type="text/javascript">
    <%--function jumpDownload() {--%>
        <%--var adminId = '<%=session.getAttribute("userId")%>;';--%>
        <%--var select = document.getElementById("download_select");--%>
        <%--var homeworkId = select.options[select.selectedIndex].value;--%>
        <%--alert(homeworkId);--%>
        <%--// // alert("然而你不是管理员");--%>
        <%--// // if(adminId!=="U201614767"){--%>
        <%--// //--%>
        <%--// //     return;--%>
        <%--// // }--%>
        <%--// $.ajax({--%>
        <%--//     url:"/Download",//要请求的服务器url--%>
        <%--//     data:{ID:adminId, homeworkid:homeworkId},  //要向服务器发送的数据--%>
        <%--//     async:true,   //是否为异步请求--%>
        <%--//     cache:true,  //是否缓存结果--%>
        <%--//     type:"POST", //请求方式为POST--%>
        <%--//     //dataType:"content-disposition",   //服务器返回的数据是什么类型--%>
        <%--//     success:function(result){  //这个方法会在服务器执行成功是被调用 ，参数result就是服务器返回的值(现在是text类型)--%>
        <%--//         if(result){--%>
        <%--//             alert(result);--%>
        <%--//         }--%>
        <%--//     }--%>
        <%--// });--%>
    <%--}--%>
</script>
</body>
</html>
