package com.medusa.gruul.common.core.util;


import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 请求用户信息工具类
 * </p>
 *
 * @author 王鹏
 * @since 2019-1206-23
 */
public class StringUtil extends StringUtils {

    // 一个空的字符串。
    public static final String EMPTY_STRING = "";

    // ================================将符号也作为常量，避免半角全角符号导致的狗血异常======================================
    // 逗号
    public static final String SYMBOL_COMMA = ",";

    // 等于号
    public static final String SYMBOL_EQUAL = "=";

    // 点号
    public static final String SYMBOL_DOT = ".";

    // 问号
    public static final String SYMBOL_QUESTION = "?";

    // 分号
    public static final String SYMBOL_SEMICOLON = ";";

    /**
     * 验证EMAIL正则表达式
     */
    public static final String PATTERN_EMAIL = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";

    /**
     * 如果传入的值是null，返回空字符串，如果不是null，返回本身。
     *
     * @param word 传入的源字符串。
     * @return
     */
    public static String getNotNullValue(String word) {
        return (word == null || "null".equalsIgnoreCase(word)) ? "" : word;
    }

    /**
     * 如果传入的值是null，返回空字符串，如果不是null，返回本身。
     *
     * @param word 传入的源字符串。
     * @return
     */
    public static String getNotNullValue(String word, String defaultWord) {
        return (word == null || "null".equalsIgnoreCase(word)) ? defaultWord : word;
    }

    /**
     * 根据分隔符从一段字符串拿到对应的列表。应用于以下场景。 2,3,4,5 ==> [2,3,4,5]
     *
     * @param originWord
     * @param symbol
     * @return
     */
    public static List<String> getSplitListFromString(String originWord, String symbol) {
        List<String> result = new ArrayList<String>();
        if (isBlank(originWord)) {
            return result;
        }

        String[] splitData = originWord.split(symbol);
        if (splitData == null || splitData.length == 0) {
            return result;
        }

        for (String word : splitData) {
            if (isNotBlank(word)) {
                result.add(word);
            }
        }
        return result;
    }

    /**
     * 将map转换成String
     *
     * @param map
     * @return
     */
    public static String mapToString(Map<String, String> map) {
        Set<String> keySet = map.keySet();
        StringBuffer strBuff = new StringBuffer();
        for (String keyStr : keySet) {
            if (StringUtils.isNotBlank(map.get(keyStr))) {

                strBuff.append(keyStr).append("=").append(map.get(keyStr)).append("&");
            }
        }
        return strBuff.substring(0, strBuff.length() - 1).toString();
    }

    /**
     * @param originalStr
     * @param symbol
     * @return
     */
    public static List<Long> getLongListFromString(String originalStr, String symbol) {
        List<Long> result = new ArrayList<Long>();
        if (isBlank(originalStr)) {
            return result;
        }

        String[] splitData = originalStr.split(symbol);

        for (String word : splitData) {
            if (isNotBlank(word)) {
                result.add(Long.parseLong(word));
            }
        }
        return result;
    }

