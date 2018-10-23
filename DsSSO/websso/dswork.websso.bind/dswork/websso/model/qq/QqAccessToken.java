package dswork.websso.model.qq;

import com.google.gson.annotations.SerializedName;

public class QqAccessToken
{
	// 授权令牌，Access_Token。
	@SerializedName("access_token")
	private String accesstoken;
	// 该access token的有效期，单位为秒。
	@SerializedName("expires_in")
	private long expiresin;
	// 在授权自动续期步骤中，获取新的Access_Token时需要提供的参数。
	@SerializedName("refresh_token")
	private String refreshtoken;
	// 错误码
	private Integer code;
	// 错误信息
	private String msg;

	public String getAccesstoken()
	{
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken)
	{
		this.accesstoken = accesstoken;
	}

	public long getExpiresin()
	{
		return expiresin;
	}

	public void setExpiresin(long expiresin)
	{
		this.expiresin = expiresin;
	}

	public String getRefreshtoken()
	{
		return refreshtoken;
	}

	public void setRefreshtoken(String refreshtoken)
	{
		this.refreshtoken = refreshtoken;
	}

	public Integer getCode()
	{
		return code;
	}

	public void setCode(Integer code)
	{
		this.code = code;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}
}
