package dswork.http;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * HttpCommon主要供HttpUtil内部调用
 * @author skey
 * @version 1.0
 */
public class HttpCommon
{
	private static final String NAME_VALUE_SEPARATOR = "=";
	private static final String PARAMETER_SEPARATOR = "&";

	private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM y HH:mm:ss 'GMT'", Locale.US);
	private static Date convertGMT(String value)
	{
		if(value == null)
		{
			return null;
		}
		try
		{
			if(value.indexOf("-") == 7)
			{
				value = value.substring(0, 7) + " " + value.substring(8, 11) + " " + value.substring(12, value.length());
			}
			if(!value.endsWith("GMT") && value.lastIndexOf(" ") >= 23)
			{
				value = value.substring(0, value.lastIndexOf(" ")) + " GMT";
			}
			return sdf.parse(value);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	private static SSLSocketFactory socketFactoryForSSL;
	private static SSLSocketFactory socketFactoryForTLS;
	private static final TrustManager tm = new TM();
	static
	{
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		try
		{
			javax.net.ssl.SSLContext scSSL = javax.net.ssl.SSLContext.getInstance("SSL");
			scSSL.init(null, new TrustManager[]{tm}, null);
			socketFactoryForSSL = scSSL.getSocketFactory();
			
			javax.net.ssl.SSLContext scTLS = javax.net.ssl.SSLContext.getInstance("TLS");
			scTLS.init(null, new TrustManager[]{tm}, null);
			socketFactoryForSSL = scTLS.getSocketFactory();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private static class TM implements TrustManager, javax.net.ssl.X509TrustManager
	{
		public java.security.cert.X509Certificate[] getAcceptedIssuers()
		{
			return null;
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException
		{
			return;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) throws java.security.cert.CertificateException
		{
			return;
		}
	}
	
	public final static HostnameVerifier HV = new HostnameVerifier()
	{
		public boolean verify(String urlHostName, SSLSession session)
		{
			System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
			return true;
		}
	};

	public static String format(List<? extends NameValue> parameters, String charset)
	{
		return format(parameters, PARAMETER_SEPARATOR, charset);
	}

	public static String format(List<? extends NameValue> parameters, String parameterSeparator, String charsetName)
	{
		StringBuilder result = new StringBuilder();
		for(NameValue parameter : parameters)
		{
			try
			{
				String encodedName = java.net.URLEncoder.encode(parameter.getName(), charsetName);
				String encodedValue = java.net.URLEncoder.encode(parameter.getValue(), charsetName);
				if(result.length() > 0)
				{
					result.append(parameterSeparator);
				}
				result.append(encodedName);
				if(encodedValue != null)
				{
					result.append(NAME_VALUE_SEPARATOR);
					result.append(encodedValue);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return result.toString();
	}

	public static String parse(List<? extends Cookie> parameters, String parameterSeparator)
	{
		StringBuilder result = new StringBuilder();
		for(Cookie parameter : parameters)
		{
			if(result.length() > 0)
			{
				result.append(parameterSeparator);
			}
			result.append(parameter.getName());
			if(parameter.getValue() != null)
			{
				result.append(NAME_VALUE_SEPARATOR);
				result.append(parameter.getValue());
			}
		}
		return result.toString();
	}

	public static List<Cookie> getHttpCookies(HttpURLConnection http)
	{
		List<Cookie> list = new ArrayList<Cookie>();
		String v = null;
		String key = null;
		Date now = new Date();
		for(int i = 1; (key = http.getHeaderFieldKey(i)) != null; i++)
		{
			try
			{
				if(key.equalsIgnoreCase("set-cookie"))
				{
					v = http.getHeaderField(i);
					//System.out.print("\n****set-cookie===" + v);
					String[] arr = v.split(";");
					String[] _m = arr[0].split("=", 2);
					Cookie c = new Cookie(_m[0], _m[1]);
					Map<String, String> map = new HashMap<String, String>();
					List<NameValue> vlist = new ArrayList<NameValue>();
					String _t = null;
					for(int j = 1; j < arr.length; j++)
					{
						_t = arr[j].trim();
						try
						{
							_m = _t.split("=", 2);
							vlist.add(new NameValue(_m[0].toLowerCase(Locale.ROOT), _m[1]));
							map.put(_m[0].toLowerCase(Locale.ROOT), _m[1]);
						}
						catch(Exception ex)
						{
							if(_t.equalsIgnoreCase("secure"))
							{
								c.setSecure(true);
							}
							else if(_t.equalsIgnoreCase("httponly"))
							{
								c.setHttpOnly(true);
							}
						}
					}
					String t = map.get("expires");
					if(t != null)
					{
						c.setExpiryDate(HttpCommon.convertGMT(t));
					}
					t = map.get("max-age");
					if(t != null)
					{
						try
						{
							long d = Long.parseLong(t);
							c.setExpiryDate(new Date(now.getTime() + d));
						}
						catch(Exception ex)
						{
							ex.printStackTrace();
						}
					}
					if(c.getExpiryDate() == null || !c.isExpired(now))
					{
						t = map.get("path");
						if(t != null)
						{
							c.setPath(t);
						}
						t = map.get("domain");
						if(t != null && c.getExpiryDate() != null)
						{
							c.setDomain(t);
						}
					}
					list.add(c);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return list;
	}

	public static void refreshCookies(List<Cookie> cookies)
	{
		Date date = new Date();
		for(int i = cookies.size() - 1; i >=0; i--)
		{
			if(cookies.get(i).isExpired(date))
			{
				cookies.remove(i);// 移除超时的
			}
		}
	}
	
	public static List<Cookie> getHttpCookies(List<Cookie> cookies, boolean hasSecure)
	{
		HttpCommon.refreshCookies(cookies);
		List<Cookie> list;
		if(hasSecure)
		{
			list = cookies;
		}
		else
		{
			list = new ArrayList<Cookie>();
			for(Cookie m : cookies)
			{
				if(!m.isSecure())
				{
					list.add(m.clone());
				}
			}
		}
		return list;
	}

	public static SSLSocketFactory getSocketFactoryForSSL()
	{
		return socketFactoryForSSL;
	}

	public static SSLSocketFactory getSocketFactoryForTLS()
	{
		return socketFactoryForTLS;
	}
	
	public static TrustManager getTrustManager()
	{
		return tm;
	}
}
