package dswork.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * 封装http请求
 * @author skey
 * @version 1.0
 */
public class HttpUtil
{
	private HttpURLConnection http;
	private SSLSocketFactory sslSocketFactory;
	private boolean isHttps = false;
	private int connectTimeout = 10000;
	private int readTimeout = 30000;
	private String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.104";

	/**
	 * 返回当前是否https请求
	 * @return boolean
	 */
	public boolean isHttps()
	{
		return isHttps;
	}

	/**
	 * 设置超时时间毫秒
	 * @param connectTimeout int
	 * @return HttpUtil
	 */
	public HttpUtil setConnectTimeout(int connectTimeout)
	{
		this.connectTimeout = connectTimeout;
		if(http != null)
		{
			this.http.setConnectTimeout(connectTimeout);
		}
		return this;
	}

	/**
	 * 设置contentType
	 * @param contentType String
	 * @return HttpUtil
	 */
	public HttpUtil setContentType(String contentType)
	{
		if(http != null)
		{
			this.http.setRequestProperty("Content-Type", contentType);
		}
		return this;
	}

	/**
	 * 设置读取超时时间毫秒
	 * @param readTimeout int
	 * @return HttpUtil
	 */
	public HttpUtil setReadTimeout(int readTimeout)
	{
		this.readTimeout = readTimeout;
		if(http != null)
		{
			this.http.setReadTimeout(readTimeout);
		}
		return this;
	}

	/**
	 * 设置requestMethod
	 * @param requestMethod String
	 * @return HttpUtil
	 */
	public HttpUtil setRequestMethod(String requestMethod)
	{
		try
		{
			this.http.setRequestMethod(requestMethod.toUpperCase(Locale.ROOT));
		}
		catch(Exception e)
		{
		}
		return this;
	}

	/**
	 * 设置requestProperty
	 * @param key String
	 * @param value String
	 * @return HttpUtil
	 */
	public HttpUtil setRequestProperty(String key, String value)
	{
		try
		{
			this.http.setRequestProperty(key, value);
		}
		catch(Exception e)
		{
		}
		return this;
	}

	/**
	 * 设置sslSocketFactory
	 * @param sslSocketFactory SslSocketFactory
	 * @return HttpUtil
	 */
	public HttpUtil setSslSocketFactory(SSLSocketFactory sslSocketFactory)
	{
		this.sslSocketFactory = sslSocketFactory;
		return this;
	}

	/**
	 * 设置useCaches
	 * @param usecaches boolean
	 * @return HttpUtil
	 */
	public HttpUtil setUseCaches(boolean usecaches)
	{
		this.http.setUseCaches(usecaches);
		return this;
	}

