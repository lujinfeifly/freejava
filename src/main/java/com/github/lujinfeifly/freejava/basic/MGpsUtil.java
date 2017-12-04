package com.github.lujinfeifly.freejava.basic;

import java.util.HashMap;
import java.util.Map;

public class MGpsUtil {

	private static double EARTH_RADIUS = 6378.137;   //地球半径
	private static char[] base32 = { '0', '1', '2', '3', '4', '5', '6', '7'};
	private final static Map<Character, Integer> decodemap = new HashMap<Character, Integer>();
	static {
		int sz = base32.length;
		for (int i = 0; i < sz; i++) {
			decodemap.put(base32[i], i);
		}
	}
	private static int precision = 14;
	private static int[] bits = {4, 2, 1 };

	/**
	 * 设置精度
	 * @param precision 设置精确位数，此参数决定了该算法的经度
	 *
	 * */
	public static void setPrecision(int precision) {
		MGpsUtil.precision = precision;
	}

	public static double getPrecision(double x, double precision) {
		double base = Math.pow(10, -precision);
		double diff = x % base;
		return x - diff;
	}
	

	public static String encode(double latitude, double longitude) {
		double[] lat_interval = { -90.0, 90.0 };
		double[] lon_interval = { -180.0, 180.0 };
		StringBuilder geohash = new StringBuilder();
		boolean is_even = true;
		int bit = 0, ch = 0;
		while (geohash.length() < precision) {
			double mid = 0.0;
			if (is_even) {
				mid = (lon_interval[0] + lon_interval[1]) / 2;
				if (longitude > mid) {
					ch |= bits[bit];
					lon_interval[0] = mid;
				} else {
					lon_interval[1] = mid;
				}
			} else {
				mid = (lat_interval[0] + lat_interval[1]) / 2;
				if (latitude > mid) {
					ch |= bits[bit];
					lat_interval[0] = mid;
				} else {
					lat_interval[1] = mid;
				}
			}
			is_even = is_even ? false : true;

			if (bit < 2) {
				bit++;
			} else {
				geohash.append(base32[ch]);
				bit = 0;
				ch = 0;
			}
		}
		return geohash.toString();
	}

	public static double[] decode(String geohash) {
		double[] ge = decode_exactly(geohash);
		double lat, lon, lat_err, lon_err;
		lat = ge[0];
		lon = ge[1];
		lat_err = ge[2];
		lon_err = ge[3];
		double lat_precision = Math.max(1, Math.round(-Math.log10(lat_err))) - 1;
		double lon_precision = Math.max(1, Math.round(-Math.log10(lon_err))) - 1;
		lat = getPrecision(lat, lat_precision);
		lon = getPrecision(lon, lon_precision);
		return new double[] { lat, lon };
	}

	public static double[] decode_exactly(String geohash) {
		double[] lat_interval = { -90.0, 90.0 };
		double[] lon_interval = { -180.0, 180.0 };
		double lat_err = 90.0;
		double lon_err = 180.0;
		boolean is_even = true;
		int sz = geohash.length();
		int bsz = bits.length;
		double latitude, longitude;
		for (int i = 0; i < sz; i++) {
			int cd = decodemap.get(geohash.charAt(i));
			for (int z = 0; z < bsz; z++) {
				int mask = bits[z];
				if (is_even) {
					lon_err /= 2;
					if ((cd & mask) != 0) {
						lon_interval[0] = (lon_interval[0] + lon_interval[1]) / 2;
					} else {
						lon_interval[1] = (lon_interval[0] + lon_interval[1]) / 2;
					}
				} else {
					lat_err /= 2;

					if ((cd & mask) != 0) {
						lat_interval[0] = (lat_interval[0] + lat_interval[1]) / 2;
					} else {
						lat_interval[1] = (lat_interval[0] + lat_interval[1]) / 2;
					}
				}
				is_even = is_even ? false : true;
			}
		}
		latitude = (lat_interval[0] + lat_interval[1]) / 2;
		longitude = (lon_interval[0] + lon_interval[1]) / 2;
		return new double[] { latitude, longitude, lat_err, lon_err };
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	} 
	
	public static double distanceFromGps(String gs, double lng, double lat) {
		MGpsUtil ghf = new MGpsUtil();
		double[] gd1 = ghf.decode(gs);
		
		double radLat1 = rad(lat); 
		double radLat2 = rad(gd1[0]); 
 
		double radLon1 = rad(lng); 
		double radLon2 = rad(gd1[1]);
	
		if (radLat1 < 0) 
			radLat1 = Math.PI / 2 + Math.abs(radLat1);// south  
		if (radLat1 > 0) 
			radLat1 = Math.PI / 2 - Math.abs(radLat1);// north  
		if (radLon1 < 0) 
			radLon1 = Math.PI * 2 - Math.abs(radLon1);// west  
		if (radLat2 < 0)	
			radLat2 = Math.PI / 2 + Math.abs(radLat2);// south  
		if (radLat2 > 0) 
			radLat2 = Math.PI / 2 - Math.abs(radLat2);// north  
		if (radLon2 < 0) 
			radLon2 = Math.PI * 2 - Math.abs(radLon2);// west  
		double x1 = EARTH_RADIUS * Math.cos(radLon1) * Math.sin(radLat1); 
		double y1 = EARTH_RADIUS * Math.sin(radLon1) * Math.sin(radLat1); 
		double z1 = EARTH_RADIUS * Math.cos(radLat1); 
		double x2 = EARTH_RADIUS * Math.cos(radLon2) * Math.sin(radLat2); 
		double y2 = EARTH_RADIUS * Math.sin(radLon2) * Math.sin(radLat2); 
		double z2 = EARTH_RADIUS * Math.cos(radLat2); 

		double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)+ (z1 - z2) * (z1 - z2)); 
		//余弦定理求夹角  
		double theta = Math.acos((EARTH_RADIUS * EARTH_RADIUS + EARTH_RADIUS * EARTH_RADIUS - d * d) / (2 * EARTH_RADIUS * EARTH_RADIUS)); 
		double dist = theta * EARTH_RADIUS; 
		return dist; 
	}
	
	public static void main(String[] args) {
		MGpsUtil ghf = new MGpsUtil();
		String gc1 = ghf.encode(39.915808,116.212389);
		String gc5 = ghf.encode(39.913328,116.211374);
		
		String gc2 = ghf.encode(39.914044,116.280481);
		String gc3 = ghf.encode(39.914044,116.404662);
		String gc4 = ghf.encode(39.914044,116.551122);
		String gc6 = ghf.encode(36.749136,117.652937);

		System.out.println("融科：    "+ gc1);
		System.out.println("京燕：    " + gc5);
		System.out.println("五棵松："+gc2);
		System.out.println("天安门："+gc3);
		System.out.println("东四环："+gc4);
		System.out.println("济南哈："+gc6);
		
		double a = distanceFromGps("71644330065702", 116.211374, 39.913328);

		double[] gd1 = ghf.decode("71644330065702");
		double[] gd2 = ghf.decode(gc2);
		System.out.println(gd1[0] + ", " + gd1[1]);
		System.out.println(gd2[0] + ", " + gd2[1]);
		System.out.println(a);
	}

}
