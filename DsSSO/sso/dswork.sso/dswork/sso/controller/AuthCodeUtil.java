package dswork.sso.controller;

public class AuthCodeUtil
{
	// 去掉小写字母中的i,l,o，去掉大写字母中的I,L,O
	private static final String AUTHCODE_STRING = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ0123456789";
	// 随机函数
	private static final java.util.Random random = new java.util.Random();
	
	// 验证码字符数组
	private static String code = getArrayAuthCode();
	
	public static String getCode()
	{
		return AuthCodeUtil.code;
	}
	
	public static void refresh()
	{
		AuthCodeUtil.code = getArrayAuthCode();
	}

	/**
	 * 获得:随机的验证码字符数组
	 * @return 验证码字符数组
	 */
	private static String getArrayAuthCode()
	{
		StringBuilder sb = new StringBuilder(6);
		for(int i = 0; i < 6; i++)
		{
			int index = random.nextInt(AUTHCODE_STRING.length());
			sb.append(AUTHCODE_STRING.charAt(index));
		}
		return sb.toString();
	}

}
