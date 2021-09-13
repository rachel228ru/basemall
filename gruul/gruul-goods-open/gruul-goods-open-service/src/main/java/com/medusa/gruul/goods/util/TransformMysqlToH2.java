package com.medusa.gruul.goods.util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 转换navicat导出的mysql的建表语句为h2的语法
 *
 * 主要的要注意的点是:
 *
 * 1.'`'全部要去掉
 *
 * 2.移除'CHARACTER'设置
 *
 * 3.移除'COLLATE'设置
 *
 * 4.移除'COMMENT'设置
 *
 * 5.'ENGINE=InnoDB'设置不支持, 删掉
 *
 * 6.'DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP'不支持, 修改为H2类似的'AS CURRENT_TIMESTAMP'
 *
 * 7.替换'float(' 和 'double(' 为  'decimal('
 *
 * 8.替换'USING BTREE'
 *
 * 9.H2的索引名必须要全局唯一, 所以需要替换所有的索引名为全局唯一
 *
 * @author lcysike
 * @since 2020-09-02
 */
public class TransformMysqlToH2 {
//
//    public static void main(String[] args) throws Exception {
//        File file = new File("/Users/lcysike/work/work/project/gruul-goods/gruul-goods-service/src/test/resources/schema.sql");
//        String content = Files.toString(file, Charsets.UTF_8);
//
//
//        content = content.replaceAll("`", "");
//        content = content.replaceAll("CHARACTER.*(?=,)", "");
//        content = content.replaceAll("COLLATE.*(?=,)", "");
//        content = content.replaceAll("COMMENT '[^']+?'", "");
//        content = content.replaceAll("COMMENT='[^']+?'", "");
//        content = content.replaceAll("\\).*ENGINE.*(?=;)", ")");
//        content = content.replaceAll("float\\(", "decimal(");
//        content = content.replaceAll("double\\(", "decimal(");
//        content = content.replaceAll("USING BTREE", "");
//        content = content.replaceAll("DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", " AS CURRENT_TIMESTAMP");
//
//        content = uniqueKey(content);
//
//        System.out.println(content);
//    }
//
//    /**
//     * h2的索引名必须全局唯一
//     *
//     * @param content sql建表脚本
//     * @return 替换索引名为全局唯一
//     */
//    private static String uniqueKey(String content) {
//        int inc = 0;
//        Pattern pattern = compile("(?<=KEY )(.*?)(?= \\()");
//        Matcher matcher = pattern.matcher(content);
//        StringBuffer sb = new StringBuffer();
//        while (matcher.find()) {
//            matcher.appendReplacement(sb, matcher.group() + inc++);
//        }
//        matcher.appendTail(sb);
//        content = sb.toString();
//        return content;
//    }
}
