package dswork.websso.model.qq;

import com.google.gson.annotations.SerializedName;

public class QqOpenid
{
	// APPID
	@SerializedName("client_id")
	private String clientid;
	// 唯一对应用户身份的标识
	private String openid;
	// 错误码
	private Integer code;
	// 错误信息
	private String msg;

	public String getClientid()
	{
		return clientid;
	}

	public void setClientid(String clientid)
	{
		this.clientid = clientid;
	}

	public String getOpenid()
	{
		return openid;
	}

	public void setOpenid(String openid)
	{
		this.openid = openid;
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
