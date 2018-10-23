/**
 * DS_WEBSSO_USERModel
 */
package dswork.websso.model;

public class DsWebssoUser
{
	// 主键
	private Long id = 0L;
	// SSO账号
	private String ssoaccount = "";
	// 国家
	private String country = "";
	// 省份
	private String province = "";
	// 城市
	private String city = "";
	// 身份证号
	private String idcard = "";
	// 姓名
	private String name = "";
	// 性别(0未知,1男,2女)
	private int sex = 0;
	// 电子邮件
	private String email = "";
	// 手机
	private String mobile = "";
	// 电话
	private String phone = "";
	// 头像
	private String avatar = "";
	// qq登陆的openid
	private String openidqq = "";
	// 支付宝登陆的openid
	private String openidalipay = "";
	// 微信登陆的openid
	private String openidwechat = "";
	// 微博登陆的openid
	private String openidweibo = "";
	// 状态(0,禁止,1,允许)
	private Integer status = 1;
	// 类型
	private String type = "";
	// 类型名称
	private String typename = "";
	// 类型扩展标识
	private String exalias = "";
	// 类型扩展名称
	private String exname = "";
	// 密码
	private String password = "";

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getSsoaccount()
	{
		return ssoaccount;
	}

	public void setSsoaccount(String ssoaccount)
	{
		this.ssoaccount = ssoaccount;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
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

	public String getIdcard()
	{
		return idcard;
	}

	public void setIdcard(String idcard)
	{
		this.idcard = idcard;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getSex()
	{
		return sex;
	}

	public void setSex(int sex)
	{
		this.sex = sex;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getAvatar()
	{
		return avatar;
	}

	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}

	public String getOpenidqq()
	{
		return openidqq;
	}

	public void setOpenidqq(String openidqq)
	{
		this.openidqq = openidqq;
	}

	public String getOpenidalipay()
	{
		return openidalipay;
	}

	public void setOpenidalipay(String openidalipay)
	{
		this.openidalipay = openidalipay;
	}

	public String getOpenidwechat()
	{
		return openidwechat;
	}

	public void setOpenidwechat(String openidwechat)
	{
		this.openidwechat = openidwechat;
	}

	public String getOpenidweibo()
	{
		return openidweibo;
	}

	public void setOpenidweibo(String openidweibo)
	{
		this.openidweibo = openidweibo;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = (status == null || status.intValue() != 1) ? 0 : 1;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getTypename()
	{
		return typename;
	}

	public void setTypename(String typename)
	{
		this.typename = typename;
	}

	public String getExalias()
	{
		return exalias;
	}

	public void setExalias(String exalias)
	{
		this.exalias = exalias;
	}

	public String getExname()
	{
		return exname;
	}

	public void setExname(String exname)
	{
		this.exname = exname;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}
