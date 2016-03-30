package deer.milu.freejava.http;

public class HttpRet {
	private int mRetCode;
	private String mRetContent;
	
	
	
	
	public int getmRetCode() {
		return mRetCode;
	}




	public String getmRetContent() {
		return mRetContent;
	}




	public HttpRet(int ret, String retContent) {
		this.mRetCode = ret;
		this.mRetContent = retContent;
	}
}