	/**
	 * 设置userAgent
	 * @param userAgent String
	 * @return HttpUtil
	 */
	public HttpUtil setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
		if(http != null)
		{
			this.http.setRequestProperty("User-Agent", userAgent);
		}
		return this;
	}

	/**
	 * 创建新的http(s)请求，重置除cookie外的所有设置
	 * @param url url地址请求
	 * @return HttpUtil
	 */
	public HttpUtil create(String url)
	{
		return create(url, true);
	}

	/**
	 * 创建新的http(s)请求，重置除cookie、connectTimeout、readTimeout、userAgent外的所有设置
	 * @param url url地址请求
	 * @param isHostnameVerifier 是否不确认主机名
	 * @return HttpUtil
	 */
	public HttpUtil create(String url, boolean isHostnameVerifier)
	{
		this.clearForm();
		URL c;
		try
		{
			c = new URL(url);
			isHttps = c.getProtocol().toLowerCase().equals("https");
			this.http = (HttpURLConnection) c.openConnection();
			if(isHttps)
			{
				HttpsURLConnection https = (HttpsURLConnection) this.http;
				if(this.sslSocketFactory != null)
				{
					https.setSSLSocketFactory(this.sslSocketFactory);
				}
				if(isHostnameVerifier)
				{
					https.setHostnameVerifier(HttpCommon.HV);// 不进行主机名确认
				}
			}
			this.http.setDoInput(true);
			this.http.setDoOutput(false);
			this.http.setConnectTimeout(connectTimeout);
			this.http.setReadTimeout(readTimeout);
			this.http.setRequestProperty("User-Agent", userAgent);
			this.http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			this.http.setRequestProperty("Accept-Charset", "utf-8");
			this.http.setRequestMethod("GET");
		}
		catch(Exception e)
		{
		}
		return this;
	}

	/**
	 * 连接并返回网页文本
	 * @return 连接失败返回null
	 */
	public String connect()
	{
		return connect("UTF-8");
	}

	/**
	 * 连接并返回网页文本
	 * @param charsetName 对封装的表单、获取的网页内容进行的编码设置
	 * @return 连接失败返回null
	 */
	public String connect(String charsetName)
	{
		String result = null;
		try
		{
			if(this.cookies.size() > 0)
			{
				String _c = HttpCommon.parse(HttpCommon.getHttpCookies(this.cookies, isHttps()), "; ");
				http.setRequestProperty("Cookie", _c);
			}
			byte[] arr = null;
			if(this.form.size() > 0)
			{
				String data = HttpCommon.format(form, charsetName);
				arr = data.getBytes("ISO-8859-1");
			}
			else if(databody != null)
			{
				arr = databody;
			}
			if(arr != null)
			{
				this.http.setDoOutput(true);
				this.http.setUseCaches(false);
				if(this.http.getRequestMethod().toUpperCase().equals("GET"))// DELETE, PUT, POST
				{
					this.http.setRequestMethod("POST");
				}
				// this.http.setRequestProperty("Content-Length", String.valueOf(data.length()));
				DataOutputStream out = new DataOutputStream(this.http.getOutputStream());
				out.write(arr, 0, arr.length);
				// out.writeBytes(data);
				out.flush();
				out.close();
			}
			this.http.connect();
			int _responseCode = http.getResponseCode();// 设置http返回状态200（ok）还是403
			BufferedReader in = null;
			if(_responseCode >= 200 && _responseCode < 300)
			{
				Date date = new Date();
				List<Cookie> list = HttpCommon.getHttpCookies(http);
				for(Cookie m : list)
				{
					if(m.getExpiryDate() == null)
					{
						this.addCookie(m.getName(), m.getValue());// 会话cookie
					}
					else
					{
						if(!m.isExpired(date))
						{
							this.addCookie(m.getName(), m.getValue());
						}
					}
				}
				in = new BufferedReader(new InputStreamReader(http.getInputStream(), charsetName));
				String temp = in.readLine();
				while(temp != null)
				{
					if(result != null)
					{
						result += temp;
					}
					else
					{
						result = temp;
					}
					temp = in.readLine();
				}
				in.close();
			}
		}
		catch(Exception e)
		{
		}
		try
		{
			http.disconnect();
		}
		catch(Exception e)
		{
		}
		return result;
	}

	/**
	 * 连接并返回网页流
	 * @return 连接失败返回null
	 */
	public InputStream connectStream()
	{
		return connectStream("UTF-8");
	}

	/**
	 * 连接并返回网页流
	 * @param charsetName 对封装的表单、获取的网页内容进行的编码设置
	 * @return 连接失败返回null
	 */
	public InputStream connectStream(String charsetName)
	{
		try
		{
			if(this.cookies.size() > 0)
			{
				String _c = HttpCommon.parse(HttpCommon.getHttpCookies(this.cookies, isHttps()), "; ");
				http.setRequestProperty("Cookie", _c);
			}
			byte[] arr = null;
			if(this.form.size() > 0)
			{
				String data = HttpCommon.format(form, charsetName);
				arr = data.getBytes("ISO-8859-1");
			}
			else if(databody != null)
			{
				arr = databody;
			}
			if(arr != null)
			{
				this.http.setDoOutput(true);
				this.http.setUseCaches(false);
				if(this.http.getRequestMethod().toUpperCase().equals("GET"))// DELETE, PUT, POST
				{
					this.http.setRequestMethod("POST");
				}
				// this.http.setRequestProperty("Content-Length", String.valueOf(data.length()));
				DataOutputStream out = new DataOutputStream(this.http.getOutputStream());
				out.write(arr, 0, arr.length);
				// out.writeBytes(data);
				out.flush();
				out.close();
			}
			this.http.connect();
			int _responseCode = http.getResponseCode();// 设置http返回状态200（ok）还是403
			if(_responseCode >= 200 && _responseCode < 300)
			{
				Date date = new Date();
				List<Cookie> list = HttpCommon.getHttpCookies(http);
				for(Cookie m : list)
				{
					if(m.getExpiryDate() == null)
					{
						this.addCookie(m.getName(), m.getValue());// 会话cookie
					}
					else
					{
						if(!m.isExpired(date))
						{
							this.addCookie(m.getName(), m.getValue());
						}
					}
				}
				return http.getInputStream();
			}
		}
		catch(Exception e)
		{
		}
		try
		{
			http.disconnect();
		}
		catch(Exception e)
		{
		}
		return null;
	}
	
	public void initSocketFactoryForSSL()
	{
		this.sslSocketFactory = HttpCommon.getSocketFactoryForSSL();
	}

	public void initSocketFactoryForTLS()
	{
		this.sslSocketFactory = HttpCommon.getSocketFactoryForTLS();
	}
	
	// post的数据流，与Form数据冲突
	private byte[] databody = null;

	/**
	 * 设置数据流，优先使用form值
	 * @param arr byte[]
	 * @return HttpUtil
	 */
	public HttpUtil setDataBody(byte[] arr)
	{
		databody = arr;
		return this;
	}
	
	// 表单项
	private List<NameValue> form = new ArrayList<NameValue>();

	/**
	 * 清除已清加的表单项
	 * @return HttpUtil
	 */
	public HttpUtil clearForm()
	{
		form.clear();
		return this;
	}

	/**
	 * 添加表单项
	 * @param name String
	 * @param value String
	 * @return HttpUtil
	 */
	public HttpUtil addForm(String name, String value)
	{
		form.add(new NameValue(name, value));
		return this;
	}

	/**
	 * 批量添加表单项
	 * @param array NameValue[]
	 * @return HttpUtil
	 */
	public HttpUtil addForms(NameValue[] array)
	{
		for(NameValue c : array)
		{
			form.add(c);
		}
		return this;
	}
	// cookie
	private List<Cookie> cookies = new ArrayList<Cookie>();

	/**
	 * 清除已清加的cookie
	 * @return HttpUtil
	 */
	public HttpUtil clearCookies()
	{
		cookies.clear();
		return this;
	}

	/**
	 * 添加cookie
	 * @param name String
	 * @param value String
	 * @return HttpUtil
	 */
	public HttpUtil addCookie(String name, String value)
	{
		cookies.add(new Cookie(name, value));
		return this;
	}

	/**
	 * 批量添加cookie
	 * @param array Cookie[]
	 * @return HttpUtil
	 */
	public HttpUtil addCookies(Cookie[] array)
	{
		for(Cookie c : array)
		{
			cookies.add(c);
		}
		return this;
	}

	/**
	 * 复制cookie
	 * @param onlySessionCookie true：仅复制会话cookie false：复制非会话cookie null:全部cookie
	 * @return List&lt;Cookie&gt;
	 */
	public List<Cookie> getCloneCookies(Boolean onlySessionCookie)
	{
		List<Cookie> lists = HttpCommon.getHttpCookies(this.cookies, true);
		List<Cookie> list = new ArrayList<Cookie>();
		if(onlySessionCookie == null)
		{
			for(Cookie m : lists)
			{
				list.add(m.clone());
			}
		}
		else if(onlySessionCookie)
		{
			for(Cookie m : lists)
			{
				if(m.getExpiryDate() == null)
				{
					list.add(m.clone());
				}
			}
		}
		else
		{
			for(Cookie m : lists)
			{
				if(m.getExpiryDate() != null)
				{
					list.add(m.clone());
				}
			}
		}
		return list;
	}
}
