package dswork.sso.http;

import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * HttpCommon主要供HttpUtil内部调用
 * @author skey
 * @version 1.0
 */
public class HttpCommon
{
	private static final String NAME_VALUE_SEPARATOR = "=";
	private static final String PARAMETER_SEPARATOR = "&";

	private static javax.net.ssl.SSLSocketFactory socketFactoryDefault;
	private static javax.net.ssl.SSLSocketFactory socketFactory;
	static
	{
		initSocketFactoryDefault();
	}

	private static class TM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager
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

	private static void initSocketFactoryDefault()
	{
		try
		{
			socketFactoryDefault = HttpsURLConnection.getDefaultSSLSocketFactory();
			javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
			javax.net.ssl.TrustManager tm = new TM();
			trustAllCerts[0] = tm;
			javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, null);
			socketFactory = sc.getSocketFactory();
		}
		catch(Exception e)
		{
			e.printStackTrace();
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

	public static javax.net.ssl.SSLSocketFactory getSocketFactoryDefault()
	{
		return socketFactoryDefault;
	}

	public static javax.net.ssl.SSLSocketFactory getSocketFactory()
	{
		return socketFactory;
	}
}
