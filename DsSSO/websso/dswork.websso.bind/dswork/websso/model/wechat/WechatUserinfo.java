package dswork.websso.model.wechat;

public class WechatUserinfo
{
	// 0: 正确返回
	private Integer errcode;
	// 如果ret<0，会有相应的错误信息提示，返回数据全部用UTF-8编码。
	private String errmsg;
	// 普通用户的标识，对当前开发者帐号唯一
	private String openid;
	// 用户在QQ空间的昵称。
	private String nickname;
	// 普通用户性别，1为男性，2为女性
	private String sex;
	// 普通用户个人资料填写的省份
	private String province;
	// 普通用户个人资料填写的城市
	private String city;
	// 国家，如中国为CN
	private String country;
	// 用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
	private String[] privilege;
	// 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
	// 请注意，在用户修改微信头像后，旧的微信头像URL将会失效，因此开发者应该自己在获取用户信息后，将头像图片保存下来，避免微信头像URL失效后的异常情况
	private String headimgurl;
	// 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的(开发者最好保存用户unionID信息，以便以后在不同应用中进行用户信息互通。)
	private String unionid;

	public Integer getErrcode()
	{
		return errcode;
	}

	public void setErrcode(Integer errcode)
	{
		this.errcode = errcode;
	}

	public String getErrmsg()
	{
		return errmsg;
	}

	public void setErrmsg(String errmsg)
	{
		this.errmsg = errmsg;
	}

	public String getOpenid()
	{
		return openid;
	}

	public void setOpenid(String openid)
	{
		this.openid = openid;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getSex()
	{
		return sex;
	}

	public void setSex(String sex)
	{
		this.sex = sex;
	}

	public String getProvince()
	{
		return province;
	}

	public void setProvince(String province)
	{
		this.province = province;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String[] getPrivilege()
	{
		return privilege;
	}

	public void setPrivilege(String[] privilege)
	{
		this.privilege = privilege;
	}

	public String getHeadimgurl()
	{
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl)
	{
		this.headimgurl = headimgurl;
	}

	public String getUnionid()
	{
		return unionid;
	}

	public void setUnionid(String unionid)
	{
		this.unionid = unionid;
	}
}
