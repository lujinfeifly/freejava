package deer.milu.freejava.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import deer.milu.freejava.basic.MString;
import deer.milu.freejava.bean.MNameValuePair;

public class MUrl {
	
	public static String ssss = "BAIDUID=F3E78CE7C4AC856CA2CE0218063E35FC:FG=1; expires=Sat, 01-Apr-17 03:30:41 GMT; max-age=31536000; path=/; domain=.baidu.com; version=1";
	
	public static String paserList(List<MNameValuePair> param) {
		StringBuilder sb = new StringBuilder();
		int count = param.size();
		if(count >= 1) {
			sb.append(param.get(0).getName() + "=" + param.get(0).getValue());
		}
		for(int i=1;i < count; i++) {
			sb.append( "&" + param.get(0).getName() + "=" + param.get(0).getValue());
		}
		return sb.toString();
	}
	
	public static HttpRet sendGetRequest(String url, List<MNameValuePair> param) {
		return sendGetRequest(url, paserList(param),"");
	}
	
	public static HttpRet sendPostRequest(String url, List<MNameValuePair> param) {
		return sendPostRequest(url, paserList(param),"");
	}
	
	/**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static HttpRet sendGetRequest(String url, String param, String sessionId) {
        HttpRet ret = new HttpRet();
        String strRet = "";
        BufferedReader in = null;
        try {
        	String urlNameString ;
        	if(MString.isNotEmpty(param)) {
        		urlNameString = url + "?" + param;
        	} else {
        		urlNameString = url;
        	}
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection)realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("Cache-Control", "max-age=0");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10532");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            connection.setRequestProperty("Refer", "http://dynamic.watch/users/sign_in");
//            connection.setRequestProperty("Pragma", "no-cache");
            connection.setInstanceFollowRedirects(false);
//            connection.setRequestProperty("If-None-Match", cachedPage.eTag);
            if(MString.isNotEmpty(sessionId)) {
            	connection.setRequestProperty("Cookie", sessionId);
            }
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            //Map<String, List<String>> map = connection.getHeaderFields();
            // 获取http编码
            String httpStatus = connection.getHeaderField(0);
            String[] codes = httpStatus.split(" ");
            int icode = 200;
            if(codes.length > 2) {
            	String code = codes[1];
            	try{
            		icode = Integer.parseInt(code);
            	}catch(NumberFormatException ex) {
            		icode = 420;
            	}
            }
            // 
            String session_value=connection.getHeaderField("Set-Cookie");
            ret.setmSessionId(session_value);
            
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
            	strRet += line;
            }
            
            ret.setmRetCode(icode);
            if(icode < 300) {
                ret.setmRetContent(strRet);
            } 
            
        } catch (MalformedURLException me) {
        	ret.setmRetCode(400);
        	me.printStackTrace();
        } catch (IOException ie) {
        	ret.setmRetCode(510);
        	ie.printStackTrace();
        } catch (Exception e) {
        	ret.setmRetCode(511);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e2) {
            	ret.setmRetCode(512);
                e2.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static HttpRet sendPostRequest(String url, String param, String sessionId) {
    	HttpRet ret = new HttpRet();
//        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setInstanceFollowRedirects(false);
            // 设置通用的请求属性
            conn.setRequestProperty("Cache-Control", "max-age=0");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10532");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("Refer", "http://dynamic.watch/users/sign_in");
            if(MString.isNotEmpty(sessionId)) {
            	conn.setRequestProperty("Cookie", sessionId);
            }
            
            
            
            // 获取URLConnection对象对应的输出流            
            OutputStream outputStream = conn.getOutputStream();   
            outputStream.write(param.getBytes()); 
            outputStream.close();   
            
            String session_value=conn.getHeaderField("Set-Cookie");
            ret.setmSessionId(session_value);
            
            // 获取所有响应头字段
            String httpStatus = conn.getHeaderField(0);
            String[] codes = httpStatus.split(" ");
            int icode = 200;
            if(codes.length > 2) {
            	String code = codes[1];
            	try{
            		icode = Integer.parseInt(code);
            	}catch(NumberFormatException ex) {
            		icode = 420;
            	}
            }
            

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            ret.setmRetCode(icode);
            if(icode < 300) {
                ret.setmRetContent(result);
            } 
        } catch (MalformedURLException me) {
        	ret.setmRetCode(400);
        	me.printStackTrace();
        } catch (IOException ie) {
        	ret.setmRetCode(510);
        	ie.printStackTrace();
        } catch (Exception e) {
        	ret.setmRetCode(511);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            
        }
        return ret;
    } 
    
    public static void main(String[] argv) {
//    	HttpRet ret = sendGetRequest("http://fljzwg.changyou.com/syntime.aspx","","");
//    	System.out.println(ret);
    	HttpRet ret2 = sendPostRequest("http://dynamic.watch/users/sign_in", "utf8=%E2%9C%93&authenticity_token=F8sEgcyKujVJhooiHr1G%2FeubbDBX7bOy9MGJGLuWKJw%3D&user%5Bemail%5D=736214397%40qq.com&user%5Bpassword%5D=LuJinfei123&user%5Bremember_me%5D=0&commit=Log+in",
    			"_ga=GA1.2.1566513836.1459674735; _watchweb_session=BAh7CEkiD3Nlc3Npb25faWQGOgZFVEkiJWM0NWYyMjcxNjBjMmQwYTQ1MTBiYjU3YjZlYTExNjM4BjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAlUmSSIiJDJhJDEwJE5FTWduRS9aY0JKYlo5U1l6bXBBbnUGOwBUSSIQX2NzcmZfdG9rZW4GOwBGSSIxeHd5eXAwMWRHdEhlaWpOdlozSWgwc2RaRnpiRjlnZ2h1WjBqMHFmT0FxYz0GOwBG--2eee7dccb321b2fed6291b7ba0d54d8441a64634; _gat=1");
    	System.out.println(ret2.toString());
    	
    	HttpRet ret = sendGetRequest("http://dynamic.watch/", "",
    			ret2.getmSessionId());
    	System.out.println(ret.toString());
//    	
    	HttpRet ret3 = sendGetRequest("http://dynamic.watch/me", "",
    			ret.getmSessionId());
    	System.out.println(ret3.toString());

    	
    }
}
