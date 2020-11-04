package com.thdz.ywqx.util;

/**
 * url 拼接工具类
 */

public class UrlUtils {

    /**
     * 根据获取到的图片路径计算图片完整url
     */
    public static String createUrlPath(String ip, String url){
        String value = Finals.Url_Http + ip + url;
        return value;
    }

}
