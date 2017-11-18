package com.github.lujinfeifly.freejava.http;

import com.github.lujinfeifly.freejava.bean.MNameValuePair;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MHttp {
    private static MHttp instance;

    public static MHttp getInstance() {
        if(instance == null) {
            instance = new MHttp();
        }
        return instance;
    }

    private int timeout = 1000;
    private String userAgent  = "freejava";
    private ConcurrentHashMap<String, String> cookieMap = new ConcurrentHashMap<String, String>(20);


    public HttpRet sendGetRequest(String url, List<MNameValuePair> param) {
        URL realUrl = null;
        try {
            realUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String urlKey = realUrl.getProtocol() +  "://"+ realUrl.getHost() +  ":" + realUrl.getPort();
        String sessionid = cookieMap.get(urlKey);

        System.out.println(urlKey + "*******" + sessionid);


        sessionid = (sessionid == null)?"":sessionid;

        //

        return MUrl.sendGetRequest(url, MUrl.paserList(param), sessionid, userAgent, timeout);

    }

    public static void main(String[] args) {
        HttpRet ret = getInstance().sendGetRequest("https://baidu.com", null);
        System.out.println(ret.getmSessionId());
    }

}
