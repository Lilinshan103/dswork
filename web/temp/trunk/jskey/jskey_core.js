var $jskey = $jskey || {};



;!function(){"use strict";



	//String.prototype.trim = function()
	//{
	//	return this.replace(/(^\s*)|(\s*$)/g, "");//移除首尾空格
	//};
	$jskey.on = function($e, et, fn)
	{
		$e.attachEvent ? 
			$e.attachEvent('on' + et, fn)
			:
			$e.addEventListener(et, fn, false);
		return $jskey;
	};
	// 根据id获得元素
	$jskey.$ = function(id)
	{
		return document.getElementById(id);
	};
	//判断是否dom
	$jskey.$isDOM = (typeof HTMLElement === 'object')?function(o){return o instanceof HTMLElement;}:function(o){return o && typeof o === 'object' && o.nodeType === 1 && typeof o.nodeName === 'string';};
	// 根据name获得元素数组
	$jskey.$byName = function(name)
	{
		return document.getElementsByName(name);
	};
	// 根据tagName获得元素数组
	$jskey.$byTagName = function(name)
	{
		return document.getElementsByTagName(name);
	};
	$jskey.$dateFormat = function(d, f)
	{
		var t = {
			"y+" : d.getFullYear(),
			"M+" : d.getMonth()+1,
			"d+" : d.getDate(),
			"H+" : d.getHours(),
			"m+" : d.getMinutes(),
			"s+" : d.getSeconds(),
			"S+" : d.getMilliseconds()
		};
		var _t;
		for(var k in t)
		{
			while(new RegExp("(" + k + ")").test(f))
			{
				_t = (RegExp.$1.length == 1) ? t[k] : ("0000000000".substring(0, RegExp.$1.length) + t[k]).substr(("" + t[k]).length);
				f = f.replace(RegExp.$1, _t + "");
			}
		}
		return f;
	};
	$jskey.$replace = function(str, t, u)
	{
		str = str + "";
		var i = str.indexOf(t);
		var r = "";
		while(i != -1)
		{
			r += str.substring(0, i) + u;// 已经匹配完的部分+替换后的字符串
			str = str.substring(i + t.length, str.length);// 未匹配的字符串内容
			i = str.indexOf(t);// 其余部分是否还包含原来的str
		}
		r = r + str;// 累加上剩下的字符串
		return r;
	};
	
	$jskey.$show = function(msg)
	{
		if((msg || "").length > 0)
		{
			alert(msg);
		}
	};
	
	// 获得执行脚本的页面URL的路径
	$jskey.$url = function()
	{
		var _p = window.location.pathname;
		return _p.substring(0, _p.lastIndexOf("/"));
	};
	$jskey.$random = function()
	{
		return (new Date().getTime()) + "";
	};
	$jskey.$link = function(path)
	{
	    var k = document.createElement("link");
	    k.rel = "stylesheet";
	    k.type = "text/css";
	    k.href = path;
	    document.getElementsByTagName("head")[0].appendChild(k);
	    k = null;
	};
	
	
	
	$jskey.$src = $jskey.$replace($jskey.$byTagName("script")[$jskey.$byTagName("script").length - 1].src + "", "\\", "/");
	//当前此js文件的目录路径
	$jskey.$path = $jskey.$src.substring(0, $jskey.$src.lastIndexOf("/") + 1);
	try{if($jskey.$path.length>7 && $jskey.$path.substring($jskey.$path.length-7)!="/jskey/"){$jskey.$path="/web/js/jskey/";}}catch(e){}
	
	
	
	//需要$jskey.$byName()方法
	//需要$jskey.$show()方法
	$jskey.checkbox =
	{
		/**
		 * 返回指定复选框组被选中的选项数组信息Array=[{id:"",value:""},...](必须有ID属性,可多单选),没有选中则返回空数组并提示信息
		 * name:复选框组名
		 * msg:没有选中时的提示信息(为空则不提示)
		 */
		getSelected:function(name, msg)
		{
			var _a = [];
			var _e = $jskey.$byName(name);// 根据name获得元素数组
			for(var i = 0;i < _e.length;i++)
			{
				if(_e[i].checked)
				{
					_a[_a.length] =
					{
						"id":_e[i].getAttribute("id"),
						"value":_e[i].value
					};
				}
			}
			if(_a.length == 0)
			{
				$jskey.$show(msg);
			}
			return _a;
		},
		/**
		 * 返回指定复选框组被选中的个数,没有选中则返回0
		 * name:复选框组名
		 */
		countSelected:function(name)
		{
			return (this.getSelected(name)).length;
		},
		/**
		 * 返回指定复选框组被选中值,并以separator分隔开,没有选中则返回""
		 * name:复选框组名
		 * sep:分隔符
		 */
		getSelectedBySeparator:function(name, sep)
		{
			var _v = "";
			var _a = this.getSelected(name);// 获得选中的元素数组
			for(var i = 0;i < _a.length;i++)
			{
				_v += ((_v == "") ? "" : sep) + _a[i].value;
			}
			return _v;
		},
		/**
		 * 返回指定复选框组被选中选项的ID(必须有ID属性),并以separator分隔开
		 * name:复选框组名
		 * sep:分隔符
		 */
		getSelectedIdBySeparator:function(name, sep)
		{
			var _v = "";
			var _a = this.getSelected(name);// 获得选中的元素数组
			for(var i = 0;i < _a.length;i++)
			{
				_v += ((_v == "") ? "" : sep) + _a[i].id;
			}
			return _v;
		},
		/**
		 * 返回指定复选框组被选中的选项信息{id:"",value:""}(必须有ID属性,仅单选),没有选中则返回null
		 * name:复选框组名
		 * msg:没有选中或非单选时的提示信息(为空则不提示)
		 */
		getSingleSelected:function(name, msg)
		{
			var _o = null;// 当前选中的选项
			var _e = $jskey.$byName(name);// 根据name获得元素数组
			for(var i = 0;i < _e.length;i++)
			{
				if(_e[i].checked)
				{
					if(_o == null)// 找到第一个选项
					{
						_o = {"id":_e[i].getAttribute("id"), "value":_e[i].value};
					}
					else// 被选中的选项不止一个
					{
						$jskey.$show(msg);
						_o = null;
						break;// 退出for
					}
				}
			}
			return _o;
		},
		/**
		 * 判断是否有选中至少一个选项
		 * name:复选框组名
		 */
		isSelected:function(name)
		{
			var _e = $jskey.$byName(name);// 根据name获得元素数组
			for(var i = 0;i < _e.length;i++)
			{
				if(_e[i].checked)
				{
					return true;
				}
			}
			return false;
		},
		/**
		 * 根据指定的状态重选目标复选框组
		 * name:复选框组名
		 * isChecked:状态,全选[true/false]/反选[null]
		 */
		reselect:function(name, chk)// chk即isChecked
		{
			var _e = $jskey.$byName(name);// 根据name获得元素数组
			if(chk == null)
			{
				for(var i = 0;i < _e.length;i++)
				{
					_e[i].checked = !(_e[i].checked);
				}
			}
			else
			{
				chk = (chk) ? true : false;
				for(var i = 0;i < _e.length;i++)
				{
					_e[i].checked = chk;
				}
			}
		}
	};
	
	
	
	//需要$jskey.$byName()方法
	//需要$jskey.$show()方法
	$jskey.radio =
	{
		/**
		 * 返回指定单选框组被选中的选项信息{id:"",value:""}(必须有ID属性),没有选中则返回null
		 * name:单选框组名
		 * msg:没有选中时的提示信息(为空则不提示)
		 */
		getSelected:function(name, msg)
		{
			var _o = null;
			var _e = $jskey.$byName(name);// 根据name获得元素数组
			for(var i = 0;i < _e.length;i++)
			{
				if(_e[i].checked)
				{
					_o = {"id":_e[i].getAttribute("id"), "value":_e[i].value};
					break;
				}
			}
			if(_o == null)//没有值被选中
			{
				$jskey.$show(msg);
			}
			return _o;
		},
		/**
		 * 根据单选框组名取得选中的ID,没有选中则返回defaultValue
		 * name:单选框组名
		 * defaultValue:指定值
		 */
		getId:function(name, defaultValue)
		{
			var _a = this.getSelected(name);
			return (_a == null)?defaultValue:_a.id;
		},
		/**
		 * 根据单选框组名取得选中的值,没有选中则返回defaultValue
		 * name:单选框组名
		 * defaultValue:指定值
		 */
		getValue:function(name, defaultValue)
		{
			var _a = this.getSelected(name);
			return (_a == null)?defaultValue:_a.value;
		},
		/**
		 * 根据单选框组名与指定值进行重选,如果存在该指定值则选中指定值,没有匹配值时,默认选中第一项
		 * name:单选框组名
		 * value:指定值
		 * isDisabled:是否使其它值为不可选状态
		 */
		reselect:function(name, value, isDisabled)
		{
			var _c = false;
			var _e = $jskey.$byName(name);// 根据name获得元素数组
			for(var i = 0;i < _e.length;i++)
			{
				if(_e[i].value == value)
				{
					_e[i].disabled = false;
					_e[i].checked = true;
					_c = true;
				}
				else
				{
					_e[i].disabled = isDisabled || false;
				}
			}
			if(!_c)
			{
				//没有值被选中,默认选中第一项
				_e[0].disabled = false;
				_e[0].checked = true;
			}
			return _c;
		},
		/**
		 * 重置单选框组
		 * name:单选框组名
		 * isDisabled:是否使其它值为不可选状态
		 */
		reset:function(name, isDisabled)
		{
			isDisabled = isDisabled ? true : false;
			var _e = $jskey.$byName(name);// 根据name获得元素数组
			for(var i = 0;i < _e.length;i++)
			{
				_e[i].disabled = isDisabled;
				_e[i].checked = false;
			}
		}
	};
	
	
	
	//需要$jskey.$()方法
	$jskey.select =
	{
		/*
		//IE下自动调整下拉框宽度,move和moveAll方法中使用
		_resetAutoWidth:function(o)
		{
			try
			{
				var tempWidth = o.style.getExpression("width");
				if(tempWidth != null)
				{
					o.style.width = "auto";
					o.style.setExpression("width", tempWidth);
					o.style.width = null;
				}
			}
			catch(e){}
		}
		,
		*/
		$fnSwap:function(o1, o2)//交换选项属性
		{
			var _t = o1.value;
			o1.value = o2.value;
			o2.value = _t;
			
			_t = o1.text;
			o1.text = o2.text;
			o2.text = _t;
			
			_t = o1.selected;
			o1.selected = o2.selected;
			o2.selected = _t;
		},
		/* public function */
		/**
		 * 判断相应的选项值是否在下拉框中存在
		 * id:下拉框对象ID
		 * optionValue:用于比较的值
		 */
		exist:function(id, v)
		{
			var _o = $jskey.$(id);
			for(var i = 0;i < _o.options.length;i++)
			{
				if(v == _o.options[i].value)
				{
					return true;
				}
			}
			return false;
		},
		/**
		 * 添加一个选项
		 * id:下拉框对象ID
		 * text:显示值
		 * value:选项值
		 */
		add:function(id, t, v)
		{
			var _o = $jskey.$(id);
			_o.options[_o.options.length] = new Option(t, v);
		},
		/**
		 * 将来源下拉框中的选项,复制到目标下拉框
		 * fromID:来源下拉框对象ID
		 * toID:目标下拉框对象ID
		 * isSelected:状态,添加选中项[true]/添加全部项[false]
		 * isClear:复制前是否清空目标下拉框,清空[true]/保留[false]
		 */
		copy:function(fromID, toID, isSelected, isClear)
		{
			var _o1 = $jskey.$(fromID);// 来源下拉框
			var _o2 = $jskey.$(toID);// 目的下拉框
			if(_o1.options.length > -1)
			{
				if(isSelected)
				{
					if(_o1.selectedIndex > -1)
					{
						if(isClear){_o2.options.length = 0;}
						for(var i = 0;i < _o1.options.length;i++)
						{
							if(_o1.options[i].selected && !this.exist(toID, _o1.options[i].value))//判断重复的内容
							{
								_o2.options[_o2.options.length] = new Option(_o1.options[i].text, _o1.options[i].value);
							}
						}
					}
					//else
					//{
					//	alert("未选择任何选项!");
					//}
				}
				else
				{
					if(isClear){_o2.options.length = 0;}
					for(var i = 0;i < _o1.options.length;i++)
					{
						_o2.options[_o2.options.length] = new Option(_o1.options[i].text, _o1.options[i].value);
					}
				}
			}
			//else
			//{
			//	alert("找不到选项!");
			//}
		},
		/**
		 * 移动选中的选项
		 * id:下拉框对象ID
		 * count:数字,移动的长度(0则根据isWay移到最上/最下)
		 * isWay:是否向上移(向上移[true],向下移[false])
		 */
		move:function(id, count, isWay)
		{
			var _o = $jskey.$(id);
			var _a = _o.options;// 所有的选项值
			if(count == 0)
			{
				var _t = null;
				if(isWay)
				{//顶
					for(var i = 0;i <_a.length;i++)
					{
						if(_a[i].selected && _t)
						{
							_o.insertBefore(_a[i], _t);
						}
						else if(!_t && !_a[i].selected)
						{
							_t = _a[i];
						}
					}
				}
				else
				{//尾
					for(var i = _a.length - 1;i > -1;i--)
					{
						if(_a[i].selected)
						{
							if(_t)
							{
								_t = _o.insertBefore(_a[i], _t);
							}
							else
							{
								_t = _o.appendChild(_a[i]);
							}
						}
					}
				}
			}
			else
			{
				if(isWay)
				{//上
					for(var j = 0;j < count;j++)
					{
						for(var i = 1;i < _a.length;i++)
						{
							if(_a[i].selected && !_a[i - 1].selected)
							{
								this.$fnSwap(_a[i], _a[i - 1]);
							}
						}
					}
				}
				else
				{//下
					for(var j = 0;j < count;j++)
					{
						for(var i = _a.length - 2;i > -1;i--)
						{
							if(_a[i].selected && !_a[i + 1].selected)
							{
								this.$fnSwap(_a[i], _a[i + 1]);
							}
						}
					}
				}
			}
		},
		/**
		 * 移除选项
		 * id:下拉框对象ID
		 * isSelected:状态,移除选中项[true]/移除全部项[false]
		 */
		remove:function(id, isSelected)
		{
			var _o = $jskey.$(id);
			if(isSelected)
			{
				//if(_o.selectedIndex == -1)
				//{
				//	alert("未选择任何选项!");
				//}
				for(var i = _o.length - 1;i >= 0;i--)
				{
					if(_o.options[i].selected)
					{
						_o.remove(i);
					}
				}
			}
			else
			{
				_o.options.length = 0;
			}
		},
		/**
		 * 根据指定的状态重选目标下拉框选项
		 * id:下拉框对象ID
		 * isSelected:状态,全选[true/false]/反选[null]
		 */
		reselect:function(id, isSelected)
		{
			var _o = $jskey.$(id);
			var _a = _o.options;
			if(isSelected == null)
			{
				for(var i = 0;i < _a.length;i++)
				{
					_a[i].selected = !_a[i].selected;
				}
			}
			else
			{
				isSelected = (isSelected) ? true : false;
				for(var i = 0;i < _a.length;i++)
				{
					_a[i].selected = isSelected;
				}
			}
		},
		/**
		 * 将来源下拉框中的选项,移动到目标下拉框
		 * fromID:来源下拉框对象ID
		 * toID:目标下拉框对象ID
		 * isSelected:状态,移动选中项[true]/移动全部项[false]
		 */
		swap:function(fromID, toID, isSelected)
		{
			var _o1 = $jskey.$(fromID);
			var _o2 = $jskey.$(toID);
			if(_o1.options.length > -1)
			{
				var _a = [];
				if(isSelected)
				{
					if(_o1.selectedIndex > -1)
					{
						// 先移除所选的内容
						for(var i = _o1.length - 1;i >= 0;i--)// 保存所选选项，并移除
						{
							if(_o1.options[i].selected)
							{
								_a[_a.length] = new Option(_o1.options[i].text, _o1.options[i].value);
								_o1.remove(i);
							}
						}
						for(var i = 0;i < _a.length;i++)
						{
							_o2.options[_o2.options.length] = _a[i];
						}
					}
					//else
					//{
					//	alert("未选择任何选项!");
					//}
				}
				else
				{
					for(var i = _o1.length - 1;i >= 0;i--)// 保存所有选项，并移除
					{
						_a[_a.length] = new Option(_o1.options[i].text, _o1.options[i].value);
						_o1.remove(i);
					}
					for(var i = 0;i < _a.length;i++)
					{
						_o2.options[_o2.options.length] = _a[i];
					}
				}
			}
			//else
			//{
			//	alert("找不到选项!");
			//}
		}
	};
	
	
	
	//需要$jskey.$url()方法
	//需要$jskey.$path变量
	//需要$jskey.$random()方法
	$jskey.dialog =
	{
		returnValue:null,
		dialogArguments:null,
		close:function(){},
		callback:function(){},
		/**
		 * 封装jquery.easyui的dialog
		 * id 弹窗ID，页面多个弹窗时必要
		 * title 窗口标题
		 * url 网址
		 * args 传递的参数(JS对象/页面对象)
		 * width=number 宽度(单位px)
		 * height=number 高度(单位px)
		 */
		showDialog:function()
		{
			var o = arguments[0];
			if(typeof(o) != "object")
			{
				o = {};
			}
			var defaults = {
				"id":"dsworkDialog",
				"title":"Window",
				"url":"",
				"width":400,"height":300,
				"args":{"data":{}, "url":""},
				"iconCls":'icon_system',
				"maximizable":false,
				"minimizable":false,
				"collapsible":false,
			    "resizable":true,
			    "modal":true,
			    "zIndex":9999
			};
			var opts = $.extend(defaults, o);
			//relative path
			if(opts.args && opts.args.url)
			{
				if(opts.args.url.indexOf("/") != 0 && opts.args.url.indexOf("http:") != 0 && opts.args.url.indexOf("https:") != 0 && o.url.indexOf("file:") != 0 && o.url.indexOf(":") != 1)
				{
					opts.args.url = $jskey.$url() + "/" + opts.args.url;
				}
			}
			$jskey.dialog.dialogArguments = {url:opts.url, args:opts.args};
			var $win = $('#'+opts.id);
			$jskey.dialog.returnValue = null;//init
			var url = opts.url + ((opts.url.indexOf("?") == -1) ? "?jskey=" : "&jskey=") + $jskey.$random();
			if($win.length)
			{
				$('iframe',$win)[0].contentWindow.location.href=url;
				$win.dialog('open');
			}
			else
			{
				$win = $('<div id="'+opts.id+'" style="overflow:hidden;"></div>').appendTo('body');
				opts.onClose = function()
				{
					$jskey.dialog.dialogArguments = null;
					$win.dialog('destroy');
					try
					{
						if(o.callback && typeof(o.callback) == "function")
						{
							o.callback();
						}
						else
						{
							$jskey.dialog.callback();
						}
					}
					catch(e)
					{
					}
				};
				opts.content = '<iframe scrolling="auto" src="" width="100%" height="100%" style="width:100%;height:100%;border:0;" frameborder="0"></iframe>';
				//opts.buttons = [{text:'Close',iconCls:'icon_ico_23',handler:function(){}}];				
				$win.dialog(opts);
				$('iframe',$win)[0].contentWindow.location.href=url;
			}
			$jskey.dialog.close = function()
			{
				$win.dialog('close');
			};
		},
		/**
		 * 封装window.showModalDialog
		 * url 网址
		 * args 传递的参数(JS对象/页面对象)
		 * width=number 宽度(单位px)
		 * height=number 高度(单位px)
		 * status=(no,auto,yes) 是否显示状态栏
		 * scroll=(auto,no,yes) 是否显示滚动条
		 * resizable=(yes,no,auto) 是否可改变窗口大小
		 */
		showModalDialog:function(url, args, width, height, status, scroll, resizable)
		{
			url += ((url.indexOf("?") == -1) ? "?jskey=" : "&jskey=") + $jskey.$random();//防止缓存
			return window.showModalDialog
			(
				url,
				args,
				(
					"help:no;center:yes;dialogWidth:" + width
					+ "px;dialogHeight:" + height
					+ "px;status:" + status
					+ ";scroll:" + scroll
					+ ";resizable:" + resizable
					+ ";dialogTop:" + ((window.screen.availHeight - height) / 3)
					+ ";dialogLeft:" + ((window.screen.availWidth - width) / 2)
				)
			);
		},
		/**
		 * 弹出模式对话框,并返回window.returnValue
		 * title 窗口标题
		 * url 需要访问的网址
		 * args 传递的参数(JS对象/页面对象)
		 * width=number 宽度(单位px)
		 * height=number 高度(单位px)
		 * reload 是否刷新父页面(true/false),默认false
		 * status=(no,auto,yes) 是否显示状态栏
		 * scroll=(auto,no,yes) 是否显示滚动条
		 * resizable=(yes,no,auto) 是否可改变窗口大小
		 */
		show:function()
		{
			var o = arguments[0];
			if(typeof(o) != "object")
			{
				o = {};
			}
			o.url = o.url || arguments[2] || "";
			if(o.url.indexOf("/") != 0 && o.url.indexOf("http:") != 0 && o.url.indexOf("https:") != 0 && o.url.indexOf("file:") != 0 && o.url.indexOf(":") != 1)
			{
				o.url = $jskey.$url() + "/" + o.url;//当前页相对路径
			}
			o.height = o.height || arguments[5] || 450;
			$jskey.dialog.returnValue = null;
			var reValue = this.showModalDialog(
				$jskey.$path + "themes/dialog/jskey_dialog.html", // 默认ModelDialogFrame的引用路径
				{
					title:o.title || arguments[1] || "", 
					url:o.url, 
					args:o.args || arguments[3] || "",
					height:o.height
				}, 
				o.width || arguments[4] || 600, 
				o.height, 
				o.status || arguments[7] || "no", 
				o.scroll || arguments[8] || "auto", 
				o.resizable || arguments[9] || "yes"
			);
			if(o.reload || arguments[6] || false)
			{
				window.location.reload();
			}
			else
			{
				var v = reValue;
				$jskey.dialog.returnValue = v;
				$jskey.dialog.callback();
				return v;
			}
		},
		/**
		 * 封装window.open
		 * url 网址
		 * target 打开的Window名称
		 * width 宽度(单位px)
		 * height 高度(单位px)
		 * top=number 窗口离屏幕顶部距离(单位px)
		 * left=number 窗口离屏幕左边距离(单位px)
		 * status=(no/yes/0/1) 是否显示状态栏
		 * scroll=(yes/no/1/0) 是否显示滚动条
		 * resizable=(no/yes/0/1) 是否可改变窗口大小
		 * menubar=(no/yes/0/1) 是否显示菜单
		 * fullscreen=(no/yes/0/1) 是否全屏
		 * toolbar=(no/yes/0/1) 是否显示工具条
		 * location=(no/yes/0/1) 是否显示地址栏
		 * directories =(no/yes/0/1) 是否显示转向按钮
		 * channelmode=(no/yes/0/1) 是否显示频道栏
		 */
		openWindow:function(url, target, width, height, top, left, status, scroll, resizable, menubar, fullscreen, toolbar, location, directories, channelmode)
		{
			url += ((url.indexOf("?") == -1) ? "?jskey=" : "&jskey=") + $jskey.$random();//防止缓存
			window.open
			(
				url,
				target,
				(
					"width=" + width
					+ ", height=" + height
					+ ", top:" + top
					+ ", left:" + left
					+ ", status=" + status
					+ ", scrollbars=" + scroll
					+ ", resizable=" + resizable
					+ ", menubar=" + menubar
					+ ", fullscreen=" + fullscreen
					+ ", toolbar=" + toolbar
					+ ", location=" + location
					+ ", directories=" + directories
					+ ", channelmode=" + channelmode
				)
			);
		},
		/**
		 * 打开窗口
		 * url 网址
		 * target 打开的Window名称
		 * width 宽度(单位px)
		 * height 高度(单位px)
		 * top=number 窗口离屏幕顶部距离(单位px)
		 * left=number 窗口离屏幕左边距离(单位px)
		 * status=(no/yes/0/1) 是否显示状态栏
		 * scroll=(yes/no/1/0) 是否显示滚动条
		 * resizable=(no/yes/0/1) 是否可改变窗口大小
		 * menubar=(no/yes/0/1) 是否显示菜单
		 * fullscreen=(no/yes/0/1) 是否全屏
		 * toolbar=(no/yes/0/1) 是否显示工具条
		 * location=(no/yes/0/1) 是否显示地址栏
		 * directories =(no/yes/0/1) 是否显示转向按钮
		 * channelmode=(no/yes/0/1) 是否显示频道栏
		 */
		open:function()
		{
			var o = arguments[0];
			if(typeof (o) != "object")
			{
				o = {};
			}
			o.width = o.width || arguments[3] || 600;
			o.height = o.height || arguments[4] || 450;
			o.top = o.top || arguments[5] || ((window.screen.availHeight - o.height) / 3);
			o.left = o.left || arguments[6] || ((window.screen.availWidth - o.width) / 2);
			this.openWindow(
				o.url || arguments[1] || "", 
				o.target || arguments[2] || "", 
				o.width, 
				o.height, 
				o.top, 
				o.left, 
				o.status || arguments[7] || "no", 
				o.scroll || arguments[8] || "yes", 
				o.resizable || arguments[9] || "yes", 
				o.menubar || arguments[10] || "no", 
				o.fullscreen || arguments[11] || "no", 
				o.toolbar || arguments[12] || "no", 
				o.location || arguments[13] || "no", 
				o.directories || arguments[14] || "no", 
				o.channelmode || arguments[15] || "no"
			);
		}
	};
	
	
	
	$jskey.dialog.showChoose = function(m){m.url = $jskey.$path + "themes/dialog/jskey_choose.html";return $jskey.dialog.showDialog(m);};
	
	
	
	$jskey.dialog.showChooseKey = function(m){m.url = $jskey.$path + "themes/dialog/jskey_choose_key.html";return $jskey.dialog.showDialog(m);};
	
	
	
	//不需要其它方法
	$jskey.Map = function()
	{
		this.$v = [];
	};
	$jskey.Map.prototype =
	{
		$fnChk:function(v)
		{
			return (typeof (v) != "string") ? v+"" : v.replace(/(^\s*)|(\s*$)/g, "");//压缩空格，等同于trim
		},
		//把对象放进map中, 新增返回true，更新返回false
		put:function(k, v)
		{
			k = this.$fnChk(k);
			if(k.length > 0)
			{
				for(var i = 0; i < this.$v.length; i++)
				{
					//存在
					if(k == this.$v[i][0])
					{
						this.$v[i][1] = v;//更新
						return false;//找到
					}
				}
				//经过for循环后没找到
				this.$v[this.$v.length] = [k, v];//新增
				return true;
			}
		},
		//尝试进行添加值，如果存在或key为null则忽略并返回false，否则增加并返回true
		putTry:function(k, v)
		{
			k = this.$fnChk(k);
			if(k.length > 0)
			{
				for(var i = 0; i < this.$v.length; i++)
				{
					//存在
					if(k == this.$v[i][0])
					{
						return false;//找到
					}
				}
				//经过for循环后没找到
				this.$v[this.$v.length] = [k, v];//新增
				return true;
			}
			return false;
		},
		//从map中移除对象
		remove:function(k)
		{
			k = this.$fnChk(k);
			if(k.length > 0)
			{
				for(var i = 0; i < this.$v.length; i++)
				{
					//存在
					if(k == this.$v[i][0])
					{
						this.$v.splice(i, 1);//找到就移除
						break;
					}
				}
			}
		},
		//从map中取得指定的对象
		get:function(k)
		{
			k = this.$fnChk(k);
			if(k.length > 0)
			{
				for(var i = 0; i < this.$v.length; i++)
				{
					//存在
					if(k == this.$v[i][0])
					{
						return this.$v[i][1];
					}
				}
			}
			return null;
		},
		//返回一维对象key
		getKeyArray:function()
		{
			var _a = [];
			for(var i = 0; i < this.$v.length; i++)
			{
				_a[_a.length] = this.$v[i][0];
			}
			return _a;
		},
		//返回一维对象value
		getValueArray:function()
		{
			var _a = [];
			for(var i = 0; i < this.$v.length; i++)
			{
				_a[_a.length] = this.$v[i][1];
			}
			return _a;
		},
		//返回一维对象key
		size:function()
		{
			return this.$v.length;
		},
		//判断是否存在，如果存在则true，否则false
		containsKey:function(k)
		{
			k = this.$fnChk(k);
			if(k.length > 0)
			{
				for(var i = 0; i < this.$v.length; i++)
				{
					//存在
					if(k == this.$v[i][0])
					{
						return true;
					}
				}
			}
			return false;
		}
	};



	//          0	1	2	3	4	5	6	7	8	9	A	B	C	D	E	F
	//0000-000F  	!	"	#	$	%	&	'	(	)	*	+	,	-	.	/
	//0010-001F 0	1	2	3	4	5	6	7	8	9	:	;	<	=	>	?
	//0020-002F @	A	B	C	D	E	F	G	H	I	J	K	L	M	N	O
	//0030-003F P	Q	R	S	T	U	V	W	X	Y	Z	[	\	]	^	_
	//0040-004F `	a	b	c	d	e	f	g	h	i	j	k	l	m	n	o
	//0050-005F p	q	r	s	t	u	v	w	x	y	z	{	|	}	~	DEL
	//0060-006F PAD	HOP	BPH	NBH	IND	NEL	SSA	ESA	HTS	HTJ	VTS	PLD	PLU	RI	SS2	SS3
	//0070-007F DCS	PU1	PU2	STS	CCH	MW	SPA	EPA	SOS	SGCI	SCI	CSI	ST	OSC	PM	APC
	//0080-008F NBSP	¡	¢	£	¤	¥	¦	§	¨	©	ª	«	¬	SHY	®	¯
	//0090-009F °	±	²	³	´	µ	¶	·	¸	¹	º	»	¼	½	¾	¿
	/**
	 * 名称命名规则说明
	 * 首字母大写,非常少用的尽可能用Custom
	 * 注:如果添加了方法形式的,记得在Validate中的case中补上
	 * --------------------------------------------------------
	 * skey:增加了默认的msg提示,以大量减少页面上的msg属性
	 * skey:修改使支持非表单形式的元素值校验,如根据父元素ID校验所有需要校验的子元素
	 * skey:增加alertMsg提示,弹出的信息可以与页面显示信息不同,默认与msg一致
	 * skey:修改部分正则表达式,并重命名所有内部函数
	 * skey:删除从未使用的部分验证,并作了部分小修改
	 * 最后更新时间:2012-05-13 23:30
	 */
	//需要$jskey.$()方法
	$jskey.validator =
	{
		"Char":{"v":/^[A-Za-z0-9_]+$/, "msg":"允许字母、数字、下划线"},
		//"Chinese":{"v":/^[\u0391-\uFFE5]+$/, "msg":"只允许中文"},
		"Chinese":{"v":/^[\u4e00-\u9fa5]+$/, "msg":"只允许中文"},
		"Email":{"v":/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/, "msg":"格式错误"},
		"English":{"v":/^[A-Za-z]+$/, "msg":"只允许字母"},
		"Enterprise":{"v":/^[\u4e00-\u9fa5\uff08\uff09]+$/, "msg":"只允许中文"},
		"Mobile":{"v":/^(1\d{10})$/, "msg":"请输入手机号码(纯数字)"},
		"Money":{"v":/^(([1-9](\d+)?)|0)(\.\d+)?$/, "msg":"请输入金额"},
		"Name":{"v":/^[\u0020\u0024-\u0025\u0028-\u002e\u0030-\u005b\u005d-\u007b\u007d-\uffef]+$/, "msg":"不允许特殊字符"},// \u0021\u0022\u0023\u0026\u0027\u002f\u005c\u007c
		"Numeral":{"v":/^\d+$/, "msg":"请输入数字"},
		"Phone":{"v":/^((0\d{2,3})|(\(0\d{2,3}\)))?(-)?[1-9]\d{6,10}(([\-0-9]+)?[^\D]{1})?$/, "msg":"请输入电话号码"},
		"Require":{"v":/\S+/, "msg":"必填"},
		"RequireCompact":{"v":/^\S+$/, "msg":"必填(无空格)"},//必填且不能有空格
		"RequireTrim":{"v":/(^[^\s]{1}(.+)?[^\s]{1}$)|(^[^\s]{1}$)/, "msg":"必填(无前后空格)"},//前后不能有空格
		"Url":{"v":/^http(s)?:\/\/[\w\-]+(\.[\w\-]+)*(:(6553[0-5]|655[0-2]\d|65[0-4]\d{2}|6[0-4]\d{3}|[1-5]\d{4}|[1-9]\d{1,3}|[1-9]))?(\/[\d\w\-\/\=\!\@\#\$\%\~\&\(\)\[\]\{\};\?\*\+\.]*)?$/, "msg":"格式错误"},
		"Zip":{"v":/^[1-9]\d{5}$/, "msg":"邮政编码不存在"},
		
		"Number":{"v":"this._Number(x.value)", "msg":"请输入数值"},
		"NumberPlus":{"v":"this._NumberPlus(x.value)", "msg":"请输入正数"},
		"NumberMinus":{"v":"this._NumberMinus(x.value)", "msg":"请输入负数"},
		"Integer":{"v":"this._Integer(x.value)", "msg":"请输入整数"},
		"IntegerPlus":{"v":"this._IntegerPlus(x.value)", "msg":"请输入正整数"},
		"IntegerMinus":{"v":"this._IntegerMinus(x.value)", "msg":"请输入负整数"},
		
		"Custom":{"v":"this._Custom(x.value, x.getAttribute('regexp'))", "msg":""},
		"DateCheck":{"v":"this._DateCheck(x.value, x.getAttribute('operator'), $jskey.$(x.getAttribute('to')).value, x.getAttribute('require'))", "msg":"日期不正确"},
		"Filename":{"v":"this._Filename(x.value)", "msg":"不能为空,且不能包含下列字符 \\ \/ \: \* \? \" < >"},
		"Filter":{"v":"this._Filter(x.value, x.getAttribute('accept'))", "msg":""},
		"Function":{"v":"this._Function(x.value, x.getAttribute('fn'))", "msg":""},
		"Group":{"v":"this._Group(x.getAttribute('name'), x.getAttribute('min'), x.getAttribute('max'))", "msg":""},// 最少选择几个
		"IdCard":{"v":"this._IdCard(x.value)", "msg":"身份证号码错误"},
		"Limit":{"v":"this._Limit(x.value.length, x.getAttribute('min'), x.getAttribute('max'))", "msg":""},
		"LimitB":{"v":"this._Limit(this._LenB(x.value), x.getAttribute('min'), x.getAttribute('max'))", "msg":""},
		"OrgCode":{"v":"this._OrgCode(x.value)", "msg":"组织机构代码错误"},
		"Password":{"v":"this._Password(x.value)", "msg":"密码不符合安全规则"},
		"Repeat":{"v":"x.value == $jskey.$(x.getAttribute('to')).value", "msg":"输入不一致"},
		"Range":{"v":"this._Range(x.value, x.getAttribute('min'), x.getAttribute('max'))", "msg":"请输入正确整数"},
		"UnitCode":{"v":"this._UnitCode(x.value)", "msg":"统一社会信用代码错误"},
		"UploadFile":{"v":"this._UploadFile(x.value)", "msg":"请上传文件"},
		"UploadFileCheck":{"v":"this._UploadFileCheck(x.value)", "msg":"请上传文件或取消(清空上传内容)"},
		"UserCard":{"v":"this._UserCard(x.value)", "msg":"身份证号码错误"},
		
		$ErrorObj:[],
		$ErrorMsg:["\u4ee5\u4e0b\u539f\u56e0\u5bfc\u81f4\u63d0\u4ea4\u5931\u8d25\uff1a\t\t\t\t"],//ErrorMsg:["以下原因导致提交失败：\t\t\t\t"],
		$AlertMsg:["\u4ee5\u4e0b\u539f\u56e0\u5bfc\u81f4\u63d0\u4ea4\u5931\u8d25\uff1a\t\t\t\t"],//AlertMsg:["以下原因导致提交失败：\t\t\t\t"],
		$num:1,// 计算，用于生成表单id不重复
		$Array:function(formID, inputID, inputObject)
		{
			var m = null;
			if(formID.length == 0)
			{
				m = {arr : []};
				var _o = (inputID.length > 0 ? $jskey.$(inputID) : inputObject);
				this.$Clear(_o);// 尝试删除验证提示
				var _dt = _o.getAttribute("datatype") || _o.getAttribute("dataType");
				if(typeof (_dt) == "object")
				{
					return m;
				}
				if(_dt.length > 0 && _dt.charAt(0) == "!")
				{
					_dt = _dt.substring(1);
				}
				if(typeof (this[_dt]) == "undefined")
				{
					return m;
				}
				m.arr.push(_o);
			}
			else
			{
				m = $jskey.$(formID) || event.srcElement;
				if(m.arr != null)
				{
					for(var i = 0;i < m.arr.length;i++)
					{
						this.$Clear(m.arr[i]);// 尝试删除验证提示
					}
				}
				m.arr = [];
				var _t = m.getElementsByTagName('*');
				var _dt;
				for(var i = 0;i < _t.length;i++)
				{
					this.$Clear(_t[i]);// 尝试删除验证提示
					if(_t[i].getAttribute)
					{
						_dt = _t[i].getAttribute("datatype") || _t[i].getAttribute("dataType");
						if(typeof (_dt) == "object")
						{
							continue;
						}
						if(_dt.length > 0 && _dt.charAt(0) == "!")
						{
							_dt = _dt.substring(1);
						}
						if(typeof (this[_dt]) == "undefined")
						{
							continue;
						}
						m.arr.push(_t[i]);
					}
				}
			}
			return m;
		},
		$OnBlur:function(obj, mode, show)
		{
			this.Validate(null, mode, null, obj, show);
		},
		$OnFocus:function(obj)
		{
			this.$Clear(obj);
		},
		Init:function()
		{
			var formID = arguments[0] || "";//formID和inputID必须有一个不为空，校验一组表单
			var mode = arguments[1] || "";//不能为空
			var inputID = arguments[2] || "";//formID和inputID必须有一个不为空，校验单个表单
			var inputObject = arguments[3] || "";//没有inputID时，直接使用对象传递
			var show = arguments[4];
			if(show == null){show = true;}
			show = show ? true : false;
			var obj = this.$Array(formID, inputID, inputObject);
			var count = obj.arr.length;
			var e = this;
			for(var i = 0;i < count;i++)
			{
				var v = obj.arr[i];
				var _dt = v.getAttribute("datatype") || v.getAttribute("dataType");
				if(typeof(_dt) == "object")
				{
					continue;
				}
				_dt = _dt + "";
				v.setAttribute("datatype", _dt);// 放回去
				if(_dt.length > 0 && _dt.charAt(0) == "!")
				{
					_dt = _dt.substring(1);// 截掉!
				}
				if(_dt.length == 0 || typeof(this[_dt]) == "undefined")
				{
					continue;
				}
				if(v.JskeyValidatorTrans){continue;}
				v.JskeyValidatorTrans = true;
				v.JskeyValidatorBlur = v.onblur;
				v.JskeyValidatorFocus = v.onfocus;
				v.JskeyValidatorShow = show;
				v.JskeyValidatorMode = 3;//mode
				v.onblur = function()
				{
					e.$OnBlur(this, this.JskeyValidatorMode, this.JskeyValidatorShow);
					if(typeof(this.JskeyValidatorBlur) == 'function')
					{
						this.JskeyValidatorBlur();
					}
				};
				v.onfocus = function()
				{
					e.$OnFocus(this);
					if(typeof(this.JskeyValidatorFocus) == 'function')
					{
						this.JskeyValidatorFocus();
					}
				};
				if(_dt == "Group")
				{
					var _g = document.getElementsByName(v.getAttribute("name"));
					for(var j = _g.length - 1;j >= 0;j--)
					{
						_g[j].JskeyValidatorObj = v;
						if(!_g[j].JskeyValidatorTrans)
						{
							_g[j].JskeyValidatorTrans = true;
							_g[j].JskeyValidatorBlur = _g[j].onblur;
							_g[j].JskeyValidatorFocus = _g[j].onfocus;
							_g[j].JskeyValidatorShow = show;
							_g[j].JskeyValidatorMode = mode;
							_g[j].onblur = function()
							{
								e.$OnBlur(this.JskeyValidatorObj, this.JskeyValidatorMode, this.JskeyValidatorShow);
								if(typeof(this.JskeyValidatorBlur) == 'function')
								{
									this.JskeyValidatorBlur();
								}
							};
							_g[j].onfocus = function()
							{
								e.$OnFocus(this.JskeyValidatorObj);
								if(typeof(this.JskeyValidatorFocus) == 'function')
								{
									this.JskeyValidatorFocus();
								}
							};
						}
					}
				}
			}
		},
		Validate:function()
		{
			var formID = arguments[0] || "";//formID和inputID必须有一个不为空，校验一组表单
			var mode = arguments[1] || "";//不能为空
			var inputID = arguments[2] || "";//formID和inputID必须有一个不为空，校验单个表单
			var inputObject = arguments[3] || "";//没有inputID时，直接使用对象传递
			var show = arguments[4];
			if(show == null){show = true;}
			show = show ? true : false;
			this.Init(formID, mode, inputID, inputObject, show);
			var obj = this.$Array(formID, inputID, inputObject);
			var count = obj.arr.length;
			var errMsg = "";
			var alertMsg = "";
			this.$ErrorMsg.length = 1;
			this.$AlertMsg.length = 1;
			this.$ErrorObj.length = 1;
			this.$ErrorObj[0] = obj;
			for(var i = 0;i < count;i++)
			{
				var x = obj.arr[i];
				var require = true;
				var _dt = x.getAttribute("datatype");
				_dt = _dt + "";
				if(_dt.charAt(0) == "!"){
					_dt = _dt.substring(1);// 截掉!
					require = false;
				}
				if((!require || x.getAttribute("require") == "false") && x.value == "")
				{
					continue;
				}
				if(x.getAttribute("msg") == null)
				{
					errMsg = this[_dt].msg;
				}
				else
				{
					errMsg = x.getAttribute("msg");
				}
				if(x.getAttribute("alertMsg") == null || typeof (x.getAttribute("alertMsg")) == "undefined")
				{
					alertMsg = errMsg;
				}
				else
				{
					alertMsg = x.getAttribute("alertMsg");
				}
				switch(_dt)
				{
					case "Number":
					case "NumberPlus":
					case "NumberMinus":
					case "Integer":
					case "IntegerPlus":
					case "IntegerMinus":
					
					case "Custom":
					case "DateCheck":
					case "Filename":
					case "Filter":
					case "Function":
					case "Group":
					case "IdCard":
					case "Limit":
					case "LimitB":
					case "OrgCode":
					case "Password":
					case "Repeat":
					case "Range":
					case "UnitCode":
					case "UploadFile":
					case "UploadFileCheck":
					case "UserCard":
						if(!eval(this[_dt].v))
						{
							this.$AddError(i, errMsg, alertMsg);
						}
						break;
					default:
						if(!this[_dt].v.test(x.value))
						{
							this.$AddError(i, errMsg, alertMsg);
						}
						break;
				}
			}
			if(this.$ErrorMsg.length > 1)
			{
				mode = mode || 3;
				if(mode < 3){mode=3;}
				var _c = this.$ErrorObj.length;
				switch(mode)
				{
					//case 2://变红并弹出提示
						//for( var i = 1;i < _c;i++)
						//{
						//	this.$ErrorObj[i].style.color = "#ff0000";
						//}
					//case 1://弹出提示
						//alert(this.$AlertMsg.join("\n"));
						//this.ErrorObj[1].focus();
						//break;
					case 4://弹出提示并显示错误信息
						alert(this.$AlertMsg.join("\n"));
					case 3://显示错误信息
						var _temp = (typeof(jQuery)=="function" && $jskey.tooltip);
						for(var i = 1;i < _c;i++)
						{
							try
							{
								var _o = document.createElement("SPAN");
								this.$num++;
								_o.id = "__ErrorMsgPanel" + this.$num;
								_o.style.color = "#ff0000";// position:absolute;display:none;z-index:9999;
								this.$Clear(this.$ErrorObj[i]);// 防止执行过快时，删不及
								this.$ErrorObj[i].JskeyValidator = _o.id;// 把错误的对象id放进对应的自定义属性中，删除信息时使用
								this.$ErrorObj[i].parentNode.appendChild(_o);// 把对象放进父容器
								_o.innerHTML = this.$ErrorMsg[i].replace(/\d+:/, "");//"*"
								//如果存在jquery，则可以使用tipx
								if(_temp)
								{
									var v = jQuery("#"+_o.id);
									var m = $("<img style=\"vertical-align:middle;\" src=\"" + $jskey.$path+"themes/tooltip/tipx.gif" + "\"/>").attr("msg", v.html());
									v.html(m);
									m.tipx();
									if(show)
									{
										m.tipx("show");
									}
								}
							}
							catch(e)
							{
								alert(e.description);
							}
						}
						break;
					default:
						alert(this.$AlertMsg.join("\n"));
						break;
				}
				return false;
			}
			return true;
		},
		// 尝试删除验证提示
		$Clear:function(o)
		{
			try
			{
				//if(style.color == "#ff0000")
				//{
				//	style.color = "";
				//}
				o.parentNode.removeChild($jskey.$(o.JskeyValidator));
				o.JskeyValidator = null;
	//			var lastNode = parentNode.childNodes[parentNode.childNodes.length - 1];
	//			if((lastNode.id + "").indexOf("__ErrorMsgPanel") == 0)
	//			{
	//				parentNode.removeChild(lastNode);
	//			}
			}
			catch(e)
			{
			}
		},
		$AddError:function(index, emsg, amsg)
		{
			this.$ErrorObj[this.$ErrorObj.length] = this.$ErrorObj[0].arr[index];
			this.$ErrorMsg[this.$ErrorMsg.length] = this.$ErrorMsg.length + ":" + emsg;
			this.$AlertMsg[this.$AlertMsg.length] = this.$AlertMsg.length + ":" + amsg;
		},
		//判断是否为格式正确的数字,小数点后可带0(如可以为-1,1,1.1等等)
		_Number:function(v)
		{
			if(!isNaN(v))
			{
				if(v.length == 0 || v.indexOf("+") != -1)
				{
					return false;
				}
				if(v.indexOf(".") == 0 || v.indexOf("-.") == 0 || v.indexOf("00") == 0 || v.indexOf("-00") == 0 || v.lastIndexOf(".") == v.length - 1)
				{
					return false;
				}
				return true;
			}
			return false;
		},
		//判断是否为正值数字(如可以为0,1.1等等)
		_NumberPlus:function(v)
		{
			if(this._Number(v))
			{
				if(v.indexOf("-") != -1)
				{
					return false;
				}
				return true;
			}
			return false;
		},
		//判断是否为负值数字(如可以为-1.1,-2等等)
		_NumberMinus:function(v)
		{
			if(this._Number(v))
			{
				if(v.indexOf("-") != -1)
				{
					return true;
				}
			}
			return false;
		},
		//判断是否为整数(如可以为-1,1等等)
		_Integer:function(v)
		{
			if(this._Number(v))
			{
				if(v.indexOf(".") != -1)
				{
					return false;
				}
				return true;
			}
			return false;
		},
		//判断是否为正整数(如可以为2等等)
		_IntegerPlus:function(v)
		{
			if(this._Integer(v))
			{
				if(v.indexOf("-") != -1)
				{
					return false;
				}
				return true;
			}
			return false;
		},
		//判断是否为负整数(如可以为-2,-0等等,注0只能为-0)
		_IntegerMinus:function(v)
		{
			if(this._Integer(v))
			{
				if(v.indexOf("-") != -1)
				{
					return true;
				}
			}
			return false;
		},
		_Custom:function(op, reg)
		{
			return new RegExp(reg, "g").test(op);
		},
		_DateCheck:function(op1, operator, op2, require)
		{
			if(require == "false" && op2.length == 0)
			{
				return true;//一个为空时是否不较验
			}
			try
			{
				if(op1.length == 0 || op2.length == 0)
				{
					return false;
				}
				var d1 = _$ToDate(op1);
				var d2 = _$ToDate(op2);
				if(typeof (d1) != "object" || typeof (d2) != "object")
				{
					return false;
				}
				switch(operator)
				{
					case "==":
						return(d1 == d2);
					case "!=":
						return(d1 != d2);
					case ">":
						return(d1 > d2);
					case ">=":
						return(d1 >= d2);
					case "<":
						return(d1 < d2);
					case "<=":
						return(d1 <= d2);
					default:
						return(d1 >= d2);
				}
			}
			catch(e)
			{
			}
			return false;
			function _$ToDate(op)
			{
				try
				{
					var o, _y, _M, _d;
					o = op.match(new RegExp("^(\\d{4})([-./])(\\d{1,2})\\2(\\d{1,2})"));//(\\n指匹配第几个括号,如\\2)
					// if(o == null){return "";}
					_d = o[4];
					_M = o[3] * 1;
					_y = o[1];
					if(!parseInt(_M)){return "";}
					_M = _M == 0 ? 12 : _M;
					return new Date(_y, _M - 1, _d);
				}
				catch(ee)
				{
				}
				return "";
			}
		},
		//合法文件名,文件名不能包含\/:*?"<>
		_Filename:function(v)
		{
			if(v.length == 0)
			{
				return false;
			}
			if(v.indexOf("\\") == -1
					&& v.indexOf("\/") == -1
					&& v.indexOf("\:") == -1
					&& v.indexOf("\*") == -1
					&& v.indexOf("\?") == -1
					&& v.indexOf("\"") == -1
					&& v.indexOf("<") == -1
					&& v.indexOf(">") == -1
					&& v.indexOf(".") != 0
					&& v.lastIndexOf(".") != (v.length - 1)
				)
			{
				return true;
			}
			return false;
		},
		_Filter:function(input, filter)
		{
			return new RegExp("^.+\.(?=EXT)(EXT)$".replace(/EXT/g, filter.split(/\s*,\s*/).join("|")), "gi").test(input);
		},
		_Function:function(value, fn)
		{
			var result = false;
			value = $jskey.$replace(value, "\"", "\\\"");
			value = $jskey.$replace(value, "\r", "");
			value = $jskey.$replace(value, "\n", "");
			eval("result = " + fn + "(\"" + $jskey.$replace(value, "\"", "\\\"") + "\")");
			return result;
		},
		_Group:function(name, min, max)
		{
			var _g = document.getElementsByName(name);
			var chk = 0;
			min = min || 1;
			max = max || _g.length;
			for(var i = _g.length - 1;i >= 0;i--)
			{
				if(_g[i].checked)
				{
					chk++;
				}
			}
			return min <= chk && chk <= max;
		},
		// 中国大陆
		_ID_CN:function(v)
		{
			//如果布尔对象没有初始值或是0,-0,null,"",false,无定义的或NaN,对象就设置为假.不然它就是真(哪怕是字符串值为"false")
			var _iSum = 0;
			//18位不能为15位
			if(!(/^\d{17}([X\d]{1})$/i.test(v)))// || /^\d{15}$/i.test(v)
			{
				return false;// 号码位数不对
			}
			v = v.replace(/[X]{1}$/i, "a");//忽略大小写的以非数字结尾的替换为"a"，a为10(需要11进制)
			//"11":"北京","12":"天津","13":"河北","14":"山西","15":"内蒙古",
			//"21":"辽宁","22":"吉林","23":"黑龙江",
			//"31":"上海","32":"江苏","33":"浙江","34":"安徽","35":"福建","36":"江西","37":"山东",
			//"41":"河南","42":"湖北","43":"湖南","44":"广东","45":"广西","46":"海南",
			//"50":"重庆","51":"四川","52":"贵州","53":"云南","54":"西藏",
			//"61":"陕西","62":"甘肃","63":"青海","64":"宁夏","65":"新疆",
			//"71":"台湾",
			//"81":"香港","82":"澳门",
			//"91":"国外"
			//前两位数必须是上面定义的city
			if("11_12_13_14_15_21_22_23_31_32_33_34_35_36_37_41_42_43_44_45_46_50_51_52_53_54_61_62_63_64_65_71_81_82_91".indexOf(v.substr(0, 2)) == -1)
			{
				return false;// 非法地区
			}
			// 15位身份证转换为18位
			/**
			if (v.length == 15)
			{
				v = v.substring(0, 6) + "19" + v.substring(6, 15);
				var _i = 0;
				var _ti = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
				var _tc = ['1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'];
				for(var i = 0; i < 17; i++)
				{
					_i += v.substr(i, 1) * _ti[i];
				}
				v += _tc[_i % 11];
			}
			*/
			//取出生日日期部分
			var _bd = v.substr(6, 4) + "/" + v.substr(10, 2) + "/" + v.substr(12, 2);
			//判断生成的日期所转成的字符串是否与之前的字符串相同
			if(_bd != $jskey.$dateFormat(new Date(_bd), "yyyy/MM/dd"))
			{
				return false;// 非法生日
			}
			for(var i = 17; i >= 0; i--)
			{
				_iSum += (Math.pow(2, i) % 11) * parseInt(v.charAt(17 - i), 11);
			}
			if(_iSum%11 != 1)
			{
				return false;// 非法证号
			}
			return true;
		},
		// 中国香港
		_ID_HK:function(v)
		{
			if(!(/^([A-Z]{1})\d{6}\(([0-9A]{1})\)$/.test(v)))
			{
				return false;
			}
			var i1 = v.charCodeAt(0) - 64;
			var i2 = v.charCodeAt(1) - 48;
			var i3 = v.charCodeAt(2) - 48;
			var i4 = v.charCodeAt(3) - 48;
			var i5 = v.charCodeAt(4) - 48;
			var i6 = v.charCodeAt(5) - 48;
			var i7 = v.charCodeAt(6) - 48;
			var i8 = v.charCodeAt(8);
			i8 = i8 - (i8 == 65 ? 55: 48);
			var _iSum = i1*8 + i2*7 + i3*6 + i4*5 + i5*4 +i6*3 + i7*2 + i8;
			if(_iSum%11 != 0)
			{
				return false;// 非法证号
			}
			return true;
		},
		// 中国台湾
		_ID_TW:function(v)
		{
			if(!(/^([A-Z]{1})([12]{1})\d{8}$/.test(v)))
			{
				return false;
			}
			//A台北市10   B台中市11   C基隆市12   D台南市13   E高雄市14   F台北县15   G宜兰县16 
			//H桃园县17   I嘉义市34   J新竹县18   K苗栗县19   L台中县20   M南投县21   N彰化县22 
			//O新竹市35   P云林县23   Q嘉义县24   R台南县25   S高雄县26   T屏东县27 
			//U花莲县28   V台东县29   W金门县30   X澎湖县31   Y阳明山32   Z连江县33 
			var d1 = parseInt(("ABCDEFGHJKLMNPQRSTUVXYWZIO".indexOf(v.charAt(0)) + 10), 10);
			
			var x1 = parseInt(d1/10, 10);// d1十位数上的数字
			var x2 = d1%10;// d2个位数上的数字
			
			// d2-9代表第2至第9位数字
			var d2 = parseInt(v.charAt(1), 10);// 性别1男2女
			var d3 = parseInt(v.charAt(2), 10);
			var d4 = parseInt(v.charAt(3), 10);
			var d5 = parseInt(v.charAt(4), 10);
			var d6 = parseInt(v.charAt(5), 10);
			var d7 = parseInt(v.charAt(6), 10);
			var d8 = parseInt(v.charAt(7), 10);
			var d9 = parseInt(v.charAt(8), 10);
			var d10 = parseInt(v.charAt(9), 10);
			
			var _iSum = x1 + x2*9 + d2*8 + d3*7 + d4*6 + d5*5 + d6*4 + d7*3 + d8*2 + d9;
			if(d10 == 10 - _iSum%10)
			{
				return true;
			}
			return false;
		},
		// 中国澳门
		_ID_MO:function(v)
		{
			if(!(/^([157]{1})\d{6}\(\d{1}\)$/.test(v)))
			{
				return false;
			}
			return true;
		},
		_IdCard:function(v)
		{
			return this._ID_CN(v);
		},
		_Limit:function(len, min, max)
		{
			min = min || 0;
			max = max || Number.MAX_VALUE;
			return min <= len && len <= max;
		},
		_LenB:function(v)
		{
			return v.replace(/[^\x00-\xff]/g, "***").length;
		},
		// 组织机构代码检验规则，参考了规范GB11714-1997
		_OrgCode:function(v)
		{
			//9位(数字及大写字母)
			if(!(/^([A-Z\d]{9})$/.test(v)))
			{
				return false;// 号码位数不对
			}
			var CC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";// 下标位值0-35
			var WW = [3,  7,  9, 10, 5, 8,  4,  2];// 加权因子对应1-8位数值
			// 计算方式
			// 由1-8位字符对应的位置，从CC取出其位于的下标值[C]，如0位于0，A位于10，Y位于34
			// 再分别与WW中加权因子[W]进行相乘
			// SUM8 = C1*W1 + C2*W2 + ... + C8*W8
			// 将8个乘积的和计算整数，用11求余即，[X] = MOD(SUM8, 11)
			// 第9位的值，即校验码的值为：C9 = 11 - X，如果C9为10，则换为X表示，其余则用原数值代替
			var iSum = 0;
			for(var i = 0; i < 8; i++)
			{
				iSum += CC.indexOf(v.charAt(i)) * WW[i];
			}
			var C9 = 11 - iSum%11;
			if(C9 == 11)
			{
				C9 = 0;
			}
			if(C9 == 10)
			{
				C9 = 'X';
			}
			if(C9 == v.charAt(8))
			{
				return true;
			}
			return false;
		},
		_Password:function(v)
		{
			return !(/^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/.test(v));
		},
		_Range:function(v, min, max)
		{
			min = min || (-1 * Number.MAX_VALUE);
			max = max || Number.MAX_VALUE;
			return this._Integer(v) && parseInt(min) <= parseInt(v) && parseInt(v) <= parseInt(max);
		},
		// 统一社会信用代码检验规则，仅参考了规范GB32100-2015和GB11714-1997，考虑到实际行政区划变化大并且更新慢，故未按GB/T2260校验3-8位
		_UnitCode:function(v)
		{
			//18位(数字及大写字母[不含IOSVZ])
			if(!(/^([ABCDEFGHJKLMNPQRTUWXY\d]{18})$/.test(v)))
			{
				return false;// 号码位数不对
			}
			//校验组织机构代码部分
			if(!this._OrgCode(v.substr(8, 9)))
			{
				return false;
			}
			//登记管理部门代码[第一位](机构类别代码[第二位])，即前两位为其组合
			//1:机构编制(1:机关，2:事业单位，3:中央编办直接管理机构编制的群众团体，9:其他)
			//2:外交(1:所有机构)
			//3:教育(1:所有机构)
			//4:公安(1:所有机构)
			//5:民政(1:社会团体，2:民办非企业单位，3:基金会，9:其他)
			//6:司法(1:所有机构)
			//7:交通运输(1:所有机构)
			//8:文化(1:所有机构)
			//9:工商(1:企业，2:个体工商户，3:农民专业合作社，9:其他)
			//A:旅游局(1:所有机构)
			//B:宗教事务管理(1:所有机构)
			//C:全国总工会(1:所有机构)
			//D:人民解放军总后勤部(1:所有机构)
			//E:省级人民政府(1:所有机构)
			//F:地、市（设区）级人民政府(1:所有机构)
			//G:区、县级人民政府(1:所有机构)
			//Y:其他(1:所有机构)
			if("11_12_13_19_21_31_41_51_52_53_59_61_71_81_91_92_93_99_A1_B1_C1_D1_E1_F1_G1_Y1".indexOf(v.substr(0, 2)) == -1)
			{
				return false;// 非法登记管理部门代码、机构类别代码
			}
			
			var CC = "0123456789ABCDEFGHJKLMNPQRTUWXY0";// 下标位值0-30，当余数为0时，用来取第31（即第0位）
			var WW = [1,  3,  9, 27, 19, 26, 16, 17, 20, 29, 25, 13,  8, 24, 10, 30, 28];// 加权因子对应1-17位数值
			// 计算方式
			// 由1-17位字符对应的位置，从CC取出其位于的下标值[C]，如0位于0，A位于10，Y位于30
			// 再分别与WW中加权因子[W]进行相乘
			// SUM17 = C1*W1 + C2*W2 + ... + C17*W17
			// 将17个乘积的和计算整数，用31求余即，[X] = MOD(SUM17, 31)
			// 第18位的值，即校验码的值为：C18 = 31 - X，同样，以C18为下标，取CC对应的值即为校验码
			var iSum = 0;
			for(var i = 0; i < 17; i++)
			{
				iSum += CC.indexOf(v.charAt(i)) * WW[i];
			}
			if(CC.charAt(31 - iSum%31) == v.charAt(17))// 当余数为0时，31-0=31，原本应该取0值的，但CC增加了第31位用来代替0，故这里不需要再处理
			{
				return true;
			}
			return false;
		},
		//用于jskey_upload或jskey_multiupload的校验,不为0和空则为已上传,等于0或空则为未上传
		_UploadFile:function(v)
		{
			if(v != "")
			{
				if(v == "0")
				{
					return false;
				}
				return true;
			}
			return false;
		},
		//用于jskey_upload或jskey_multiupload的校验,file表单为空则返回true,不为空则选择了文件未进行上传
		_UploadFileCheck:function(v)
		{
			return v == "";
		},
		_UserCard:function(v)
		{
			if(this._ID_CN(v))// 中国大陆身份证
			{
				return true;
			}
			else if(this._ID_HK(v))// 中国香港身份证
			{
				return true;
			}
			else if(this._ID_TW(v))// 中国台湾身份证
			{
				return true;
			}
			else if(this._ID_MO(v))// 中国澳门身份证
			{
				return true;
			}
			return false;
		},
		//默认自带一些校验方法
		submit:function(containerid, submitid, type)
		{
			if(this.Validate(containerid, 3))
			{
				if(type == null || type != "select")
				{
					var _msg = "是否确定提交?";
					if(type == "insert")
						_msg = "是否确定提交?";
					else if(type == "update")
						_msg = "是否确定保存?";
					if(!confirm(_msg))
					{
						return false;
					}
				}
				$jskey.$(submitid).click();
				return true;
			}
			return false;
		}
	};
	
	
	
	if(true){(new Image()).src = $jskey.$path+"themes/tooltip/tipx.gif";}



}();



//for 页面模块加载、Node.js运用、页面普通应用
"function" === typeof define ? define(function() {
  return $jskey;
}) : "undefined" != typeof exports ? module.exports = $jskey : window.$jskey = $jskey;


