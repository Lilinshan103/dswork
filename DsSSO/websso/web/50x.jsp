<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%><%int code = response.getStatus();%><!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Error</title>
<style>body{width: 40em;margin: 0 auto;font-family: Tahoma, Verdana, Arial, sans-serif, 微软雅黑;}</style>
</head>
<body>
<h1>发生错误。</h1>
<%if(code >= 500){ %>
<p>很抱歉，您所要的页面目前无法使用。	<br/>请稍后再试。</p>
<p><a onclick="var o=document.getElementById('detail_error_msg');o.style.display=(o.style.display==''?'none':'');return false;" href="#">点击此处查看错误详情</a></p>
<p><div id="detail_error_msg" style="display:none;"><pre><%exception.printStackTrace(new java.io.PrintWriter(out));%></pre></div></p>
<%}else if(code == 404){%>
<p>很抱歉，您所要的页面不存在。</p>
<%}else{%>
<p>很抱歉，您所要的页面目前无权使用。<br/>请确定您有权访问该页面，或稍后再试。</p>
<%}%>

<p><em>system administrator.</em></p>
</body>
</html>
