package cn.metathought.tool.util;

import java.util.Date;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description:编号生成工具类
 * @Author andrewzbl
 * @Date 2016/11/9
 */
public class CodeUtil {

    public static final Snowflake snowflak = new Snowflake(1);

    private static String[] chars = new String[]{
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * 64位随机码uuid生成器
     * <p/>
     * snowflak算法
     */
    public static String uuid64() {
        return String.valueOf(snowflak.nextId());
    }

    /**
     * 32位随机码uuid生成器
     */
    public static String uuid() {
        return StringUtils.replaceChars(UUID.randomUUID().toString(), "-", "");
    }

    /**
     * 8位随机码uuid生成器
     */
    public static String uuid8() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString().toUpperCase();
    }

    /**
     * 获取四位验证码
     */
    public static int getVerifyCode() {
        int code = (int) (Math.random() * 8999 + 1000);
        return code;
    }

    /**
     * 订单号生成器
     */
    public static String getOrderNO() {
        StringBuffer orderNO = new StringBuffer(DateTool.formatDateToString(new Date(), DateTool.yyyyMMddHHmm2));
        orderNO.append(uuid8());
        return orderNO.toString();
    }

    /**
     * 获取会员卡卡号
     */
    public static String getMemberCardNo(String code) {
        StringBuffer reciid = new StringBuffer("H");
        reciid.append(code);
        reciid.append(DateTool.formatDateToString(new Date(), DateTool.yyyyMMddHHmmsss));
        return reciid.toString();
    }
}