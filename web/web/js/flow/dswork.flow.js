var $dswork = $dswork || {};
$dswork.flow = $dswork.flow || {};



;!function(){
$ColorStart = "7BB538";
$ColorEnd = "FF6666";
$ColorPoint = "BBDDFF";
$PP = "0055AA";
$ColorTask = "6699CC";
$NN = "0055AA";
$ColorLine = "AACCEE";
$LL = "2277CC";
$dswork.flow.initEventPoint = function(dom){};
$dswork.flow.initEventLine = function(dom){};
$dswork.flow.initEventNode = function(dom){};
$dswork.flow.initEventFlow = function(dom){};



$dswork.flow.MyPoint = function(x, y){
	this.x = x; this.y = y; this.r = 4; this.opacity = 0;
	this.$color = $ColorPoint; this.color = $ColorPoint;
	this.dom = null; this.line = null;
};
$dswork.flow.MyPoint.prototype = {
	toSvg:function(){return "";}
	,toDom:function(){this.dom = document.createElementNS("http://www.w3.org/2000/svg", "rect");
		this.dom.setAttribute("x", this.x-4);this.dom.setAttribute("y", this.y-4);
		this.dom.setAttribute("width", 8);this.dom.setAttribute("height", 8);
		this.dom.setAttribute("fill", "#"+this.color);this.dom.setAttribute("stroke", "#"+$ColorTask);this.dom.setAttribute("stroke-width", 1);
		this.dom.obj = this;this.flow.dom.appendChild(this.dom);$dswork.flow.initEventPoint(this.dom);
		return this.dom;
	}
	,redraw:function(){this.dom.setAttribute("fill", "#"+this.color);this.dom.setAttribute("x", this.x-4);this.dom.setAttribute("y", this.y-4);}
	,selected:function(){this.color = $PP;this.redraw();}
	,unselected:function(){this.color = this.$color;this.redraw();}
	,remove:function(){var m = this.line;if(m != null){
		for(var j=m.points.length-1; j>=0; j--){var n = m.points[j];if(n == this){n.dom.remove();m.points.splice(j, 1);break;}}
		m.redraw();
	}}
};



$dswork.flow.MyLine = function(){
	this.from = null; this.to = null; this.points = []; this.forks="";
	this.$color = $ColorLine; this.color = $ColorLine;
	this.dom = null; this.flow = null;
};
$dswork.flow.MyLine.prototype = {
	setPoints:function(p){if(p){var s = p.split(" ");for(var i = 0; i < s.length; i++){var ss = s[i].split(",");if(ss.length == 2){var x = parseInt(ss[0]), y = parseInt(ss[1]);var p = new $dswork.flow.MyPoint(x, y);p.line = this;p.flow = this.flow;this.points.push(p);}}}else{this.points = [];}}
	,toSvg:function(){return '<g>' + this.innerHTML("svg") + '</g>';}
	,toXml:function(){var p = _pointsToString(this.points);
		return '<line to="'+this.to.alias+($ColorLine!=this.color?'" color="'+this.color:'')+(p!=''?'" p="'+p:'')+(this.forks!=''?'" forks="'+this.forks:'')+'"></line>';
	}
	,toDom:function(){this.dom = document.createElementNS("http://www.w3.org/2000/svg", "g");this.dom.obj = this;
		this.redraw();this.flow.dom.appendChild(this.dom);$dswork.flow.initEventLine(this.dom);
		return this.dom;
	}
	,innerHTML:function(xx){var e = this;
		var p = _lineInfo(e.from, e.to, e.points, 3);
		return '<polyline fill="#ffffff" fill-opacity="0" stroke="#'+e.color+'" stroke-width="3" points="'+_pointsToString(p.points)+'"></polyline>'
		+'<polygon fill="#'+e.color+'" points="'+_pointsToString(p.arrows)+'"></polygon>'
		+("svg"!=xx && e.forks!=''?'<text font-size="14" x="'+p.labelXY.x+'" y="'+p.labelXY.y+'" dy="4">['+e.forks+']</text>':'');
	}
	,redraw:function(){var e = this;e.dom.innerHTML = e.innerHTML();}
	,selected:function(){this.color = $LL;this.redraw();}
	,unselected:function(){this.color = this.$color;this.redraw();}
	,remove:function(){var ee = this.flow;if(ee != null){
		for(var i=ee.lines.length-1; i>=0; i--){var m = ee.lines[i];if(m == this){
			for(var j=m.points.length-1; j>=0; j--){var n = m.points[j];n.dom.remove();}m.dom.remove();ee.lines.splice(i, 1);
		break;}}}
	}
	,linePoints:function(){var p = _lineInfo(this.from, this.to, this.points, 3);return p.points;}
};



$dswork.flow.MyNode = function(){
	this.name=""; this.alias=""; this.count=1; this.users=""; this.subcount=-1; this.subusers="";
	this.x=0; this.y=0; this.width=100; this.height=50;
	this.$color=$ColorTask; this.color=$ColorTask; this.dom=null; this.flow=null;
};
$dswork.flow.MyNode.prototype = {
	g:function(g){if(g){var x = g.split(",");if(x.length>=2){this.x=parseInt(x[0]);this.y=parseInt(x[1]);this.width=100;this.height=50;}if(x.length==4){this.width=parseInt(x[2]);this.height=parseInt(x[3]);}}else{return this.x+","+this.y+","+this.width+","+this.height;}}
	,center:function(){return new $dswork.flow.MyPoint(parseInt(this.x+this.width/2), parseInt(this.y+this.height/2));}
	,toSvg:function(){return '<g>' + this.innerHTML("svg") + '</g>';}
	,toXml:function(l){var hc = false;
		if("start" == this.alias){if($ColorStart != this.color){hc = true;}}
		else if("end" == this.alias){if($ColorEnd != this.color){hc = true;}}
		else{if($ColorTask != this.color){hc = true;}}
		return '<task alias="' + this.alias+'" name="'+this.name+(this.count>1?'" count="'+this.count:'')+(hc?'" color="'+this.color:'')+'" users="'+this.users+'" g="'+this.g()+'"'+ (this.subcount>=0?' subcount="'+this.subcount+'" subusers="'+this.subusers+'"':'') +' >'+l+'</task>';
	}
	,toDom:function(){this.dom = document.createElementNS("http://www.w3.org/2000/svg", "g");this.dom.obj = this;
		this.dom.innerHTML = this.innerHTML();this.flow.dom.appendChild(this.dom);$dswork.flow.initEventNode(this.dom);
		return this.dom;
	}
	,innerHTML:function(xx){var e = this;var c = e.center(), r = parseInt(this.width/2);
		if("start" == e.alias || "end" == e.alias){
			return '<circle stroke="#'+e.color+'" stroke-width="2" cx="'+c.x+'" cy="'+c.y+'" r="'+r+'" fill="#FFFFFF"></circle>'
			      +'<circle cx="'+c.x+'" cy="'+c.y+'" r="'+(r-4)+'" fill="#'+e.color+'"></circle>';
		}else{
			return '<rect x="'+e.x+'" y="'+e.y+'" width="'+e.width+'" height="'+e.height+'" stroke="#'+e.color+'" stroke-width="2" fill="#CCEEFF"></rect>'
			+ _textMult(e.name, e.x, e.y, e.width, e.height, 14)
			+("svg"!=xx?'<text font-size="14" x="'+e.x+'" y="'+e.y+'" dy="-8">'+(e.count>1?'['+e.count+'] ':'')+e.alias+'</text>':'');
		}
	}
	,redraw:function(){var e = this;
		e.dom.innerHTML = e.innerHTML();
		for(var i=0; i<e.flow.lines.length; i++){if(e.flow.lines[i].from==e || e.flow.lines[i].to==e){e.flow.lines[i].redraw();}}
	}
	,selected:function(){this.color = $NN;this.redraw();}
	,unselected:function(){this.color = this.$color;this.redraw();}
	,remove:function(){var ee = this.flow;if(ee != null){
		for(var i=ee.lines.length-1; i>=0; i--){var m = ee.lines[i];if(m.from == this || m.to == this){m.remove();}}
		for(var i=ee.tasks.length-1; i>=0; i--){var m = ee.tasks[i];if(m == this){m.dom.remove();ee.tasks.splice(i, 1);}}
	}}
};



$dswork.flow.MyFlow = function(){this.tasks=[]; this.lines=[]; this.width=0; this.height=0; this.dom=null;};
$dswork.flow.MyFlow.prototype = {
	toSvg:function(prettyPrint){
		var e = this;
		var nt = "", n = "";
		if(prettyPrint){nt = "\n\t";n = "\n";};
		var s = '<svg width="'+e.width+'" height="'+e.height+'">';
		for(var i = 0; i < e.tasks.length; i++){
			s += nt + e.tasks[i].toSvg();
		}
		for(var i = 0; i < e.lines.length; i++){
			s += nt + e.lines[i].toSvg();
			for(var j = 0; j < e.lines[i].points.length; j++){
				s += e.lines[i].points[j].toSvg();
			}
		}
		s += n + '</svg>';
		return s;
	}
	,toXml:function(prettyPrint){
		var e = this;
		var nt = "", n = "";
		if(prettyPrint){nt = "\n\t";n = "\n";};
		var s = '<flow>';
		var endTask = null;
		for(var i = 0; i < e.tasks.length; i++){
			if("end" == e.tasks[i].alias){
				endTask = e.tasks[i];
			}
			else{
				var x = "";
				for(var j = 0; j < e.lines.length; j++){
					if(e.lines[j].from.alias == e.tasks[i].alias){
						x += nt + e.lines[j].toXml();
					}
				}
				x += n;
				s += n + e.tasks[i].toXml(x);
			}
		}
		s += n + (endTask!=null?endTask.toXml('')+n:'') + '</flow>';
		return s;
	}
	,toDom:function(){
		var e = this;
		e.width = 2000;
		e.height = 2000;
		e.dom = document.createElementNS("http://www.w3.org/2000/svg", "svg");
		var d = e.dom;
		d.setAttribute("alias", e.alias);
		d.setAttribute("width", e.width);
		d.setAttribute("height", e.height);
		d.setAttribute("name", e.name);
		for(var i = 0; i < e.tasks.length; i++){
			e.tasks[i].toDom();
		}
		for(var i = 0; i < e.lines.length; i++){
			e.lines[i].toDom();
			for(var j = 0; j < e.lines[i].points.length; j++){
				e.lines[i].points[j].toDom();
			}
		}
		d.obj = e;
		$dswork.flow.initEventFlow(this.dom);
		return d;
	}
};
	
	
	
$dswork.flow.parse = function(xmlStirng){
	var f = new $dswork.flow.MyFlow();
	var taskMap = {};
	function parseNode(dom){
		var m = new $dswork.flow.MyNode();
		m.alias = dom.getAttribute("alias");
		m.name = dom.getAttribute("name");
		if(dom.getAttribute("users")){
			m.users = dom.getAttribute("users");
		}
		if(dom.getAttribute("subusers")){
			m.subusers = dom.getAttribute("subusers");
		}
		if("start" == m.alias){
			m.name = "开始";
		}
		else if("end" == m.alias){
			m.name = "结束";
		}
		try{
			m.count = parseInt(dom.getAttribute("count"));
			if(isNaN(m.count) || m.count < 1){
				m.count = 1;
			}
		}
		catch(ee){
			m.count = 1;
		}
		try{
			m.subcount = parseInt(dom.getAttribute("subcount"));
			if(isNaN(m.subcount) || m.subcount < 0){
				m.subcount = -1;
			}
		}
		catch(ee){
			m.subcount = -1;
		}
		if(dom.getAttribute("color")){
			m.color = m.$color = dom.getAttribute("color");
		}else{
			if("start" == m.alias){
				m.color = m.$color = $ColorStart;
			}
			else if("end" == m.alias){
				m.color = m.$color = $ColorEnd;
			}
		}
		m.g(dom.getAttribute("g"));
		if(f.width < (m.x + m.width)){
			f.width = parseInt(m.x + m.width);
		}
		if(f.height < (m.y + m.height)){
			f.height = parseInt(m.y + m.height);
		}
		var nl = dom.getElementsByTagName("line");
		for(var i=0; i<nl.length; i++){
			var n = parseLine(nl[i]);
			n.from = m;
			f.lines.push(n);
		}
		m.flow = f;
		return m;
	}
	function parseLine(dom){
		var n = new $dswork.flow.MyLine();
		n.flow = f;
		n._to = dom.getAttribute("to");
		if(dom.getAttribute("color")){
			n.color = n.$color = dom.getAttribute("color");
		}
		// n.style = dom.getAttribute("style");
		n.setPoints(dom.getAttribute("p"));
		for(var i=0; i<n.points.length; i++){
			if(f.width < n.points[i].x){
				f.width = n.points[i].x;
			}
			if(f.height < n.points[i].y){
				f.height = n.points[i].y;
			}
		}
		if(dom.getAttribute("forks")){
			n.forks = dom.getAttribute("forks");
		}
		return n;
	}
	var div = document.createElement('div');
	div.innerHTML = xmlStirng;//.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
	var dom = div.getElementsByTagName("flow")[0];
	// f.alias = dom.getAttribute("alias");
	var nl = dom.getElementsByTagName("task");
	for(var i=0; i<nl.length; i++){
		var node = parseNode(nl[i]);
		taskMap[node.alias] = node;
		f.tasks.push(node);
	}
	for(var i=0; i<f.lines.length; i++){
		f.lines[i].to = taskMap[f.lines[i]._to];
		f.lines[i]._to = null;
	}
	f.width = parseInt(f.width + 5);
	f.height = parseInt(f.height + 5);
	return f;
}



//*********************************************************
function _textMult(text, x, y, width, height, fontSize){
	fontSize = fontSize < 10 ? 10 : fontSize;
	var len =  Math.floor(width/fontSize);// 靠0取整
	var n = Math.ceil(text.length/len); 
	var ss = [];
	for(var i = 0; i < n; i++){ss.push(text.substring(i * len, (i + 1) * len))}
	var s = "";
	if(ss.length > 0){
		var _x = (width - ss[0].length * fontSize) / 2;
		var _y = (height - ss.length * fontSize) / 2;
		x = _x>0 ? x+_x : x;
		y = _y>0 ? y+_y : y;
		s = '<text font-size="'+fontSize+'" x="'+x+'" y="'+y+'">';
		for(var i=0; i<ss.length; i++){s += '<tspan text-anchor="left" x="'+x+'" dy="'+fontSize+'">'+ss[i]+'</tspan>';}
		s += '</text>';
	}
	return s;
}



//计算任务节点和线的交点
function __crossPoint(node, point){
	var x, y, c = node.center();
	if("start" == node.alias || "end" == node.alias){
		var r = parseInt(node.width/2);
		var W = point.x-c.x, H = point.y-c.y;
		var L = Math.sqrt(Math.pow(W,2) + Math.pow(H,2));// 三角斜率
		x = Math.ceil(c.x + r*W/L);
		y = Math.ceil(c.y + r*H/L);
	}else{
		var w = [], h = [];
		// 四个边框和中心点，和点进行连线，组成两个三角形
		// 中心和点组成的三角差
		w[0] = point.x - c.x;
		h[0] = point.y - c.y;
		
		// 中心和边框上或下宽组成的三角差
		w[1] = (w[0]>0?1:-1) * (node.width/2);
		h[1] = w[0]!=0 ? h[0]/w[0]*w[1] : (node.height/2);// h[1]/w[1]=h[0]/w[0]，按三角斜率计算边
		
		// 中心和边框左或右高组成的三角差
		h[2] = (h[0]>0?1:-1) * (node.height/2);
		w[2] = h[0]!=0 ? w[0]/h[0]*h[2] : (node.width/2);// h[2]/w[2]=h[0]/w[0]，按三角斜率计算边
		
		// 如果[0]三角在里面，肯定是最小的，所以想交点只在边框上，就只需要取宽或高上的交点即可，即从[1]开始
		var mw = w[1], mh = h[1];
		for(var i = 1; i < 3; i++){
			if(Math.abs(mw) > Math.abs(w[i])){mw = w[i];mh = h[i];}// 取最小的三角
		}
		x = Math.ceil(c.x + mw);
		y = Math.ceil(c.y + mh);
	}
	// 从交点转为侧边中心点
//	if(node.x == x || node.x == x - node.width){
//		y = node.y+node.height/2;
//	}else if(node.y == y || node.y == y - node.height){
//		x = node.x+node.width/2;
//	}
	return new $dswork.flow.MyPoint(x, y);
}
// 根据两个点画箭头
function __arrow(start, end, strokeWidth){
	var aw = 6, ah = 10, h = 8, r = strokeWidth/2;
	var p = [
		 [end.x,    end.y]
		,[end.x+ah, end.y+aw]
		,[end.x+h,  end.y+r]
		,[end.x+h,  end.y]
		,[end.x+h,  end.y-r]
		,[end.x+ah, end.y-aw]
	];
	var points = [];
	if(start.x == end.x && start.y == end.y){// 当两点重叠时，箭头不用转方向
		for(var i = 0; i < p.length; i++){
			points.push(new $dswork.flow.MyPoint(p[i][0], p[i][1]));
		}
	}else{// 使用矩阵进行方向旋转
		var W = start.x-end.x, H = start.y-end.y;
		var L = Math.sqrt(Math.pow(W,2) + Math.pow(H,2));
		var c = W/L, s = H/L;
		var matrix = [
			[c, -s, (1-c)*end.x + s*end.y],
			[s,  c, (1-c)*end.y - s*end.x],
			[0,  0,                     1]
		];
		for(var i = 0; i < p.length; i++){
			var x = Math.round(matrix[0][0]*p[i][0] + matrix[0][1]*p[i][1] + matrix[0][2]);
			var y = Math.round(matrix[1][0]*p[i][0] + matrix[1][1]*p[i][1] + matrix[1][2]);
			points.push(new $dswork.flow.MyPoint(x, y));
		}
	}
	return points;
}
// 计算两个任务节点（如果存在）和线的交点并为线增加交点坐标
function _lineInfo(from, to, points, strokeWidth){
	//计算线的路径
	var arr = [];
	if(points.length == 0){
		if(from != null && to != null){
			arr = [__crossPoint(from, to.center()), __crossPoint(to, from.center())];
		}
	}else{
		if(from){arr.push(__crossPoint(from, points[0]));}
		for(var i=0; i<points.length; i++){arr.push(points[i]);}
		if(to){arr.push(__crossPoint(to, points[points.length - 1]));}
	}
	var rs = [];
	if(arr.length > 1){
		rs = __arrow(arr[arr.length-2], arr[arr.length-1], strokeWidth);
		arr[arr.length - 1] = rs[3];
	}
	//计算label位置
	var labelXY = null;
	if(arr.length % 2 == 1){
		var p = arr[(arr.length - 1) / 2];
		labelXY = new $dswork.flow.MyPoint(p.x, p.y);
	}else{
		var p1 = arr[(arr.length - 2) / 2];
		var p2 = arr[(arr.length) / 2];
		labelXY = new $dswork.flow.MyPoint(parseInt((p1.x + p2.x) / 2), parseInt((p1.y + p2.y) / 2))
	}
	return {points:arr, arrows:rs, labelXY:labelXY};
}



function _pointsToString(p){
	var s = "";
	if(p.length > 0){
		s = p[0].x + "," + p[0].y;
		for(var i = 1; i < p.length; i++){
			s += " " + p[i].x + "," + p[i].y;
		}
	}
	return s;
}



}();


