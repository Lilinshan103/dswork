package dswork.websso.model.alipay;

import com.google.gson.annotations.SerializedName;

public class AlipayAccessToken
{
	/**
	 * 正常响应
	 */
	// 交换令牌
	@SerializedName("access_token")
	private String accesstoken;
	// 用户的userId
	@SerializedName("user_id")
	private String userid;
	// 令牌有效期
	@SerializedName("expires_in")
	private Long expiresin;
	// 刷新令牌有效期
	@SerializedName("re_expires_in")
	private Long reexpiresin;
	// 刷新令牌
	@SerializedName("refresh_token")
	private String refreshtoken;

	public String getAccesstoken()
	{
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken)
	{
		this.accesstoken = accesstoken;
	}

	public String getUserid()
	{
		return userid;
	}

	public void setUserid(String userid)
	{
		this.userid = userid;
	}

	public Long getExpiresin()
	{
		return expiresin;
	}

	public void setExpiresin(Long expiresin)
	{
		this.expiresin = expiresin;
	}

	public Long getReexpiresin()
	{
		return reexpiresin;
	}

	public void setReexpiresin(Long reexpiresin)
	{
		this.reexpiresin = reexpiresin;
	}

	public String getRefreshtoken()
	{
		return refreshtoken;
	}

	public void setRefreshtoken(String refreshtoken)
	{
		this.refreshtoken = refreshtoken;
	}
}
