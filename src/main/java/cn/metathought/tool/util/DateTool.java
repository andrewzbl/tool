package cn.metathought.tool.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @Description:日期工具类
 * @author zbl
 *
 * @date 2015.03.09
 */
@Slf4j
public class DateTool {
	public static final String yyyyMMddHHmmss1 = "yyyy-MM-dd HH:mm:ss";// 精确到秒
	public static final String yyyyMMddHHmm1 = "yyyy-MM-dd HH:mm";// 精确到分
	public static final String yyyyMMddHH1 = "yyyy-MM-dd HH";// 精确到时
	public static final String yyyyMMdd1 = "yyyy-MM-dd";// 精确到日
	public static final String yyyyMM1 = "yyyy-MM";// 精确到月
	public static final String yyyy = "yyyy";// 精确到年

	public static final String yyyyMMddHHmmsss = "yyyyMMddHHmmssS";// 精确到毫秒
	public static final String yyyyMMddHHmmss2 = "yyyyMMddHHmmss";// 精确到秒
	public static final String yyyyMMddHHmm2 = "yyyyMMddHHmm";// 精确到分
	public static final String yyyyMMddHH2 = "yyyyMMddHH";// 精确到时
	public static final String yyyyMMdd2 = "yyyyMMdd";// 精确到日

