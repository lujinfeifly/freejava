package cn.miludeer.freejava.basic;

import java.util.Random;

/**
 * 字符串处理
 * Created by lu on 16/3/27.
 */
public class MString {

    /**将二进制转换成16进制样式字符串
     * @param buf 输入的数组
     * @return 返回的字符串
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**将16进制转换为二进制byte[] 数组
     * @param hexStr 输入的16进只字符串
     * @return 返回的数组
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
    
    /**
	 * 是否不为空
	 *
	 * @param s 字符串
	 * @return 返回是否不为空
	 */
	public static boolean isNotEmpty(String s) {
		return s != null && !"".equals(s.trim());
	}

	/**
	 * 是否为空
	 *
	 * @param s 字符串
	 * @return 返回是否为空
	 */
	public static boolean isEmpty(String s) {
		return s == null || "".equals(s.trim());
	}
	
	/**
	 * 通过{n},格式化.
	 *
	 * @param src 字符串
	 * @param objects 参数
	 * @return 返回的字符串
	 */
	public static String format(String src, Object... objects) {
		int k = 0;
		for (Object obj : objects) {
			src = src.replace("{" + k + "}", obj.toString());
			k++;
		}
		return src;
	}
	
	public static String getFileNameWithoutType(String filename) {
		int index = filename.lastIndexOf(".");
		if(index<0) {
			return filename.substring(0, index);
		}
		return filename;
	}
	
	public static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	} 
}
