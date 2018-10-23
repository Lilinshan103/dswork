package dswork.websso.model.alipay;

import com.google.gson.annotations.SerializedName;

/**
 * 支付宝获取token出错时返回
 * @author hasee
 */
public class AlipayErrorResponse
{
	private Integer code;
	private String msg;
	@SerializedName("sub_code")
	private String subcode;
	@SerializedName("sub_msg")
	private String submsg;

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

	public String getSubcode()
	{
		return subcode;
	}

	public void setSubcode(String subcode)
	{
		this.subcode = subcode;
	}

	public String getSubmsg()
	{
		return submsg;
	}

	public void setSubmsg(String submsg)
	{
		this.submsg = submsg;
	}
}
