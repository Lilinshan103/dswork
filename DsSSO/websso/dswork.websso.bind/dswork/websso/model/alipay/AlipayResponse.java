package dswork.websso.model.alipay;

import com.google.gson.annotations.SerializedName;

public class AlipayResponse
{
	// 正常token接口返回
	@SerializedName("alipay_system_oauth_token_response")
	private AlipayAccessToken accesstoken;
	// 用户信息接口返回
	@SerializedName("alipay_user_info_share_response")
	private AlipayUserinfo userinfo;
	// 错误token接口返回
	@SerializedName("error_response")
	private AlipayErrorResponse errorresponse;
	// 签名
	private String sign;

	public AlipayAccessToken getAccesstoken()
	{
		return accesstoken;
	}

	public void setAccesstoken(AlipayAccessToken accesstoken)
	{
		this.accesstoken = accesstoken;
	}

	public AlipayUserinfo getUserinfo()
	{
		return userinfo;
	}

	public void setUserinfo(AlipayUserinfo userinfo)
	{
		this.userinfo = userinfo;
	}

	public AlipayErrorResponse getErrorresponse()
	{
		return errorresponse;
	}

	public void setErrorresponse(AlipayErrorResponse errorresponse)
	{
		this.errorresponse = errorresponse;
	}

	public String getSign()
	{
		return sign;
	}

	public void setSign(String sign)
	{
		this.sign = sign;
	}
}
