package com.github.lujinfeifly.freejava.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import com.github.lujinfeifly.freejava.basic.MString;
import com.github.lujinfeifly.freejava.bean.MNameValuePair;

public class MUrl {
	
	public static final String BOUNDARYSTR = "----WebKitFormBoundaryDwwA7U9966TVkJIR";
    public static final String BOUNDARY = "--" + BOUNDARYSTR + "\r\n";
    
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
    	 
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

	public static String paserList(List<MNameValuePair> param) {
		if(param == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		int count = param.size();
		if(count >= 1) {
			sb.append(param.get(0).getName() + "=" + param.get(0).getValue());
		}
		for(int i=1;i < count; i++) {
			sb.append( "&" + param.get(i).getName() + "=" + param.get(i).getValue());
		}
		return sb.toString();
	}
	
	public static HttpRet sendGetRequest(String url, List<MNameValuePair> param, String sessionId,  String userAgent) {
		return sendGetRequest(url, paserList(param), sessionId, userAgent);
	}
	
	public static HttpRet sendPostRequest(String url, List<MNameValuePair> param, String sessionId, String userAgent) {
		return sendPostRequest(url, paserList(param), sessionId, userAgent, 2000);
	}
	
	public static HttpRet sendGetRequest(String url, String param, String sessionId) {
		return sendGetRequest(url, param, sessionId,
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10532");
	}


	public static HttpRet sendGetRequest(String url, String param, String sessionId, String userAgent) {
		return sendGetRequest(url, param, sessionId, userAgent, 2000);
	}

    public static HttpRet sendGetRequest(String url, String param, String sessionId, String userAgent, int timeout) {
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
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            
            if (conn instanceof HttpsURLConnection) { // 是Https请求
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
                if (sslContext != null) {
                    SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                    ((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
                }
            }

            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            // 设置通用的请求属性
            conn.setRequestProperty("Cache-Control", "max-age=0");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", userAgent);
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("Refer", "http://dynamic.watch/users/sign_in");
//            connection.setRequestProperty("Pragma", "no-cache");
            conn.setInstanceFollowRedirects(false);
//            connection.setRequestProperty("If-None-Match", cachedPage.eTag);
            if(MString.isNotEmpty(sessionId)) {
            	conn.setRequestProperty("Cookie", sessionId);
            }
            // 建立实际的连接
            conn.connect();
            // 获取所有响应头字段
            //Map<String, List<String>> map = connection.getHeaderFields();
            // 获取http编码

            int icode = conn.getResponseCode();

            // 
            String session_value=conn.getHeaderField("Set-Cookie");
            ret.setmSessionId(session_value);
            
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
            	strRet += line;
            }
            
            ret.setmRetCode(icode);
            if(icode == 302) {
            	ret.setmRetContent(conn.getHeaderField("Location"));
            }
            else if(icode < 400) {
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
    
    public static HttpRet sendPostRequest(String url, String param, String sessionId) {
    	return sendPostRequest(url, param, sessionId, 
    			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10532",
				2000);
    }

    public static HttpRet sendPostRequest(String url, String param, String sessionId, String userAgent, int timeout) {
    	HttpRet ret = new HttpRet();
        BufferedReader in = null;
        String result = "";
        try {        	
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            
            if (conn instanceof HttpsURLConnection) { // 是Https请求
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
                if (sslContext != null) {
                    SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                    ((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
                }
            }

            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setInstanceFollowRedirects(false);
            // 设置通用的请求属性
            conn.setRequestProperty("Cache-Control", "max-age=0");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", userAgent);
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
//            conn.setRequestProperty("Refer", "http://dynamic.watch/users/sign_in");
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
            int icode = conn.getResponseCode();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            ret.setmRetCode(icode);
            if(icode == 302) {
            	ret.setmRetContent(conn.getHeaderField("Location"));
            }
            else if(icode < 400) {
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
    
    public static HttpRet uploadFile(String url, List<MNameValuePair> param, File file, String sessionId) {
    	return uploadFile(url, param, file, sessionId, 
    			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10532");
    }
    
    public static HttpRet uploadFile(String url, List<MNameValuePair> param, File file, String sessionId, String userAgent) {
		HttpRet ret = new HttpRet();
		BufferedReader in = null;
		String result = "";
		try {
		    URL realUrl = new URL(url);
		    // 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
			
			if (conn instanceof HttpsURLConnection) { // 是Https请求
	            SSLContext sslContext = SSLContext.getDefault();
	            if (sslContext != null) {
	                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
	                ((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
	            }
	        }
			  // 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setInstanceFollowRedirects(false);
			  // 设置通用的请求属性
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("Accept", "application/json");
	//		conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
			conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", userAgent);
	//		conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
			conn.setRequestProperty("Refer", "http://dynamic.watch/routes/new");
			conn.setRequestProperty("Content-type", "multipart/form-data; boundary=" + BOUNDARYSTR);
			conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			if(MString.isNotEmpty(sessionId)) {
			  	conn.setRequestProperty("Cookie", sessionId);
			} else {
			  	//conn.setRequestProperty("Cookie", "_watchweb_session=BAh7CEkiD3Nlc3Npb25faWQGOgZFVEkiJWM0NWYyMjcxNjBjMmQwYTQ1MTBiYjU3YjZlYTExNjM4BjsAVEkiGXdhcmRlbi51c2VyLnVzZXIua2V5BjsAVFsHWwZpAlUmSSIiJDJhJDEwJE5FTWduRS9aY0JKYlo5U1l6bXBBbnUGOwBUSSIQX2NzcmZfdG9rZW4GOwBGSSIxeHd5eXAwMWRHdEhlaWpOdlozSWgwc2RaRnpiRjlnZ2h1WjBqMHFmT0FxYz0GOwBG--2eee7dccb321b2fed6291b7ba0d54d8441a64634;");
			}
			
			StringBuilder sb = new StringBuilder();
			
			Iterator<MNameValuePair> it = param.iterator();
			while(it.hasNext()) {
				MNameValuePair pair = it.next();
				sb.append(BOUNDARY);
				sb.append("Content-Disposition: form-data; name=\"");
				sb.append(pair.getName());
				sb.append("\"\r\n\r\n");
				sb.append(pair.getValue());
				sb.append("\r\n");
			}
			
			// 加入文件
			sb.append(BOUNDARY);
			sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"");
			sb.append(MString.getFileNameWithoutType(file.getName()));
			sb.append("\"\r\n");
			sb.append("Content-Type: application/octet-stream");
			sb.append("\r\n\r\n");
			  
			  // 获取URLConnection对象对应的输出流            
			OutputStream outputStream = conn.getOutputStream();   
			outputStream.write(sb.toString().getBytes()); 
			
			FileInputStream fis = new FileInputStream(file);
		    byte[] buffer = new byte[8192]; // 8k
		    int count = 0;
		    // 读取文件
		    while ((count = fis.read(buffer)) != -1) {
		    	outputStream.write(buffer, 0, count);
		    }
		    fis.close();
		    outputStream.write("\r\n\r\n".getBytes());
		    outputStream.write(("--" + BOUNDARYSTR + "--\r\n").getBytes());
			outputStream.close();
			  
			String session_value=conn.getHeaderField("Set-Cookie");
			ret.setmSessionId(session_value);
			  
			  // 获取所有响应头字段
			int icode = conn.getResponseCode();
		
		  // 定义BufferedReader输入流来读取URL的响应
		  in = new BufferedReader(
		          new InputStreamReader(conn.getInputStream()));
		   String line;
		   while ((line = in.readLine()) != null) {
		       result += line;
		   }
		   ret.setmRetCode(icode);
		   if(icode < 400) {
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
    
    private static void trustAllHosts() {
        final String TAG = "trustAllHosts";
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
     
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }
     
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
     
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        } };
     
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
	static class myX509TrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	static class myHostnameVerifier implements HostnameVerifier {

		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
    
    private static class MyTrustManager implements X509TrustManager{  
    	  
        public void checkClientTrusted(X509Certificate[] chain, String authType)  
                        throws CertificateException {  
                // TODO Auto-generated method stub  
                  
        }  

        public void checkServerTrusted(X509Certificate[] chain, String authType)  
                        throws CertificateException {  
                // TODO Auto-generated method stub  
                  
        }  

        public X509Certificate[] getAcceptedIssuers() {  
                // TODO Auto-generated method stub  
                return null;  
        }          
    } 
     
}
