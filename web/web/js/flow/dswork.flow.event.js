
;!function(){
var $f = $dswork.flow;
$f.on=function(dom,e,fn){dom.attachEvent ? dom.attachEvent('on'+e,function(){fn.call(dom,window.event);}):dom.addEventListener(e,fn,false);return $dswork;};



// o 是当前激活的dom，otime激活o的时间，state是EDIT或LINE
$f.p = {flow:null, o:null, otime:null, state:"0", EDIT:"0", LINE:"2", dx:0, dy:0, isDown:false, isMove:false};
//$f.c = ["#FFFFFF", "", "", "", "", "", "", ""];
$f.line = new $f.MyLine();
$f.line.$color = $f.line.color = "00FF00";
$f.line.toDom = function(){$f.line.dom = document.createElementNS("http://www.w3.org/2000/svg", "g");$f.line.flow.dom.appendChild($f.line.dom);$f.line.redraw();};
$f.set = function(x){
	if(x == null || $f.p.o != x){
		$f.setNone();
		$("#btn_delete").prop("disabled", true).val("不可操作");
		if($f.p.o != null){$f.p.o.unselected();}
		if(x != null){
			x.selected();
		}else{
			if($f.p.state == $dswork.flow.p.EDIT){
				$f.setTask("", "", 1, "", "", "");
				$("#btn_save").prop("disabled", false).val("增加任务");
			}
		}
		$f.p.o = x;
	}
};
$f.setNone = function(){
	$("#txt_alias").val("").prop("disabled", true);
	$("#txt_name").val("").prop("disabled", true);
	$("#txt_count").val("1").prop("disabled", true);
	$("#txt_users").val("").prop("disabled", true);
	$("#txt_subcount").val("").prop("disabled", true);
	$("#txt_subusers").val("").prop("disabled", true);
	$("#txt_forks").val("").prop("disabled", true);
	$("#btn_save").prop("disabled", true).val("不可操作");
};
$f.setTask = function(alias, name, count, users, subcount, subusers){
	$("#txt_alias").val(alias).prop("disabled", false);
	$("#txt_name").val(name).prop("disabled", false);
	$("#txt_count").val(count).prop("disabled", false);
	$("#txt_users").val(users).prop("disabled", false);
	$("#txt_subcount").val(subcount<0?"":subcount).prop("disabled", false);
	$("#txt_subusers").val(subusers).prop("disabled", false);
	if(alias == "start"){
		$("#txt_alias").prop("disabled", true);
		$("#txt_count").val("1").prop("disabled", true);
		$("#txt_subcount").val("").prop("disabled", true);
		$("#txt_subusers").val("").prop("disabled", true);
	}else if(alias == "end"){
		$("#txt_alias").prop("disabled", true);
		$("#txt_count").val("1").prop("disabled", true);
		$("#txt_users").val("").prop("disabled", true);
		$("#txt_subcount").val("").prop("disabled", true);
		$("#txt_subusers").val("").prop("disabled", true);
	}
};
$f.setLine = function(forks){
	$("#txt_forks").val(forks).prop("disabled", false);
};
$f.getTask = function(){
	var o = {
		"alias":$("#txt_alias").val(),
		"name":$("#txt_name").val(),
		"count":$("#txt_count").val(),
		"users":$("#txt_users").val(),
		"subcount":$("#txt_subcount").val(),
		"subusers":$("#txt_subusers").val()
	};
	try{o.count = parseInt(o.count);if(isNaN(o.count) || o.count < 1){o.count = 1;}}catch(ee){o.count = 1;}
	try{o.subcount = parseInt(o.subcount);if(isNaN(o.subcount) || o.subcount < 0){o.subcount = -1;}}catch(ee){o.subcount = -1;}
	return o;
};
$f.getLine = function(){
	var o = {
		"forks":$("#txt_forks").val()
	};
	return o;
};


$f.initEventPoint = function(dom){
	$f.on(dom,"dblclick",function(){var e = this;
		if($f.p.state == $f.p.EDIT){
			e.obj.remove();
		}
	return false;});
	$f.on(dom, "mousedown", function(event){var e = this;
		if($f.p.state == $f.p.EDIT){
			$f.set(e.obj);
			//if(e.obj.line != null){e.obj.line.selected();}
			$f.p.dx = event.clientX-$f.p.o.x;
			$f.p.dy = event.clientY-$f.p.o.y;
			$f.p.isDown = true;
			$("#btn_delete").prop("disabled", false).val("删除拐点");
		}
	return false;});
};
$f.initEventLine = function(dom){
	$f.on(dom, "mouseup", function(){var e = this;
		if($f.p.state == $f.p.EDIT){
			if($f.p.o == e.obj && (new Date()).getTime() - $f.p.otime < 300){// 证明是真实的双击
				var x = event.clientX-e.obj.flow.left, y = event.clientY-e.obj.flow.top;
				var arr = e.obj.linePoints();
				// 计算两两连线的长度
				var a = Math.sqrt(Math.pow(arr[0].x-x, 2)+Math.pow(arr[0].y-y, 2));
				var b = Math.sqrt(Math.pow(arr[1].x-x, 2)+Math.pow(arr[1].y-y, 2));
				var c = Math.sqrt(Math.pow(arr[1].x-arr[0].x, 2)+Math.pow(arr[1].y-arr[0].y, 2));
				// 以下利用椭圆原理，计算短轴半径最短的椭圆来判断距离哪条线最近
				var r1 = Math.pow((a+b)/2, 2) - Math.pow(c/2, 2);// 如果开根号则为椭圆短轴半径
				var index = 1;// 等于几就相当于这个点增加在哪个位置前面
				for(var i=2; i<arr.length; i++){
					a = Math.sqrt(Math.pow(arr[i-1].x-x, 2)+Math.pow(arr[i-1].y-y, 2));
					b = Math.sqrt(Math.pow(arr[i].x-x, 2)+Math.pow(arr[i].y-y, 2));
					c = Math.sqrt(Math.pow(arr[i].x-arr[i-1].x, 2)+Math.pow(arr[i].y-arr[i-1].y, 2));
					var r2 = Math.pow((a+b)/2, 2) - Math.pow(c/2, 2);
					if(r1 > r2){r1 = r2;index = i;}
				}
				var z = new $f.MyPoint(x, y);z.line = e.obj;z.flow = e.obj.flow;z.toDom();
				if(e.obj.from != null){e.obj.points.splice(index-1, 0, z);}else{e.obj.points.splice(index, 0, z);}
				e.obj.redraw();
				$f.set(e.obj);$("#btn_delete").prop("disabled", false).val("删除绘线");//$f.set(z);$("#btn_delete").prop("disabled", false).val("删除拐点");
			}else{
				$f.set(e.obj);$("#btn_delete").prop("disabled", false).val("删除绘线");
				$f.setNone();
				$f.setLine(e.obj.forks);
				$f.p.otime = (new Date()).getTime();// 记录这次点的时间
			}
		}
	return false;});
};
$f.initEventNode = function(dom){
	$f.on(dom, "mousedown", function(event){var e = this;$f.p.isDown = true;$f.p.isMove = false;
		if($f.p.state == $f.p.EDIT){
			$f.set(e.obj);$f.p.dx = event.clientX-$f.p.o.x;$f.p.dy = event.clientY-$f.p.o.y;
		}else if($f.p.state == $f.p.LINE){
			if(e.obj.alias != "end"){$f.set(e.obj);$f.line.from = e.obj;e.obj.selected();}
		}
	return false;});
	$f.on(dom, "mouseup", function(){var e = this;
		if($f.p.isDown && $f.p.state == $f.p.EDIT){
			$f.setTask(e.obj.alias, e.obj.name, e.obj.count, e.obj.users, e.obj.subcount, e.obj.subusers);
			if(e.obj.alias == "start"){
			}else if(e.obj.alias == "end"){
			}else{
				$("#btn_delete").prop("disabled", false).val("删除任务");
			}
			$("#btn_save").prop("disabled", true).val("不可操作");
		}
	return false;});
	$f.on(dom, "mousemove", function(event){var e = this;
		if($f.p.state == $f.p.LINE && $f.p.isMove && $f.line.from != null){
			if(e.obj != $f.line.from){
				if($f.line.to != null){if($f.line.to != e.obj){$f.line.to.unselected();}}
				$f.line.points = [];$f.line.to = e.obj;$f.line.to.selected();
			}else{// 在开始节点里
				if($f.line.to != null){$f.line.to.unselected();$f.line.to = null;}
				$f.line.points = [new $f.MyPoint(event.clientX-$f.p.flow.left, event.clientY-$f.p.flow.top)];
			}
		}
	return false;});
};
$f.initEventFlow = function(dom){
	$f.on(document.body, "mousemove", function(event){
		if($f.p.isDown){$f.p.isMove = true;
			if($f.p.state == $f.p.EDIT){
				if($f.p.o instanceof $dswork.flow.MyNode || $f.p.o instanceof $dswork.flow.MyPoint){
					$f.p.o.x = event.clientX-$f.p.dx;
					$f.p.o.y = event.clientY-$f.p.dy;
					if($f.p.o.x < 0){$f.p.o.x = 0;}
					if($f.p.o.y < 0){$f.p.o.y = 0;}
					$f.p.o.redraw();
					if($f.p.o instanceof $dswork.flow.MyPoint){if($f.p.o.line != null){$f.p.o.line.redraw();}}
				}
			}else if($f.p.state == $f.p.LINE){
				var scrollTop = window.pageYOffset||document.documentElement.scrollTop||document.body.scrollTop||0;
				var scrollLeft = window.pageXOffset||document.documentElement.scrollLeft||document.body.scrollLeft||0;
				var now = new $f.MyPoint(event.clientX-$f.p.flow.left+scrollLeft, event.clientY-$f.p.flow.top+scrollTop);
				if(now.x<0){now.x=0;}if(now.y<0){now.y=0;}
				var to = $f.line.to;
				if(to != null){// 判断是否还在to里面，比如刚刚从to里面出来
					if(now.x >= to.x && now.y >= to.y && now.x-to.x<=to.width && now.y-to.y<=to.height){$f.line.points = [];}
					else{$f.line.points = [now];$f.line.to.unselected();$f.line.to = null;}
				}else{$f.line.points = [now];}
				if($f.line.dom == null){$f.line.toDom();}
				$f.line.redraw();
			}
		}
	return false;});
	$f.on(document.body, "mouseup", function(event){
		if($f.p.isDown){
			if($f.p.state == $f.p.LINE){
				if($f.line.from != null && $f.line.to != null){
					var n = new $dswork.flow.MyLine();
					n.flow = $f.p.flow;
					n.from = $f.line.from;
					n.to = $f.line.to;
					var isFind = false;
					// 在toDom之前，如果两个节点已经存在指向，则不需要再添加
					for(var i=0; i<$f.p.flow.lines.length; i++){var m = $f.p.flow.lines[i];if(m.from == n.from && m.to == n.to){isFind = true;break;}}
					if(!isFind){n.toDom();n.flow.lines.push(n);}
				}
				$f.set(null);
				if($f.line.dom != null){$f.line.dom.remove();$f.line.dom = null;}
				if($f.line.from != null){$f.line.from.unselected();$f.line.from = null;}
				if($f.line.to != null){$f.line.to.unselected();$f.line.to = null;}
				$f.line.points = [];// 清空所有点
			}
		}else{if($f.p.state == $f.p.EDIT){var dom = event.target||event.srcElement;if(dom.obj instanceof $dswork.flow.MyFlow){$f.set(null);}}}
		$f.p.isDown = false;$f.p.isMove = false;
	return false;});
};







$(function(){
	$("#btn_edit").click(function(){
		$("#btn_line").prop("disabled", false).show();
		$dswork.flow.p.state = $dswork.flow.p.EDIT;
		$dswork.flow.set(null);
		$(this).hide().prop("disabled", true);
	});
	$("#btn_line").click(function(){
		$("#btn_edit").prop("disabled", false).show();
		$dswork.flow.p.state = $dswork.flow.p.LINE;
		$dswork.flow.set(null);
		$(this).hide().prop("disabled", true);
	});

	var changeTask = function(){
		$("#txt_alias").val($("#txt_alias").val().replace(/[\W|\_]/g, ""));
		if($dswork.flow.p.o instanceof $dswork.flow.MyNode){
			var o = $f.getTask();
			$dswork.flow.p.o.alias = o.alias;
			$dswork.flow.p.o.name = o.name;
			$dswork.flow.p.o.count = o.count;
			$dswork.flow.p.o.users = o.users;
			$dswork.flow.p.o.subcount = o.subcount;
			$dswork.flow.p.o.subusers = o.subusers;
			$dswork.flow.p.o.redraw();
		}
	};
	$("#txt_alias").keyup(function(){changeTask();}).mouseup(function(){changeTask();});
	$("#txt_name").keyup(function(){changeTask();}).mouseup(function(){changeTask();});
	$("#txt_count").keyup(function(){changeTask();}).mouseup(function(){changeTask();});
	$("#txt_users").keyup(function(){changeTask();}).mouseup(function(){changeTask();});
	$("#txt_subcount").keyup(function(){changeTask();}).mouseup(function(){changeTask();});
	$("#txt_subusers").keyup(function(){changeTask();}).mouseup(function(){changeTask();});
	var changeLine = function(){if($dswork.flow.p.o instanceof $dswork.flow.MyLine){
		var o = $f.getLine();
		$dswork.flow.p.o.forks = o.forks;
		$dswork.flow.p.o.redraw();
	}};
	$("#txt_forks").mouseup(function(){changeLine();}).change(function(){changeLine();});
	
	
	$("#btn_save").click(function(){
		var o = $f.getTask();
		if(o.alias != "" && o.name != ""){
			var node = new $dswork.flow.MyNode();
			node.alias = o.alias;
			node.name = o.name;
			node.count = o.count;
			node.users = o.users;
			node.subcount = o.subcount;
			node.subusers = o.subusers;
			node.y = 25;
			node.flow = $dswork.flow.p.flow;
			node.toDom();
			$f.p.flow.tasks.push(node);
			$dswork.flow.set(node);
		}
	});
	$("#btn_delete").click(function(){
		var x = $dswork.flow.p.o;
		if(x instanceof $dswork.flow.MyNode){
			if(x.alias != "start" && x.alias != "end"){x.remove();}
		}else if(x instanceof $dswork.flow.MyPoint){
			x.remove();
		}else if(x instanceof $dswork.flow.MyLine){
			x.remove();
		}
		$dswork.flow.set(null);
	});
	$("#btn_check").click(function(){
		$dswork.flow.set(null);
		var msg = $dswork.flow.check($dswork.flow.p.flow);
		if(msg == ""){
			try{console.log($dswork.flow.p.flow.toXml(true));}catch(ex){}
		}else{
			alert(msg);
		}
	});
	

	var flow = $dswork.flow.parse(document.getElementById("myFlowXML").innerHTML);
	document.getElementById("myFlowSVG").appendChild(flow.toDom());
	flow.left = $(flow.dom).position().left;
	flow.top = $(flow.dom).position().top;
	$f.line.flow = $f.p.flow = flow;
	$("#btn_edit").click();
	flow.left = $(flow.dom).position().left;
	flow.top = $(flow.dom).position().top;
});








}();