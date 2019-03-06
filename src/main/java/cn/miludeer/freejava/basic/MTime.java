package cn.miludeer.freejava.basic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间处理
 * Created by lu on 16/3/27.
 */
public class MTime {
    private String commonFormat = "yyyy-MM-dd HH:mm:ss";
    private Date time;

    /**
     *
     * @param itime 时间的long值
     */
    public MTime(long itime) {
        this.time = new Date(itime);
    }

    /**
     *
     * @param date 时间的Date值
     */
    public MTime(Date date) {
        this.time = date;
    }

    /**
     *
     * @param strTime 时间字符串
     * @param format 时间格式
     */
    public MTime(String strTime, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(strTime);
        } catch (ParseException e) {
            date = new Date(0);
        }
        this.time = date;
    }



    public long getTime() {
        return this.time.getTime();
    }

    public String getCommonTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(commonFormat);
        return sdf.format(this.time);
    }

    public String getSpecialTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(this.time);
    }

    public long timeDiffNow() {
        return this.time.getTime() - new Date().getTime() / 1000;
    }
    
    /**
	 * 获取强用户可读性时间：
	 *    1.今天的返回：时候加小时分钟
	 *    2.昨天的返回："昨天"加时候加小时分钟
	 *    3.今年的返回：月日加时候小时分钟
	 *    4.去年的返回：年份加时候小时分钟
	 * @param time 日期值
	 * @return 返回的时间表示字符串
	 */
	public static String getHumenTime(long time) {
		SimpleDateFormat HourFormat = new SimpleDateFormat("HH:mm");
		SimpleDateFormat  dayFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
		long now = System.currentTimeMillis();
		// 判断凌晨、上午、下午、晚上
		String hour = HourFormat.format(time);
		String hours[] = hour.split(":");
		String timedup = "";
		if (hours.length == 2) {
			int dhour = Integer.parseInt(hours[0]);
			int dmini = Integer.parseInt(hours[1]);
			timedup = getTimeDup(dhour, dmini);
		}

		// 是今年，不是今天的，返回MM-dd HH:mm
		String nowDay = dayFormat.format(now);
		String yesterday = dayFormat.format(now - 86400000);
		String day = dayFormat.format(time);
		if (nowDay.equals(day)) { // 今天
			return new SimpleDateFormat(timedup + "HH:mm").format(time);
		} else if (nowDay.equals(yesterday)) {
			return new SimpleDateFormat("昨天 " + timedup + "HH:mm").format(time);
		} else {
			// 不是今年的，返回yyyy-MM-dd HH:mm
			String nowYear = yearFormat.format(now);
			String year = yearFormat.format(time);
			if (!nowYear.equals(year)) {
				return new SimpleDateFormat("yyyy-MM-dd " + timedup + "HH:mm")
						.format(time);
			}
			return new SimpleDateFormat("MM-dd " + timedup + "HH:mm")
					.format(time);
		}
	}
	
	/**
	 * 获取当天的时候（凌晨，上午，中午，下午，晚上）
	 * @param hour 小时 24时制
	 * @param mini 分钟
	 * @return 返回的时候（凌晨，上午，中午，下午，晚上）
	 */
	public static String getTimeDup(int hour, int mini) {
		if (hour >= 0 && hour < 7) {
			return "凌晨";
		} else if (hour >= 7 && hour < 12) {
			return "上午";
		} else if (hour >= 12 && hour < 13) {
			return "中午";
		} else if (hour >= 13 && hour < 19) {
			return "下午";
		} else if (hour >= 19 && hour < 24) {
			return "晚上";
		} else {
			return "";
		}
	}
}
