package com.github.lujinfeifly.freejava.http;

import com.github.lujinfeifly.freejava.basic.MString;
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

        HttpRet ret = MUrl.sendGetRequest(url, MUrl.paserList(param), sessionid, userAgent, timeout);

        if(MString.isNotEmpty(ret.getmSessionId())) {
            cookieMap.put(urlKey, ret.getmSessionId());
        }

        return  ret;
    }

    public static void main(String[] args) {
        HttpRet ret = getInstance().sendGetRequest("https://www.baidu.com", null);
        System.out.println(ret.getmSessionId());




//        HttpRet ret2 = getInstance().sendPostRequest("https://dynamic.watch/users/sign_in", "utf8=%E2%9C%93&authenticity_token="+ java.net.URLEncoder.encode(bbb) +"&user%5Bemail%5D=736214397%40qq.com&user%5Bpassword%5D=lujinfei123.&user%5Bremember_me%5D=0&commit=Log+in",
//                ret1.getmSessionId());
//        System.out.println(ret2.toString());
//
//        HttpRet ret4 = sendGetRequest("http://dynamic.watch/me", "",
//                ret2.getmSessionId());
//        System.out.println(ret4.toString());
    }

}
