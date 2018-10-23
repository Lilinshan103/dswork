package dswork.websso.model.weibo;

import com.google.gson.annotations.SerializedName;

public class WeiboAccessToken
{
	@SerializedName("access_token")
	private String accesstoken;// 授权令牌，Access_Token。
	@SerializedName("expires_in")
	private long expiresin;// 该access token的有效期，单位为秒。
	@SerializedName("refresh_token")
	private String refreshtoken;
	@SerializedName("remind_in")
	private long remindIn;
	@SerializedName("error")
	/*
	 * redirect_uri_mismatch	21322	重定向地址不匹配
	 * invalid_request	21323	请求不合法
	 * invalid_client	21324	client_id或client_secret参数无效
	 * invalid_grant	21325	提供的Access Grant是无效的、过期的或已撤销的
	 * unauthorized_client	21326	客户端没有权限
	 * expired_token	21327	token过期
	 * unsupported_grant_type	21328	不支持的 GrantType
	 * unsupported_response_type	21329	不支持的 ResponseType
	 * access_denied	21330	用户或授权服务器拒绝授予数据访问权限
	 * temporarily_unavailable	21331	服务暂时无法访问
	 * appkey permission denied	21337	应用权限不足
	 */
	private String error;// 错误
	@SerializedName("error_code")
	private Integer error_code;// 错误码
	@SerializedName("error_description")
	private String errorDescription;// 错误信息

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

	public long getRemindIn()
	{
		return remindIn;
	}

	public void setRemindIn(long remindIn)
	{
		this.remindIn = remindIn;
	}

	public String getError()
	{
		return error;
	}

	public void setError(String error)
	{
		this.error = error;
	}

	public Integer getError_code()
	{
		return error_code;
	}

	public void setError_code(Integer error_code)
	{
		this.error_code = error_code;
	}

	public String getErrorDescription()
	{
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription)
	{
		this.errorDescription = errorDescription;
	}
}
