package deer.milu.freejava.basic;

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
}
