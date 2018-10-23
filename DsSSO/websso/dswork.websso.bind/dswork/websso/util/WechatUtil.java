package dswork.websso.util;

import dswork.core.util.EnvironmentUtil;
import dswork.http.HttpUtil;
import dswork.websso.model.wechat.WechatAccessToken;
import dswork.websso.model.wechat.WechatUserinfo;

public class WechatUtil
{
	public static final String API_ACCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
	public static final String API_USERINFO = "https://api.weixin.qq.com/sns/userinfo";

	public static final String APPID = EnvironmentUtil.getToString("websso.wechat.appid", "");
	public static final String APPKEY = EnvironmentUtil.getToString("websso.wechat.appkey", "");
	public static final String RETURNURL = EnvironmentUtil.getToString("websso.wechat.returnurl", "");

	public static WechatAccessToken getAccessToken(String auth_code)
	{
		StringBuilder sb = new StringBuilder().append(API_ACCESSTOKEN)
				.append("?appid=").append(APPID)
				.append("&secret=").append(APPKEY)
				.append("&code=").append(auth_code)
				.append("&grant_type=authorization_code");
		String json = new HttpUtil().create(sb.toString())
				.connect();
		return GsonUtil.toBean(json, WechatAccessToken.class);
	}

	public static WechatUserinfo getUserInfo(String accesstoken, String openId)
	{
		StringBuilder sb = new StringBuilder().append(API_USERINFO)
				.append("?access_token=").append(accesstoken)
				.append("&openid=").append(openId);
		String json = new HttpUtil().create(sb.toString())
				.connect();
		return GsonUtil.toBean(json, WechatUserinfo.class);
	}
}
