package dswork.sso.model;

public class LoginUser extends IUser
{
	private static final long serialVersionUID = 1L;
	// 密码
	private String password = "";
	// CA证书的KEY
	private String cakey = "";

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getPassword()
	{
		return this.password;
	}

	public void setCakey(String cakey)
	{
		this.cakey = cakey;
	}

	public String getCakey()
	{
		return this.cakey;
	}
}
