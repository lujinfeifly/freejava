package com.github.lujinfeifly.freejava.http;

import com.github.lujinfeifly.freejava.basic.MString;
import com.github.lujinfeifly.freejava.bean.MNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

    private String[] fetchSessionId(String url) {
        URL realUrl = null;
        try {
            realUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        int port = (realUrl.getPort() == -1)?realUrl.getDefaultPort():realUrl.getPort();

        String urlKey = realUrl.getProtocol() +  "://"+ realUrl.getHost() +  ":" + port;
        String sessionid = cookieMap.get(urlKey);

//        System.out.println(urlKey + "*******" + sessionid);

        sessionid = (sessionid == null)?"":sessionid;
        return new String[]{urlKey,sessionid};
    }


    /**
     * Get的pair的list参数请求
     * @param url 请求的url
     * @param param 请求的参数
     * @return
     */
    public HttpRet sendGetRequest(String url, List<MNameValuePair> param) {
        return sendGetRequest(url, MUrl.paserList(param));
    }

    /**
     * Get的String参数请求
     * @param url 请求的url
     * @param param 请求的参数
     * @return
     */
    public HttpRet sendGetRequest(String url, String param) {
        String[] sessionid = fetchSessionId(url);

        // 这里进行请求
        HttpRet ret = MUrl.sendGetRequest(url, param, sessionid[1], userAgent, timeout);

        if(MString.isNotEmpty(ret.getmSessionId())) {
            cookieMap.put(sessionid[0], ret.getmSessionId());
        }

        return  ret;
    }

    /**
     * poat的pair参数请求
     * @param url 请求的url
     * @param param 请求的参数
     * @return
     */
    public HttpRet sendPostRequest(String url, List<MNameValuePair> param) {
        return sendPostRequest(url, MUrl.paserList(param));
    }

    /**
     * post的String 参数请求
     * @param url
     * @param param
     * @return
     */
    public HttpRet sendPostRequest(String url, String param) {
        String[] sessionid = fetchSessionId(url);

        // 这里进行请求
        HttpRet ret = MUrl.sendPostRequest(url, param, sessionid[1], userAgent, timeout);

        if(MString.isNotEmpty(ret.getmSessionId())) {
            cookieMap.put(sessionid[0], ret.getmSessionId());
        }

        return  ret;

    }

    /**
     * 单文件上传
     * @param url 上传的url
     * @param param 上传的参数，不是文件
     * @param filePath 文件的路径
     * @return 返回结果
     */
    public HttpRet uploadFile(String url, List<MNameValuePair> param, String filePath) {
        String[] sessionid = fetchSessionId(url);

        // 这里进行请求
        HttpRet ret = MUrl.sendPostRequest(url, param, filePath, sessionid[1]);

        if(MString.isNotEmpty(ret.getmSessionId())) {
            cookieMap.put(sessionid[0], ret.getmSessionId());
        }

        return ret;
    }

    public static void main(String[] args) {
        getInstance().timeout = 5000;
//        HttpRet ret = getInstance().sendGetRequest("http://www.baidu.com", null);
//        System.out.println(ret.getmSessionId());


        HttpRet ret1 = getInstance().sendGetRequest("https://dynamic.watch/users/sign_in", "");
        System.out.println(ret1.toString());
        String bbb ="";
        if(ret1.getmRetCode() == 200) {
            Document doc = Jsoup.parse(ret1.getmRetContent());
            Elements contents = doc.getElementsByAttributeValue("name", "authenticity_token");
            Element content = contents.get(0);
            bbb = content.attr("value");
            System.out.println(bbb);
        }

        HttpRet ret2 = getInstance().sendPostRequest("https://dynamic.watch/users/sign_in", "utf8=%E2%9C%93&authenticity_token="+ java.net.URLEncoder.encode(bbb) +"&user%5Bemail%5D=736214397%40qq.com&user%5Bpassword%5D=lujinfei123.&user%5Bremember_me%5D=0&commit=Log+in");
        System.out.println(ret2.toString());

        HttpRet ret4 = getInstance().sendGetRequest("http://dynamic.watch/me", "");
        System.out.println(ret4.toString());

    }

}
