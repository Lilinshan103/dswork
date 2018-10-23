<%@page language="java" pageEncoding="UTF-8" import="
java.net.URLEncoder,
dswork.web.MyRequest,
dswork.websso.model.DsWebssoUser,
dswork.websso.util.GsonUtil
"%><%
MyRequest req = new MyRequest(request);
DsWebssoUser po = new DsWebssoUser();
req.getFillObject(po);
String json = GsonUtil.toJson(po);
request.setAttribute("json", json);
%><html>
<body>
${json}
</body>
</html>