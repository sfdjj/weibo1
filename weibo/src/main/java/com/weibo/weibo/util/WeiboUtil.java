package com.weibo.weibo.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.util.Map;

/**
 * Created by jwc on 2017/7/22.
 */
public class WeiboUtil {

    private static String [] MAIL = {"qq.com","163.com","126.com","yeah.com"};
    private static String [] IMAGE_TYPE = {"png", "bmp", "jpg", "jpeg"};
    public static String QINIU_IMAGE_DOMAIN = "http://os37jww30.bkt.clouddn.com/";
    public static String IMAGE_DIR = "D:/upload/";
    public static int ENTITY_TYPE_WEIBO = 0;
    public static int ENTITY_TYPE_COMMENT = 1;
    public static int ENTITY_TYPE_USER = 3;
    public static int DEFAULT_OFFSET = 0;
    public static int DEFAULT_LIMIT = 10;
    public static int DEFAULT_SYSTEM_ID = 1;

    public static void UpdateCookieTicket(HttpServletResponse response, String ticket, int maxAge) {
        Cookie cookie = new Cookie("ticket", ticket);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static String getCookieTicket(HttpServletRequest httpServletRequest) {
        String ticket = null;
        if(httpServletRequest.getCookies()==null) {
            return null;
        }
        for(Cookie cookie:httpServletRequest.getCookies()) {
            if(cookie.getName().equals("ticket")) {
                ticket = cookie.getValue();
                break;
            }
        }
        return ticket;
    }

    public static boolean isMail(String postfix) {
        for(String s:MAIL) {
            if(postfix.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isImage(String postfix) {
        for(String s:IMAGE_TYPE) {
            if(postfix.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    public static String getJSONString(int code) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        return jsonObject.toJSONString();
    }

    public static String getJSONString(int code,String s) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",s);
        return jsonObject.toJSONString();
    }

    public static String getJSONString(int code,Map<String,Object> map) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        for(Map.Entry<String,Object> entity:map.entrySet()) {
            jsonObject.put(entity.getKey(),entity.getValue());
        }
        return jsonObject.toJSONString();
    }

    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
