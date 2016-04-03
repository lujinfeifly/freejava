package deer.milu.freejava.http;

public class HttpRet {
	private int mRetCode;
	private String mRetContent;
	private String mSessionId;
	
	
	
	
	public String getmSessionId() {
		return mSessionId;
	}




	public void setmSessionId(String mSessionId) {
		this.mSessionId = mSessionId;
	}




	public void setmRetCode(int mRetCode) {
		this.mRetCode = mRetCode;
	}




	public void setmRetContent(String mRetContent) {
		this.mRetContent = mRetContent;
	}




	public int getmRetCode() {
		return mRetCode;
	}




	public String getmRetContent() {
		return mRetContent;
	}


	public HttpRet() {
		this.mRetCode = -1;
		this.mRetContent = "";
	}

	public HttpRet(int ret, String retContent) {
		this.mRetCode = ret;
		this.mRetContent = retContent;
	}
	
	public String toString() {
		return "(RetCode:" + mRetCode + ",RetContent:" + mRetContent + ")";
	}
}
