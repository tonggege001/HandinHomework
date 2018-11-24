<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%request.setCharacterEncoding("utf-8");%>
<%
  //loginResult: 登录结果
  String loginResult = request.getParameter("loginResult");
  String hintToUser = ""; //给用户的提示信息
  if (loginResult != null && loginResult.equals("false")) {
    hintToUser = "<p id=\"hint\">用户名或密码错误</p>";
  }
%>
<html>
  <head>
    <meta charset="UTF-8">
    <title>登录</title>
    <link rel="stylesheet" type="text/css" href="css/main.css">
    <link rel="stylesheet" type="text/css" href="css/login.css">
  </head>
  <body>
  <div id="header">
    <span id="siteName">CS1609班</span>
    <span id="jumpHerf"><a href="index.jsp">登录</a></span>
  </div>
  <div id="container">
    <h2 id="title">登录</h2>
    <div id="postForm">
      <div id="postFormContent" style="width: 169px;">
        <form action="LoginHandle" method="post">
          <label for="user_id">学号：</label><br/>
          <input type="text" name="user_id" id = "user_id"/><br/>
          <label for="student_name">姓名：</label><br/>
          <input type="text" name="student_name" id = "student_name"/><br/>
          <div class="Center" id="twoBtnStyle">
            <input type="submit" value="登录" name="commit" />
            <input type="button" value="修改密码" onclick="return false;" />
          </div>
          <!--此处显示提示信息 -->
          <%=hintToUser%>
        </form>
      </div>
    </div>
  </div>
  </body>
</html>
