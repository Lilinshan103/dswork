//jquery.js和jskey_core.js支持
if(typeof($dswork)!="object"){$dswork={};}
$dswork.showDate = function(){
	var o = arguments[0];
	var f = arguments[1] || "yyyy-MM-dd";
	$jskey.calendar.show(o,{skin:'default', lang:0, format:f, sample:f});
};
$dswork.uploadURL = function(){
	return "/webio/io/upload." + ($dswork.dotnet ? "aspx" : "jsp");
};
$dswork.ioURL = function(){
	return "/webio/io/up." + ($dswork.dotnet ? "aspx" : "jsp");
};
$dswork.getChoose = function(m){m.url = "/web/js/jskey/themes/dialog/jskey_choose.html";return $jskey.dialog.show(m);};
$dswork.getChooseByKey = function(m){m.url = "/web/js/jskey/themes/dialog/jskey_choose_key.html";return $jskey.dialog.show(m);};
$dswork.getChooseDialog = function(m){return $jskey.dialog.showChoose(m);};
$dswork.getChooseDialogByKey = function(m){return $jskey.dialog.showChooseKey(m);};

$dswork.getWebRootPath = function(){
	var p = window.location.pathname;
	var i = p.indexOf("/");
	if(i != 0){p = p.substring(0, i);return "/" + p;}
	return (p.match(/\/\w+/))[0];
};
var ctx = $dswork.getWebRootPath();//站点虚拟根目录，没有则改为"/"
String.prototype.trim = function(){return this.replace(/(^\s*)|(\s*$)/g, "").replace(/(^　*)|(　*$)/g, "");};
String.prototype.replaceAll = function(t, u){
	var i = this.indexOf(t), r = "";
	if(i == -1){return this;}
	r += this.substring(0, i) + u;
	if(i + t.length < this.length){r += (this.substring(i + t.length, this.length)).replaceAll(t, u);}
	return r;
};
function attachUrl(){
	var url = arguments[0];
	var frameId = arguments[1] || "mainFrame";
	if(!url){url = "about:blank";}
	else if(url.indexOf("/") == 0){url = ctx + url;}
	document.getElementById(frameId).src = url;
}
//导航
$dswork.showNavigation = function(title){try{
	var url = window.location.href;
	top.frames['middleFrame'].navigation.addUrl(title, url);
	top.frames['middleFrame'].navigation.show();
}catch(ex){}};

//form
$dswork.callback = null;
$dswork.validCallBack = function(){return true;};
$dswork.result = {type:"", msg:"", data:""};
$dswork.checkResult = function(responseText){
	$dswork.result = {type:"", msg:"", data:""};
	try{
		var _msg = "", _arr = (responseText + "").split(":");
		if(_arr.length > 1){_msg = _arr[1];}
		else{switch(_arr[0]){
			case "0": _msg = "操作失败！";break;
			case "1": _msg = "操作成功！";break;
			default: _msg = (!isNaN(_arr[0]))?"":_arr[0];
		}}
		$dswork.result = {type:_arr[0], msg:_msg, data:(_arr.length>2?_arr[2]:"")};
	}
	catch(e){$dswork.result = {type:"", msg:"", data:""};}
	return $dswork.result.msg;
};
/**
 * 信息控件
 * var o=new MaskControl();//初始化
 * o.show("显示提示信息");
 * o.hide();//隐藏提示信息
 * o.autoDelayHide=function(html,time)//显示提示信息，并隔time毫秒后关闭
 */
