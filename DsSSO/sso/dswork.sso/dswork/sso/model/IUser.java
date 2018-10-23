/**
 * @描述：用户
 */
package dswork.sso.model;

import java.io.Serializable;

public class IUser implements Serializable
{
	private static final long serialVersionUID = 1L;
	// ID
	private Long id = 0L;
	// 帐号
	private String account = "";
	// // 密码
	// private String password = "";
	// 姓名
	private String name = "";
	// 手机
	private String mobile = "";
	// 电话
	private String phone = "";
	// 状态(0,禁止,1,允许)
	private Integer status = 1;
	// 电子邮件
	private String email = "";
	// 身份证号
	private String idcard = "";
	// 工作证号
	private String workcard = "";
	// // CA证书的KEY
	// private String cakey = "";
	// 创建时间
	private String createtime = "";
	// 所属单位
	private Long orgpid = 0L;
	// 所属部门
	private Long orgid = 0L;
	// 类型
	private String type = "";
	// 类型名称
	private String typename = "";
	// 类型扩展标识
	private String exalias = "";
	// 类型扩展名称
	private String exname = "";

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getId()
	{
		return this.id;
	}

	public void setAccount(String account)
	{
		this.account = String.valueOf(account).trim();
	}

	public String getAccount()
	{
		return this.account;
	}

//	public void setPassword(String password)
//	{
//		this.password = password;
//	}
//
//	public String getPassword()
//	{
//		return this.password;
//	}
	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getMobile()
	{
		return this.mobile;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getPhone()
	{
		return this.phone;
	}

	public void setStatus(Integer status)
	{
		this.status = (status == null || status.intValue() != 1) ? 0 : 1;
	}

	public Integer getStatus()
	{
		return this.status;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getEmail()
	{
		return this.email;
	}

	public String getIdcard()
	{
		return idcard;
	}

	public void setIdcard(String idcard)
	{
		this.idcard = idcard;
	}

	public String getWorkcard()
	{
		return workcard;
	}

	public void setWorkcard(String workcard)
	{
		this.workcard = workcard;
	}

//	 public void setCakey(String cakey)
//	 {
//	 	this.cakey = cakey;
//	 }
//	
//	 public String getCakey()
//	 {
//	 	return this.cakey;
//	 }
	public void setCreatetime(String createtime)
	{
		this.createtime = createtime;
	}

	public String getCreatetime()
	{
		return this.createtime;
	}

	public Long getOrgpid()
	{
		return orgpid;
	}

	public void setOrgpid(Long orgpid)
	{
		this.orgpid = orgpid;
	}

	public Long getOrgid()
	{
		return orgid;
	}

	public void setOrgid(Long orgid)
	{
		this.orgid = orgid;
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
}
