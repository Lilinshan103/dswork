package dswork.websso.model.weibo;

import com.google.gson.annotations.SerializedName;

public class WeiboTokenInfo
{
	// 唯一对应用户身份的标识
	@SerializedName("uid")
	private String openid;
	// 用户授权的scope权限。
	private String scope;
	// access_token的创建时间，从1970年到创建时间的秒数。
	private Integer create_at;
	// access_token的剩余时间，单位是秒数。
	private long expiresin;// 该access token的有效期，单位为秒。

	// appkey

	public String getOpenid()
	{
		return openid;
	}

	public void setOpenid(String openid)
	{
		this.openid = openid;
	}

	public String getScope()
	{
		return scope;
	}

	public void setScope(String scope)
	{
		this.scope = scope;
	}

	public Integer getCreate_at()
	{
		return create_at;
	}

	public void setCreate_at(Integer create_at)
	{
		this.create_at = create_at;
	}

	public long getExpiresin()
	{
		return expiresin;
	}

	public void setExpiresin(long expiresin)
	{
		this.expiresin = expiresin;
	}
}
