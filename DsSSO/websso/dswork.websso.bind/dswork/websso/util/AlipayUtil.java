package dswork.websso.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dswork.core.util.EncryptUtil;
import dswork.core.util.EnvironmentUtil;
import dswork.core.util.TimeUtil;
import dswork.http.HttpUtil;
import dswork.http.NameValue;
import dswork.websso.model.alipay.AlipayAccessToken;
import dswork.websso.model.alipay.AlipayResponse;
import dswork.websso.model.alipay.AlipayUserinfo;

public class AlipayUtil
{
	public static final String APPID = EnvironmentUtil.getToString("websso.alipay.appid", "");
	public static final String KEYPRIVATE = EnvironmentUtil.getToString("websso.alipay.keyprivate", "");
	public static final String KEYPUBLIC = EnvironmentUtil.getToString("websso.alipay.keypublic", "");
	public static final String RETURNURL = EnvironmentUtil.getToString("websso.alipay.returnurl", "");

	public static final String API_GATEWAY = "https://openapi.alipay.com/gateway.do";

	private AlipayUtil()
	{
	}

	public static AlipayAccessToken getAccessToken(String auth_code)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("app_id", APPID);
		map.put("method", "alipay.system.oauth.token");
		map.put("charset", "GBK");
		map.put("sign_type", "RSA2");
		map.put("timestamp", TimeUtil.getCurrentTime());
		map.put("version", "1.0");
		map.put("grant_type", "authorization_code");
		map.put("code", auth_code);
		map.put("sign", sign(map));
		String json = new HttpUtil().create(API_GATEWAY)
				.addForms(getNameValues(map))
				.connect("GBK");
		AlipayResponse resp = GsonUtil.toBean(json, AlipayResponse.class);
		return resp.getAccesstoken();
	}

	public static AlipayUserinfo getUserInfo(String access_token)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("app_id", APPID);
		map.put("method", "alipay.user.info.share");
		map.put("charset", "GBK");
		map.put("sign_type", "RSA2");
		map.put("timestamp", TimeUtil.getCurrentTime());
		map.put("version", "1.0");
		map.put("auth_token", access_token);
		map.put("sign", sign(map));
		String json = new HttpUtil().create(API_GATEWAY)
				.addForms(getNameValues(map))
				.connect("GBK");
		AlipayResponse resp = GsonUtil.toBean(json, AlipayResponse.class);
		return resp.getUserinfo();
	}

	private static NameValue[] getNameValues(Map<String, String> map)
	{
		NameValue[] arr = new NameValue[map.size()];
		List<String> keys = new ArrayList<String>(map.keySet());
		for(int i = 0; i < keys.size(); i++)
		{
			String k = keys.get(i);
			String v = map.get(k);
			arr[i] = new NameValue(k, v);
		}
		return arr;
	}

	private static String sign(Map<String, String> map)
	{
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
		String k = keys.get(0);
		String v = map.get(k);
		StringBuffer sb = new StringBuffer(k + "=" + v);
		for(int i = 1; i < keys.size(); i++)
		{
			k = keys.get(i);
			v = map.get(k);
			sb.append("&" + k + "=" + v);
		}
		return EncryptUtil.encryptRsa2(sb.toString(), KEYPRIVATE);
	}
}
