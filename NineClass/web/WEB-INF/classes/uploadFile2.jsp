<%@ page language="java" import="java.util.*"
         contentType="text/html; charset=utf-8"%>
<%request.setCharacterEncoding("utf-8");%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>UploadFile</title>
</head>
<body>
    <%
        String uploadResult = (String)request.getAttribute("message");
    %>
    <div onload="uploadTip(${pageScope.uploadRequest})"></div>

    <script language="JavaScript" type="text/javascript">
        function uploadTip(str) {
            if(str==="true"){
                alert("上传成功");
            }
            else
                alert("上传失败")
        }
    </script>
<%
    RequestDispatcher rd = request.getRequestDispatcher("homepage.jsp");
    List<Map<String,String>> list = DB.Verify.getHomeworkInfo((String)session.getAttribute("userId"));
    request.setAttribute("uploadResult",uploadResult);
    request.setAttribute("homework_info",list);
    rd.forward(request,response);
%>
</body>
</html>