function MaskControl(){
	this.callBack = null;
	var self = this;
	this.show = function(html){
		var m = $("#div_maskContainer"),w = $(window).width(),h = $(window).height();
		if(m.length == 0){
			var s = "<div id='div_maskContainer'><div id='div_maskMessage'></div><div id='div_maskBackground'></div>";
			if(document.all){s += '<iframe style="left:expression(this.previousSibling.offsetLeft);top:expression(this.previousSibling.offsetTop);width:expression(this.previousSibling.offsetWidth);height:expression(this.previousSibling.offsetHeight);position:absolute;z-index:2000;display:expression(this.previousSibling.style.display);" scrolling="no" frameborder="no"></iframe>';}
			s += "</div>";
			$("body").append(m=$(s));
		}
		var ww = $(document).width(), hh = $(document).height();
		if(ww < w){ww = w;}if(hh < h){hh = h;}
		$("#div_maskBackground").css("top", 0).css("left", 0).css("width", ww).css("height", hh);
		var tip = $("#div_maskMessage");
		tip.html(html||"");
		m.show();
		var x = (w - tip.width()) / 2, y = (h - tip.height()) / 2;
		tip.css("left", x).css("top", y + $(document).scrollTop());
	};
	this.hide = function(){var m = $("#div_maskContainer");if(m.length == 0){return;}m.remove();if(self.callBack != null){self.callBack();}this.callBack = null;};
	this.autoDelayHide = function(html, timeOut){var m = $("#div_maskContainer");if(m.length == 0){this.show(html);}else{$("#div_maskMessage").html(html);}if(timeOut == undefined){timeOut = 3000;}window.setTimeout(this.hide, timeOut);};
}
$dswork.doAjax = false;
$dswork.doAjaxObject = new MaskControl();
$dswork.showRequest = function(formData, jqForm, options){
	$dswork.doAjaxObject.show("正在处理，请稍候<img src='/web/js/dswork/loading.gif' />");return true;
};
$dswork.showResponse = function(data, status, xhr){
	$dswork.doAjaxObject.autoDelayHide($dswork.checkResult(data), 2000);
	$dswork.doAjaxObject.callBack = $dswork.callback;
};
$dswork.doAjaxShow = function(data, callback){
	$dswork.doAjaxObject.autoDelayHide($dswork.checkResult(data), 2000);
	$dswork.doAjaxObject.callBack = callback;
};
$dswork.doAjaxOption = {beforeSubmit:$dswork.showRequest,success:$dswork.showResponse};
_options = $dswork.doAjaxOption;
$dswork.readySubmit = function(){};
$dswork.beforeSubmit = function(){
	if(!$dswork.validCallBack()){return false;}
	return $jskey.validator.Validate("dataForm", $dswork.validValue || 3);
};
$dswork.showTree = function(p){if(typeof(p)!="object"){p={};}
	var ini = {id:"showTree"
		,title:"请选择"
		,url:function(node){return "";}
		,width:400,height:300,left:0,top:0
		,click:function(id, node){return true;}
		,dataFilter:function(id, pnode, data){return data;}
		,check:function(treeId, node){}
		,async:true,checkEnable:true,chkStyle:"radio"
		,tree:null,nodeArray:[]
	};
	p = $.extend(ini, p);
	var root = {id:"0", pid:"-1", isParent:true, name:p.title, nocheck:true};
	root = $.extend(root, p.root);
	if(p.nodeArray.length == 0){
		p.nodeArray.push(root);
	}
	p.divid = p.id + "_div";
	var $win = $('#'+p.divid);
	if($win.length){$win.css("display", "");}
	else{
		$win = $('<div id="'+p.divid+'" style="z-index:100000;position:absolute;background-color:#ffffff;border:0px;"></div>').appendTo('body');
	}
	if($('#jskey_temp_div_close').length > 0){$winsub.remove();}
	$winsub = $('<div id="jskey_temp_div_close" style="filter:alpha(opacity=1);opacity:0;z-index:99999;top:0px;left:0px;position:absolute;background-color:#ffffff;">&nbsp;</div>').appendTo('body');
	$winsub.css("width", $(document).width()).css("height", $(document).height());
	$winsub.bind("click", function(event){
		$win.css("display", "none");
		$winsub.remove();
		return true;
	});
	$win.css("width", p.width).css("height", p.height).css("left", p.left).css("top", p.top);
	var $tree = $('<ul id="'+p.id+'" class="ztree" style="border:2px solid #999999;overflow:auto;z-index:100000;background-color:#ffffff;"></ul>');
	$tree.css("width", p.width-10).css("height", p.height-10);
	$win.html($tree);
	var cfg = {view:{expandSpeed:""}
		,async:{enable:p.async,dataFilter:p.dataFilter,url:function(id, node){return p.url(node);}}
		,data:{key:{name:"name"},simpleData:{enable:true,idKey:"id",pIdKey:"pid"}}
		,check:{enable:p.checkEnable,chkStyle:p.chkStyle,chkboxType:{"Y":"ps","N":"s"},radioType:"all"}
	};
	cfg.callback = {
		"beforeCheck":p.check,
		"onCheck":function(event, treeId, node){$winsub.click();},
		"onClick":p.click
	};
	$win.css("display", "");
	p.tree = $.fn.zTree.init($tree, cfg, p.nodeArray);
	try{var _pn = p.tree.getNodeByParam("id", root.id);p.tree.selectNode(_pn);p.tree.reAsyncChildNodes(_pn, "refresh");}catch(e){}
};

