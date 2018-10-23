package dswork.sso;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthGlobal
{
	static Logger log = LoggerFactory.getLogger("dswork.sso");
	private static com.google.gson.GsonBuilder builder = new com.google.gson.GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
	public static com.google.gson.Gson getGson()
	{
		return builder.create();
	}
	
	private static String ssoURL = "";
	private static String ssoName = "";
	private static String ssoPassword = "";

	public static void init(String ssoURL, String ssoName, String ssoPassword)
	{
		AuthGlobal.ssoURL = ssoURL;
		AuthGlobal.ssoName = ssoName;
		AuthGlobal.ssoPassword = ssoPassword;
	}

	public static String getURL()
	{
		return ssoURL;
	}

	public static String getName()
	{
		return ssoName;
	}

//	public static String getPassword()
//	{
//		return ssoPassword;
//	}

	public static String getPwd()
	{
		String v = ssoPassword;
		return pwdMd5(v);
	}

	private static String pwdMd5(String str)
	{
		if(str != null)
		{
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
			String v = sdf.format(cal.getTime()) + str;
			StringBuilder sb = new StringBuilder();
			try
			{
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] digest = md.digest(v.getBytes("UTF-8"));
				String stmp = "";
				for(int n = 0; n < digest.length; n++)
				{
					stmp = (Integer.toHexString(digest[n] & 0XFF));
					sb.append((stmp.length() == 1) ? "0" : "").append(stmp);
				}
				return sb.toString().toUpperCase(Locale.ENGLISH);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				sb = null;
			}
		}
		return "";
	}
}
