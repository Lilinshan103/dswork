package dswork.websso.model.weibo;

import com.google.gson.annotations.SerializedName;

public class WeiboUser
{
	@SerializedName("id")
	private String openid; // 用户UID
	@SerializedName("screen_name")
	private String screenName; // 微博昵称
	private String name; // 友好显示名称，如Bill Gates,名称中间的空格正常显示(此特性暂不支持)
	private int province; // 省份编码（参考省份编码表）
	private int city; // 城市编码（参考城市编码表）
	private String location; // 地址
	private String description; // 个人描述
	@SerializedName("profile_image_url")
	private String profileImageUrl; // 自定义图像
	private String gender; // 性别,m--男，f--女,n--未知
	private boolean verified; // 加V标示，是否微博认证用户
	@SerializedName("avatar_large")
	private String avatarLarge; // 大头像地址

	public String getOpenid()
	{
		return openid;
	}

	public void setOpenid(String openid)
	{
		this.openid = openid;
	}

	public String getScreenName()
	{
		return screenName;
	}

	public void setScreenName(String screenName)
	{
		this.screenName = screenName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getProvince()
	{
		return province;
	}

	public void setProvince(int province)
	{
		this.province = province;
	}

	public int getCity()
	{
		return city;
	}

	public void setCity(int city)
	{
		this.city = city;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getProfileImageUrl()
	{
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl)
	{
		this.profileImageUrl = profileImageUrl;
	}

	public String getGender()
	{
		return gender;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public boolean isVerified()
	{
		return verified;
	}

	public void setVerified(boolean verified)
	{
		this.verified = verified;
	}

	public String getAvatarLarge()
	{
		return avatarLarge;
	}

	public void setAvatarLarge(String avatarLarge)
	{
		this.avatarLarge = avatarLarge;
	}
}
