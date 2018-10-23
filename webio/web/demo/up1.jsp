<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>文件上传</title>
<script type="text/javascript" src="/web/js/jquery/jquery.js"></script>
<script type="text/javascript" src="/web/js/dswork/dswork.js"></script>
<script type="text/javascript" src="/web/js/jskey/jskey_core.js"></script>
<script type="text/javascript" src="/web/js/jskey/jskey_upload.js"></script>
<script type="text/javascript">
var o = new $dswork.upload({io:true, name:"MYAPP", ext:"file"});
window.onload = function()
{
	o.init({id:"fjFile", vid:"fjFileNames", ext:"image", });
};
</script>
</head>
<body>
<center>
<form id="dateForm" action="up2.jsp" method="post">
<%--key--%>
<span><input id="fjFile" name="fjFile" type="hidden" value="" datatype="UploadFile" /></span>
<input id="fjFileNames" name="fjFileNames" type="hidden" value="" /><%--newname:oldname|newname:oldname--%>
<br /><br />
<input type="submit" class="button" id="_submit_" value="提交" />
</form>
</center>
</body>
</html>