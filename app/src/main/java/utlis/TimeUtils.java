package utlis;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

	public TimeUtils() {
	}

	public static String getTime(int mode) {

		Date now = new Date(System.currentTimeMillis());

		switch (mode) {
		case 0: {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			return df.format(now);
		}
		case 1: {
			DateFormat df = new SimpleDateFormat("yyMMdd");
			return df.format(now);
		}
		case 2: {
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			return df.format(now);
		}
		case 3: {
			DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
			return df.format(now);
		}
		case 4:
		case 5: {
			DateFormat df = new SimpleDateFormat("HH:mm:ss");
			return df.format(now);
		}
		case 6: {
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
			return df.format(now);
		}
		case 8: {
			DateFormat df = new SimpleDateFormat("HHmmss");
			return df.format(now);
		}

		default: {
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			return df.format(now);
		}
		}
	}

	public static String SpliterDeal(String orgin_data) {
		return orgin_data.replaceAll("\\|\\|", "\\| \\|").replaceAll("\\|\\|",
				"\\| \\|");
	}

	public static String FormatHasGetDate(String date) {
		DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		Date date2 = null;
		try {
			date2 = df1.parse(date);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return df2.format(date2);
	}

	public static String FormatHasGetDate2(String date) {
		DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		Date date2 = null;
		try {
			date2 = df2.parse(date);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return df1.format(date2);
	}

	public static String formatString(String src, String sign, int length,
                                      boolean isRight) {
		if (src.getBytes().length > length) {
			return src;
		}
		if (isRight) {
			while (src.getBytes().length < length) {
				src = src + sign;
			}
		} else {
			while (src.getBytes().length < length) {
				src = sign + src;
			}
		}
		return src;
	}

	/**
	 * 获取时间间隔
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static String DateSpace(String date1, String date2){
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date d1 = (Date)sdf.parse(date1);
			Date d2 = (Date)sdf.parse(date2);
			long diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
			long days = diff / (1000 * 60 * 60 * 24);
			return String.valueOf(days);
		}
		catch(Exception ex)
		{
			return "";
		}
	}

	public static void main(String args[]) {
		System.out.println(FormatHasGetDate2("2012-01-09"));
	}
}
