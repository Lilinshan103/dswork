package dswork.websso.util;

import dswork.core.util.EnvironmentUtil;
import dswork.http.HttpUtil;
import dswork.websso.model.weibo.WeiboAccessToken;
import dswork.websso.model.weibo.WeiboTokenInfo;
import dswork.websso.model.weibo.WeiboUser;

public class WeiboUtil
{
	public static final String APPID = EnvironmentUtil.getToString("websso.weibo.appid", "");
	public static final String APPKEY = EnvironmentUtil.getToString("websso.weibo.appkey", "");
	public static final String RETURNURL = EnvironmentUtil.getToString("websso.weibo.returnurl", "");

	public static final String API_ACCESSTOKEN = "https://api.weibo.com/oauth2/access_token";
	public static final String API_TOKEN_INFO = "https://api.weibo.com/oauth2/get_token_info";
	public static final String API_USER = "https://api.weibo.com/2/users/show.json";
	

	private WeiboUtil()
	{
	}

	public static WeiboAccessToken getAccessToken(String code)
	{
		String url = new StringBuilder(API_ACCESSTOKEN)
				.append("?client_id=").append(APPID)
				.append("&client_secret=").append(APPKEY)
				.append("&grant_type=").append("authorization_code")
				.append("&redirect_uri=").append(RETURNURL)
				.append("&code=").append(code)
				.toString();
		String json = new HttpUtil().create(url).setRequestMethod("POST").connect();
		return GsonUtil.toBean(json, WeiboAccessToken.class);
	}

	public static WeiboTokenInfo getOpenid(String access_token)
	{
		String url = new StringBuilder(API_TOKEN_INFO)
				.append("?access_token=").append(access_token)
				.toString();
		String json = new HttpUtil().create(url).setRequestMethod("POST").connect();
		return GsonUtil.toBean(json, WeiboTokenInfo.class);
	}

	public static WeiboUser getUser(String access_token, String openid)
	{
		String url = new StringBuilder(API_USER)
				.append("?access_token=").append(access_token)
				.append("&uid=").append(openid)
				.toString();
		String json = new HttpUtil().create(url).setRequestMethod("GET").connect();
		WeiboUser po = GsonUtil.toBean(json, WeiboUser.class);
		// po.getOpenid(openid);// 保存openid
		return po;
	}
}
