package deer.milu.freejava.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import deer.milu.freejava.bean.MNameValuePair;

public class MUrl {
	
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
		return sendGetRequest(url, paserList(param));
	}
	
	public static HttpRet sendPostRequest(String url, List<MNameValuePair> param) {
		return sendGetRequest(url, paserList(param));
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
    public static HttpRet sendGetRequest(String url, String param) {
        HttpRet ret = new HttpRet();
        String strRet = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "freejava-0.0.2");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            //Map<String, List<String>> map = connection.getHeaderFields();
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
    public static HttpRet sendPostRequest(String url, String param) {
    	HttpRet ret = new HttpRet();
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "freejava-0.0.2");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            
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
            
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
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
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
            	ret.setmRetCode(512);
                ex.printStackTrace();
            }
        }
        return ret;
    } 
}
