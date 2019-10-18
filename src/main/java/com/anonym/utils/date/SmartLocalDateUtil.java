package com.anonym.utils.date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * java8 LocalDate 日期工具类
 *
 * @author listen
 * @date 2019/02/20 10:16
 */
public class SmartLocalDateUtil {

    /**
     * 日期格式 ： yyyy-MM-dd
     * 例：2019-10-15
     */
    public static final String YMD = "yyyy-MM-dd";

    /**
     * 日期格式 ： yyyy-MM-dd HH:mm:ss
     * 例：2019-10-15 10:15:00
     */
    public static final String YMD_HMS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式 ： yyyy-MM-dd HH:mm
     * 例：2019-10-15 10:15
     */
    public static final String YMD_HM = "yyyy-MM-dd HH:mm";

    /**
     * 日期格式 ： yyyy-MM
     * 例：2019-10
     */
    public static final String YM = "yyyy-MM";

    /**
     * 获取 DateTimeFormatter 格式器 yyyy-MM-dd
     *
     * @return
     */
    public static DateTimeFormatter formatterYMD() {
        return DateTimeFormatter.ofPattern(YMD);
    }

    /**
     * 获取 DateTimeFormatter 格式器 yyyy-MM-dd HH:mm
     *
     * @return
     */
    public static DateTimeFormatter formatterYMDHM() {
        return DateTimeFormatter.ofPattern(YMD_HM);
    }

    /**
     * 获取 DateTimeFormatter 格式器 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static DateTimeFormatter formatterYMDHMS() {
        return DateTimeFormatter.ofPattern(YMD_HMS);
    }

    /**
     * 获取 DateTimeFormatter 格式器 yyyy-MM
     *
     * @return
     */
    public static DateTimeFormatter formatterYM() {
        return DateTimeFormatter.ofPattern(YM);
    }

    /**
     * 格式日期 yyyy-MM-dd
     * 返回String字符
     *
     * @return
     */
    public static String formatYMD(LocalDateTime localDateTime) {
        return localDateTime.format(formatterYMD());
    }

    /**
     * 格式日期 yyyy-MM-dd HH:mm:ss
     * 返回String字符
     *
     * @return
     */
    public static String formatYMDHMS(LocalDateTime localDateTime) {
        return localDateTime.format(formatterYMDHMS());
    }

    public static String formatYMDHMS(LocalDate localDate) {
        return localDate.format(formatterYMDHMS());
    }

    public static String formatYMD(LocalDate localDate) {
        return localDate.format(formatterYMD());
    }
    /**
     * 格式日期 yyyy-MM-dd HH:mm
     * 返回String字符
     *
     * @return
     */
    public static String formatYMDHM(LocalDateTime localDateTime) {
        return localDateTime.format(formatterYMDHM());
    }

    /**
     * 格式日期 yyyy-MM
     * 返回String字符 2019-10
     *
     * @return
     */
    public static String formatYM(LocalDateTime localDateTime) {
        return localDateTime.format(formatterYM());
    }

    public static LocalDate parseYMD(String dateStr) {
        return LocalDate.parse(dateStr, formatterYMD());
    }

    public static LocalDateTime parseYMDHMS(String dateStr) {
        return LocalDateTime.parse(dateStr, formatterYMDHMS());
    }
}
