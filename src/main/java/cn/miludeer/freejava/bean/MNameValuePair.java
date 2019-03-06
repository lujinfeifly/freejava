package cn.miludeer.freejava.bean;

public class MNameValuePair {
	private String name;
	private String value;
	public String getName() {
		return name;
	}
	
	public MNameValuePair(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return "(name:" + name +",value:" + value+")";
	}
}