    /**
     * 移除左边的0, eg：00000jakjdkf89000988000 转换之后变为 jakjdkf89000988000
     *
     * @param str
     * @return
     */
    public static String removeLeftZero(String str) {
        int start = 0;
        if (isNotEmpty(str)) {
            char[] chars = str.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] != '0') {
                    start = i;
                    break;
                }
            }
            return str.substring(start);
        }
        return "";
    }

    /**
     * 格式化大数据为len个小数
     *
     * @param obj
     * @param len
     * @return
     */
    public static String formatLagerNumberToStr(Object obj, int len) {
        if (obj != null) {
            if (obj instanceof String) {
                String str = String.valueOf(obj);
                return str.substring(0, str.indexOf(".") + len + 1);
            } else {
                StringBuffer pattern = new StringBuffer("0");
                for (int i = 0; i < len; i++) {
                    if (i == 0) {
                        pattern.append(".0");
                        continue;
                    }
                    pattern.append("0");
                }

                return new DecimalFormat(pattern.toString()).format(obj);
            }
        }

        return "";
    }

    /**
     * 金额格式化
     *
     * @param money 金额
     * @param len   小数位数
     * @return 格式后的金额
     */
    public static String insertComma(String money, int len) {
        if (money == null || money.length() < 1) {
            return "";
        }
        NumberFormat formater = null;
        double num = Double.parseDouble(money);
        if (len == 0) {
            formater = new DecimalFormat("###,###");

        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###,###.");
            for (int i = 0; i < len; i++) {
                buff.append("#");
            }
            formater = new DecimalFormat(buff.toString());
        }
        return formater.format(num);
    }

    /**
     * 金额去掉“,”
     *
     * @param money 金额
     * @return 去掉“,”后的金额
     */
    public static String delComma(String money) {
        String formatString = "";
        if (money != null && money.length() >= 1) {
            formatString = money.replaceAll(",", "");
        }

        return formatString;
    }

    public static String toRMB(double money) {
        char[] s1 = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
        char[] s4 = {'分', '角', '元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿', '拾', '佰', '仟', '万'};
        String str = String.valueOf(Math.round(money * 100 + 0.00001));
        String result = "";

        for (int i = 0; i < str.length(); i++) {
            int n = str.charAt(str.length() - 1 - i) - '0';
            result = s1[n] + "" + s4[i] + result;
        }

        result = result.replaceAll("零仟", "零");
        result = result.replaceAll("零佰", "零");
        result = result.replaceAll("零拾", "零");
        result = result.replaceAll("零亿", "亿");
        result = result.replaceAll("零万", "万");
        result = result.replaceAll("零元", "元");
        result = result.replaceAll("零角", "零");
        result = result.replaceAll("零分", "零");

        result = result.replaceAll("零零", "零");
        result = result.replaceAll("零亿", "亿");
        result = result.replaceAll("零零", "零");
        result = result.replaceAll("零万", "万");
        result = result.replaceAll("零零", "零");
        result = result.replaceAll("零元", "元");
        result = result.replaceAll("亿万", "亿");

        result = result.replaceAll("零$", "");
        result = result.replaceAll("元$", "元整");

        return result;
    }

    public static boolean containStr(String rawStr, String containStr) {
        if ((rawStr != null) && (containStr != null)) {
            return rawStr.contains(containStr);
        }
        return false;
    }

    /**
     * 判断是否为数字或小数
     *
     * @param number
     * @return
     */
    public static boolean checkNumber(String number) {
        String str = "^[0-9]+(.[0-9]{2})?$";
        Pattern pattern = Pattern.compile(str);
        if (StringUtil.isNotBlank(number)) {
            Matcher matcher = pattern.matcher(number);
            return matcher.find();
        }
        return false;

    }

    /**
     * 过程名称：ChineseLen(获得当前文字的长度，中文为2个字符)
     *
     * @param FromStr
     * @return
     */

    public static boolean checkChineseLen(String FromStr, int maxLen) {
        if (StringUtil.isBlank(FromStr)) {
            return false;
        }
        int FromLen = FromStr.length();

        int ChineseLen = 0;

        for (int i = 0; i < FromLen; i++) {

            if (gbValue(FromStr.charAt(i)) > 0) {

                ChineseLen = ChineseLen + 2;

            } else {

                ChineseLen++;

            }

        }

        if (ChineseLen <= maxLen) {
            return true;
        } else {
            return false;
        }

    }

    /*******
     * 过程名称：gbValue(返回GBK的编码)
     *
     * @param ch
     * @return
     */

    public static int gbValue(char ch) {
        String str = new String();

        str += ch;

        try {
            byte[] bytes = str.getBytes("GBK");

            if (bytes.length < 2) {
                return 0;
            }
            return (bytes[0] << 8 & 0xff00) + (bytes[1] & 0xff);

        } catch (Exception e) {

            return 0;

        }

    }

    /**
     * 半角转全角
     *
     * @param input String.
     * @return 全角字符串.
     */
    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);

            }
        }
        return new String(c);
    }

    /**
     * 全角转半角
     *
     * @param input String.
     * @return 半角字符串
     */
    public static String ToDBC(String input) {

        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);

            }
        }
        String returnString = new String(c);

        return returnString;
    }

    /**
     * LIST转换为字符串，转换时插入制定的分隔符
     *
     * @param list   字符串LIST
     * @param symbol 分隔符
     * @return
     */
    public static String listToString(List<?> list, String symbol) {
        String res = null;
        if (null != list && 0 < list.size()) {
            int size = list.size();
            if (size == 1) {
                return String.valueOf(list.get(0));
            }

            StringBuilder sb = new StringBuilder();
            if (symbol == null) {
                symbol = "";
            }

            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    sb.append(symbol);
                }

                sb.append(String.valueOf(list.get(i)));
            }
            res = sb.toString();
        }

        return res;
    }

    /**
     * <p>
     * 判断是否为数字格式
     * </p>
     *
     * <pre>
     * StringUtils.isNumeric(null)   = false
     * StringUtils.isNumeric("")     = false
     * StringUtils.isNumeric("  ")   = false
     * StringUtils.isNumeric("123")  = true
     * StringUtils.isNumeric("12 3") = false
     * StringUtils.isNumeric("ab2c") = false
     * StringUtils.isNumeric("12-3") = false
     * StringUtils.isNumeric("12.3") = true
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if only contains digits, and is non-null
     */
    public static boolean isDoubleNumeric(String str) {
        if (str == null) {
            return false;
        }

        try {
            Double.parseDouble(str.replace(",", ""));
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 判断是否为数字(有小数位或者有千分位)
     *
     * @param str
     * @return
     */
    public static boolean isNumericDecimal(String str) {
        return isDoubleNumeric(str) && (str.indexOf(".") > 0 || str.indexOf(",") > 0);
    }

    /**
     * 判断是否为email格式
     *
     * @param email 验证字符串
     * @return true|false
     */
    public static boolean isEmail(String email) {
        Pattern p = Pattern.compile(PATTERN_EMAIL);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 字符数组转化为字符串
     *
     * @param strs   字符数组
     * @param symbol 分隔符
     * @return
     */
    public static String arrayToStr(String[] strs, String symbol) {
        return getString(strs, symbol);
    }

    public static String arrayToStr(String symbol, String... strs) {
        return getString(strs, symbol);
    }

    private static String getString(String[] strs, String symbol) {
        if (strs == null || strs.length == 0) {
            return null;
        }
        if (symbol == null) {
            symbol = "";
        }
        StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            if (sb.length() > 0) {
                sb.append(symbol);
            }
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * 首字母大写
     *
     * @param s
     * @return
     */
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }


    public static String genNumberRandomCode(int fix) {
        String chars = "0123456789";
        char[] rands = new char[fix];
        for (int i = 0; i < fix; i++) {
            int rand = (int) (Math.random() * 10);
            rands[i] = chars.charAt(rand);
        }
        return new String(rands);
    }
}
