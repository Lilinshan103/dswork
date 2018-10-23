package dswork.websso.util;

import dswork.core.util.EnvironmentUtil;
import dswork.http.HttpUtil;
import dswork.websso.model.qq.QqAccessToken;
import dswork.websso.model.qq.QqOpenid;
import dswork.websso.model.qq.QqUserinfo;

public class QqUtil
{
	public static final String APPID = EnvironmentUtil.getToString("websso.qq.appid", "");
	public static final String APPKEY = EnvironmentUtil.getToString("websso.qq.appkey", "");
	public static final String RETURNURL = EnvironmentUtil.getToString("websso.qq.returnurl", "");

	public static final String API_ACCESSTOKEN = "https://graph.qq.com/oauth2.0/token";
	public static final String API_OPENID = "https://graph.qq.com/oauth2.0/me";
	public static final String API_USERINFO = "https://graph.qq.com/user/get_user_info";

	private QqUtil()
	{
	}

	public static QqAccessToken getAccessToken(String code)
	{
		String url = new StringBuilder(API_ACCESSTOKEN)
				.append("?grant_type=").append("authorization_code")
				.append("&client_id=").append(APPID)
				.append("&client_secret=").append(APPKEY)
				.append("&code=").append(code)
				.append("&redirect_uri=").append(RETURNURL)
				.toString();
		String query = new HttpUtil().create(url)
				.setRequestMethod("GET")
				.connect();
		String json = queryToJson(query);
		return GsonUtil.toBean(json, QqAccessToken.class);
	}

	public static QqOpenid getOpenid(String access_token)
	{
		String url = new StringBuilder(API_OPENID)
				.append("?access_token=").append(access_token)
				.toString();
		String json = new HttpUtil().create(url)
				.setRequestMethod("GET")
				.connect();
		json = json.substring(json.indexOf("(") + 1, json.indexOf(")")).trim();
		return GsonUtil.toBean(json, QqOpenid.class);
	}

	public static QqUserinfo getUserInfo(String access_token, String openid)
	{
		String url = new StringBuilder(API_USERINFO)
				.append("?access_token=").append(access_token)
				.append("&oauth_consumer_key=").append(APPID)
				.append("&openid=").append(openid)
				.toString();
		String json = new HttpUtil().create(url)
				.setRequestMethod("GET")
				.connect();
		QqUserinfo po = GsonUtil.toBean(json, QqUserinfo.class);
		po.setOpenid(openid);// 保存openid
		return po;
	}

	public static String queryToJson(String query)
	{
		StringBuilder sb = new StringBuilder("{");  
		String[] term = query.split("&");
		if(term.length > 0)
		{
			String[] ss = term[0].split("=");
			if(ss.length == 2)
			{
				sb.append("\"").append(ss[0]).append("\":");
				sb.append("\"").append(ss[1]).append("\"");
			}
			for(int i = 1; i < term.length; i++)
			{
				ss = term[i].split("=");
				if(ss.length == 2)
				{
					sb.append(",");
					sb.append("\"").append(ss[0]).append("\":");
					sb.append("\"").append(ss[1]).append("\"");
				}
			}
		}
		sb.append("}");
		return sb.toString();
	}
}
