package dswork.websso.model.wechat;

import com.google.gson.annotations.SerializedName;

/**
 * 获取微信用户Access_token返回类
 * @author hasee
 */
public class WechatAccessToken
{
	// 授权令牌:用于获取用户信息
	@SerializedName("access_token")
	private String accesstoken;
	// 授权用户唯一标识
	private String openid;
	// 令牌有效期:交换令牌的有效期，单位秒，默认7200，2小时
	@SerializedName("expires_in")
	private String expiresin;
	// 刷新令牌:通过该令牌可以刷新access_token
	@SerializedName("refresh_token")
	private String refreshtoken;
	// 用户授权的作用域，使用逗号（,）分隔
	private String scope;
	// 当且仅当该网站应用已获得该用户的userinfo授权时，才会出现该字段。
	private String unionid;
	// 返回码，正确时为“10000”
	private Integer errcode;
	// 返回码对应说明
	private String errmsg;

	public String getAccesstoken()
	{
		return accesstoken;
	}

	public void setAccesstoken(String accesstoken)
	{
		this.accesstoken = accesstoken;
	}

	public String getOpenid()
	{
		return openid;
	}

	public void setOpenid(String openid)
	{
		this.openid = openid;
	}

	public String getExpiresin()
	{
		return expiresin;
	}

	public void setExpiresin(String expiresin)
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

	public String getScope()
	{
		return scope;
	}

	public void setScope(String scope)
	{
		this.scope = scope;
	}

	public String getUnionid()
	{
		return unionid;
	}

	public void setUnionid(String unionid)
	{
		this.unionid = unionid;
	}

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
}