//{sessionKey, fileKey, ext:file, limit:10240(KB), show:true}
$dswork.upload = function(o){
	if(typeof(o) != "object"){o = {};}
	this.sessionKey = o.sessionKey || 1;
	this.fileKey = parseInt(o.fileKey || 1);
	this.ext = o.ext || "file";
	this.count = 1;
	this.limit = o.limit || 10240;
	this.show  = o.show;
	if(this.show == null){this.show = true;}
	this.show == this.show ? true : false;
	this.image = "jpg,jpeg,gif,png";
	this.file =  "bmp,doc,docx,gif,jpeg,jpg,pdf,png,ppt,pptx,rar,rtf,txt,xls,xlsx,zip,7z";
	this.name = o.name || "";
	this.io = o.io;
	if(this.io == null){this.io = false;}
	this.io = this.io ? true : false;
	this.url = o.url || "";
};
$dswork.upload.prototype = {
	init:function(op){try{
	//{id:?,vid:*?,uploadone:,ext};ext :"file|image|***,***",uploadone:"true|false"
	if(typeof(op) != "object"){op = {};}
	this.count++;
	var defaults = {url:this.url,uploadone:"true",vid:"",sessionKey:this.sessionKey,fileKey:this.fileKey+this.count,show:this.show,limit:this.limit,ext:this.ext,name:this.name};
	var p = $.extend(defaults, op);
	p.sessionKey = parseInt(p.sessionKey);
	p.fileKey = parseInt(p.fileKey);
	p.limit = parseInt(p.limit);
	p.buttonid = p.buttonid || "";
	p.$bid = p.buttonid == "" ? p.id + "_span" : p.buttonid;
	p.$sid = p.id + "_showdiv";
	p.$input = $("#" + p.id);
	
	p.$vInput = null;
	if(p.vid != ""){p.$vInput = $("#" + p.vid);}
	if($jskey.upload.swf){
		if(p.ext == "image"){p.types = "*." + $jskey.$replace(this.image, ",", ";*.");}
		else if(p.ext == "file"){p.types = "*." + $jskey.$replace(this.file, ",", ";*.");}
		else{p.types = "*." + $jskey.$replace(p.ext, ",", ";*.");}
		if(p.buttonid == ""){
			p.$input.parent().append('<span id="' + p.$bid + '"></span>');
		}
		if(p.show){p.$input.parent().append('<div id="' + p.$sid + '" style="text-align:left;display:inline;"></div>');}
	}
	else{
		if(p.ext == "image"){p.types = this.image;}
		else if(p.ext == "file"){p.types = this.file;}
		else{p.types = p.ext;}
		if(p.buttonid == ""){
			p.$input.parent().append('<img id="' + p.$bid + '" style="cursor:pointer;width:61px;height:22px;border:none;vertical-align:middle;margin-top:-3px;" src="/web/js/jskey/themes/plupload/UploadButton.png"/>');
		}
		if(p.show){p.$input.parent().append('<div id="' + p.$sid + '" style="text-align:left;height:22px;display:inline;"></div>');}
	}
	if(p.url == ""){
		if(this.io){
			p.url = $dswork.ioURL() + "?name=" + this.name + "&ext=" + p.ext;
		}
		else{
			p.url = $dswork.uploadURL() + "?sessionkey=" + p.sessionKey + "&filekey=" + p.fileKey + "&ext=" + p.ext + "&uploadone=" + (p.uploadone=="true"?"true":"false");
		}
	}
	p.success = (typeof(p.success) == "function" ? p.success : function(p){});
	p.clear = (typeof(p.clear) == "function" ? p.clear : function(p){});
	var ps = {
		url:p.url,
		browse_button : p.$bid,
		unique_names : false,
		filters : {
			max_file_size : p.limit + "kb",
			mime_types: [{title : p.types, extensions : p.types}]
		},
		debug:false,
		settings:{div:p.show?p.$sid:"",success:function(data){// {arr:[{id,name,size,state,file,type,msg}],msg:"",err:""}
			if(typeof (data.err) == 'undefined'){alert("upload error");return false;}
			if(data.err != ''){alert(data.err);}
			var o,_has = p.$input.attr("has") || "";
			if(p.vid == ""){
				var ok = false;
				for(var i = 0;i < data.arr.length;i++){o = data.arr[i];if(o.state == "1"){ok = true;}}
				p.$input.val(ok ? p.fileKey : _has);
			}
			else{
				var v = p.$vInput.val();
				if(p.uploadone == "true"){
					v = "";
				}
				for(var i = 0;i < data.arr.length;i++){o = data.arr[i];if(o.state == "1"){v += (v != "" ? "|" : "") + o.file + ":" + o.name;}}
				p.$vInput.val(v);
				p.$input.val(v == "" ? _has : p.fileKey);
			}
			var v = p.$input.val();
			if(v != "" && v != 0){p.$input.attr("msg","");if(p.show){var s = $("#" + p.$sid), btn = $('<input class="button" type="button" value="取消上传" />');s.html('&nbsp;');s.append(btn);btn.bind("click", function(){p.$input.val(p.$input.attr("has") || "");if(p.vid!=""){p.$vInput.val("");}s.html("");p.$input.attr("msg","请上传文件");p.clear(p);});}}
			p.success(p);
		}}
	};
	// 兼容swfupload
	ps.custom_settings = ps.settings;
	ps.button_placeholder_id = ps.browse_button;
	ps.file_types = p.types;
	ps.file_size_limit = p.limit;
	
	return $jskey.upload.init(ps);
	}catch(e){alert("upload init error\n" + e.name + "\n" + e.message);return null;}}
};
$(function(){
	$("input").each(function(){
		var o = $(this);
		if(o.hasClass("WebDate") && o.attr("format")){
			o.bind("click", function(event){
				$dswork.showDate(this, o.attr("format"));
			});
			o.prop("readonly", true);
		}
	});
	$("select").each(function(){
		var o = $(this);
		var v = o.attr("v");
		if(v == null || typeof(v)=="undefined"){return true;}// false为跳出each循环，true为跳出当前循环
		try{o.val(v);}catch(e){}
		try{if(o.val() != v){o.prop("selectedIndex", 0);}}catch(e){}
	});
	$("input[type='radio']").each(function(){
		var o = $(this);
		var v = o.attr("v");
		var n = o.attr("name");
		if(v == null || typeof(v)=="undefined"){return true;}
		try{var _c = false, _e = $jskey.$byName(n);
			for(var i = 0;i < _e.length;i++){if(_e[i].value == v){_e[i].checked = true;_c = true;}}
			if(!_c){_e[0].checked = true;}
		}catch(e){}
	});
});