	/**
	 * 根据自定义格式返回时间字符串
	 * 
	 * @param date
	 *            要格式化的时间
	 * @param datePattern
	 *            格式化类型
	 * @return 格式化出错时返回null
	 */
	public static String formatDateToString(Date date, String datePattern) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(datePattern);
			return format.format(date);
		} catch (Exception e) {
			log.error("custom format time string error", e);
			return null;
		}
	}

	/**
	 * 根据自定义格式返回时间Date
	 * 
	 * @param dateString
	 *            要格式化的时间字符串
	 * @param datePattern
	 *            格式化类型
	 * @return 格式化出错时返回null
	 */
	public static Date formatStringToDate(String dateString, String datePattern) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(datePattern);
			return format.parse(dateString);
		} catch (Exception e) {
			log.error("custom format time date error", e);
			return null;
		}
	}

	/**
	 * 返回当前日期字符串,类型格式为"yyyy-MM-dd HH:mm:ss"的字符串 精确到秒
	 * 
	 * @return 格式化出错时返回null
	 */
	public static String getNow_Time() {
		return formatDateToString(new Date(), yyyyMMddHHmmss1);
	}

	/**
	 * 返回当前日期字符串,类型格式为"yyyy-MM-dd HH:mm"的字符串 精确到分
	 * 
	 * @return 格式化出错时返回null
	 */
	public static String getNow_Minute() {
		return formatDateToString(new Date(), yyyyMMddHHmm1);
	}

	/**
	 * 返回当前日期,类型为格式"yyyy-MM-dd"的字符串 精确到天
	 * 
	 * @return 格式化出错时返回null
	 */
	public static String getDate() {
		return formatDateToString(new Date(), yyyyMMdd1);
	}

	/**
	 * 返回当前日期,类型为格式"yyyy"的字符串 精确到年
	 * 
	 * @return 格式化出错时返回null
	 */
	public static String getYear() {
		return formatDateToString(new Date(), yyyy);
	}

	/**
	 * 返回当前日期,类型为格式"yyyy-MM"的字符串 精确到月
	 * 
	 * @return 格式化出错时返回null
	 */
	public static String getMonth() {
		return formatDateToString(new Date(), yyyyMM1);
	}

	/**
	 * 返回当前日期,类型为格式"yyyyMMddHHmmssS"的字符串,精确到毫秒
	 * 
	 * @return 格式化出错时返回null
	 */
	public static String getNowTimeMS() {
		return formatDateToString(new Date(), yyyyMMddHHmmsss);
	}

	/**
	 * 返回当前日期,类型为格式"yyyyMMddHHmmss"的字符串,精确到秒
	 * 
	 * @return 格式化出错时返回null
	 */
	public static String getNowTimeYMDHMS() {
		return formatDateToString(new Date(), yyyyMMddHHmmss2);
	}

	/**
	 * 返回当前日期,类型为格式"yyyyMMdd"的字符串,精确到日
	 * 
	 * @return 格式化出错时返回null
	 */
	public static String getCurDate() {
		return formatDateToString(new Date(), yyyyMMdd2);
	}

	/**
	 * 返回当前日期第二天,类型为格式"yyyy-mm-dd"的字符串
	 * 
	 * @return 格式化出错时返回null
	 */
	public static String getTomorrowDate() {
		return formatDateToString(addDate(1), yyyyMMdd1);
	}

	/**
	 * 返回当前日期下个月,类型为格式"yyyy-mm"的字符串
	 * 
	 * @return 格式化出错时返回null
	 */
	public static String getNextMonth() {
		return formatDateToString(addMonth(1), yyyyMM1);
	}

	/**
	 * 根据自定义格式格式化字符串为yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateString
	 * @return 格式化出错时返回null
	 */
	public static Date getStrToDateYMDHMS1(String dateString) {
		return formatStringToDate(dateString, yyyyMMddHHmmss1);
	}

	/**
	 * 根据自定义格式格式化字符串为yyyy-MM-dd
	 * 
	 * @param dateString
	 * @return 格式化出错时返回null
	 */
	public static Date getStrToDateYMD1(String dateString) {
		return formatStringToDate(dateString, yyyyMMdd1);
	}

	/**
	 * 根据自定义格式格式化字符串为yyyyMMddHHmmss Date
	 * 
	 * @param dateString
	 * @return 格式化出错时返回null
	 */
	public static Date getStrToDateYMDHMS2(String dateString) {
		return formatStringToDate(dateString, yyyyMMddHHmmss2);
	}

	/**
	 * 根据自定义格式格式化字符串为yyyyMMdd Date
	 * 
	 * @param dateString
	 * @return 格式化出错时返回null
	 */
	public static Date getStrToDateYMD2(String dateString) {
		return formatStringToDate(dateString, yyyyMMdd2);
	}

	/**
	 * 判断 传入的时间 是不是大于当前时间
	 * 
	 * @param myDate
	 *            日期
	 * @return 传入时间大于当前时间返回TRUE 小于等于返回FALSE 传入空时恒返回FALSE
	 */
	public static Boolean isThanNowTime(Date myDate) {
		if (StringUtils.isEmpty(myDate)) {
			return false;
		}
		long bbl = myDate.getTime() - System.currentTimeMillis();
		if (bbl > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 第二时间和第一时间比较,大于number小时限制时间 返回TRUE 小于等于返回FALSE
	 * 
	 * @param firsttime
	 *            比较时间
	 * @param secondtime
	 *            规定时间
	 * @param number
	 *            相差小时数
	 * @return
	 */
	public static boolean isThanLowTime(Date firsttime, Date secondtime,
			String number) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(firsttime);
		c1.add(Calendar.HOUR_OF_DAY, Integer.parseInt(number));
		c2.setTime(secondtime);
		int result = c1.compareTo(c2);
		if (result < 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 当前时间和规定时间比较,大于number分钟限制时间 返回TRUE 小于返回FALSE
	 * 
	 * @param firsttime
	 * @param currtime
	 * @return
	 * @throws Exception
	 */
	public static boolean isThanLowTimeByMinute(Date firsttime, Date currtime,
			String Minute) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(firsttime);
		c1.add(Calendar.MINUTE, Integer.parseInt(Minute));
		c2.setTime(currtime);
		int result = c1.compareTo(c2);
		if (result < 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 得到两个日期的秒之差
	 * 
	 * @param firstDate
	 *            开始日期
	 * @param lastDate
	 *            结束日期
	 * @return 秒之差
	 */
	public static Long getTimeSecond(Date firstDate, Date lastDate) {
		return (lastDate.getTime() - firstDate.getTime()) / 1000;
	}

	/**
	 * 得到两个日期的分钟之差
	 * 
	 * @param firstDate
	 *            开始日期
	 * @param lastDate
	 *            结束日期
	 * @return 分钟之差
	 */
	public static Long getMinutes(Date firstDate, Date lastDate) {
		return (lastDate.getTime() - firstDate.getTime()) / (1000 * 60);
	}

	/**
	 * 得到两个日期的小时之差
	 * 
	 * @param firstDate
	 *            开始日期
	 * @param lastDate
	 *            结束日期
	 * @return 小时之差
	 */
	public static Long getHours(Date firstDate, Date lastDate) {
		return (lastDate.getTime() - firstDate.getTime()) / (1000 * 60 * 60);
	}

	/**
	 * 得到两个日期的天数之差
	 * 
	 * @param firstDate
	 *            开始日期
	 * @param lastDate
	 *            结束日期
	 * @return 天数之差
	 */
	public static Long getDays(Date firstDate, Date lastDate) {
		return (lastDate.getTime() - firstDate.getTime())
				/ (1000 * 60 * 60 * 24);
	}

	/**
	 * 相对当前时间，增加多少分钟
	 * 
	 * @param num
	 * @return
	 */
	public static Date addMinute(int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, num);

		return calendar.getTime();
	}

	/**
	 * 相对当前时间，增加或减少多少小时 负数为减少
	 * 
	 * @param num
	 * @return
	 */
	public static Date addHOUR(int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, num);

		return calendar.getTime();
	}

	/**
	 * 相对当前时间，增加多少天
	 * 
	 * @param num
	 * @return
	 */
	public static Date addDate(int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, num);

		return calendar.getTime();
	}

	/**
	 * 相对当前时间，增加多少月
	 * 
	 * @param num
	 * @return
	 */
	public static Date addMonth(int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, num);

		return calendar.getTime();
	}

	/**
	 * 相对当前时间，增加多少年
	 * 
	 * @param num
	 * @return
	 */
	public static Date addYear(int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, num);

		return calendar.getTime();
	}

	/**
	 * 获取指定日期的前后N(年,月,日,时,分,秒)日期，日期格式自己指定
	 * 
	 * @param curDate
	 *            指定日期
	 * @param field
	 *            传入类型为:(年:Calendar.YEAR,月:Calendar.MONTH,日:Calendar.DATE,时:
	 *            Calendar.HOUR,分:Calendar.MINUTE,秒:Calendar.SECOND)
	 * @param num
	 *            加、减N(年,月,日,时,分,秒)
	 * @param format
	 *            日期格式
	 * @return 若日期转换异常，则返回指定日期
	 */
	public static String getRefTime(String curDate, int field, int num,
			String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.format(getLastDate(sdf.parse(curDate), field, num));
		} catch (ParseException e) {
			log.error("time parse exception", e);
		}
		return curDate;
	}

	/**
	 * 得到N(年,月,日,时,分,秒)前,后的时间
	 * 
	 * @param date
	 *            传入的日期
	 * @param field
	 *            传入类型为:(年:Calendar.YEAR,月:Calendar.MONTH,日:Calendar.DATE,时:
	 *            Calendar.HOUR,分:Calendar.MINUTE,秒:Calendar.SECOND)
	 * @param num
	 *            N个(年,月,日,时,分,秒)
	 * @return
	 */
	private static Date getLastDate(Date date, int field, int num) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, num);
		return cal.getTime();
	}
}
