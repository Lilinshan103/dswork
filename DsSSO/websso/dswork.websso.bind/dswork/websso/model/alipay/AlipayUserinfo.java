package dswork.websso.model.alipay;

import com.google.gson.annotations.SerializedName;

public class AlipayUserinfo
{
	// 支付宝用户id
	@SerializedName("user_id")
	private String userid = "";
	// 用户头像
	private String avatar = "";
	// 用户昵称
	@SerializedName("nick_name")
	private String nickname = "";
	// 省份
	private String province = "";
	// 城市
	private String city = "";
	// 用户性别
	private String gender = "";
	// 用户类型
	@SerializedName("user_type")
	private String usertype = "";
	// 用户状态
	@SerializedName("user_status")
	private String userstatus = "";
	// 是否通过实名认证
	@SerializedName("is_certified")
	private String iscertified  = "";
	// 是否是学生
	@SerializedName("is_student_certified")
	private String isstudentcertified = "";

	public String getUserid()
	{
		return userid;
	}

	public void setUserid(String userid)
	{
		this.userid = userid;
	}

	public String getAvatar()
	{
		return avatar;
	}

	public void setAvatar(String avatar)
	{
		this.avatar = avatar;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
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

	public String getGender()
	{
		return gender;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public String getUsertype()
	{
		return usertype;
	}

	public void setUsertype(String usertype)
	{
		this.usertype = usertype;
	}

	public String getUserstatus()
	{
		return userstatus;
	}

	public void setUserstatus(String userstatus)
	{
		this.userstatus = userstatus;
	}

	public String getIscertified()
	{
		return iscertified;
	}

	public void setIscertified(String iscertified)
	{
		this.iscertified = iscertified;
	}

	public String getIsstudentcertified()
	{
		return isstudentcertified;
	}

	public void setIsstudentcertified(String isstudentcertified)
	{
		this.isstudentcertified = isstudentcertified;
	}
}
