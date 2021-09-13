package com.medusa.gruul.account;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 转换navicat导出的mysql的建表语句为h2的语法
 * <p>
 * 主要的要注意的点是:
 * <p>
 * 1.'`'全部要去掉
 * <p>
 * 2.移除'CHARACTER'设置
 * <p>
 * 3.移除'COLLATE'设置
 * <p>
 * 4.移除'COMMENT'设置
 * <p>
 * 5.'ENGINE=InnoDB'设置不支持, 删掉
 * <p>
 * 6.'DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP'不支持, 修改为H2类似的'AS CURRENT_TIMESTAMP'
 * <p>
 * 7.替换'float(' 和 'double(' 为  'decimal('
 * <p>
 * 8.替换'USING BTREE'
 * <p>
 * 9.H2的索引名必须要全局唯一, 所以需要替换所有的索引名为全局唯一
 *
 * @author: alan
 * @date: 2020/8/30 14:44
 */
public class TransformMysqlToH2 {

    public static void main(String[] args) throws Exception {
        File file = new File("G:\\社区团购项目文档\\单元测试\\account_open.sql");
        String content = Files.toString(file, Charsets.UTF_8);


        content = content.replaceAll("`", "");
        content = content.replaceAll("CHARACTER.*(?=,)", "");
        content = content.replaceAll("COLLATE.*(?=,)", "");
        content = content.replaceAll("COMMENT '[^']+?'", "");
        content = content.replaceAll("COMMENT='[^']+?'", "");
        content = content.replaceAll("\\).*ENGINE.*(?=;)", ")");
        content = content.replaceAll("float\\(", "decimal(");
        content = content.replaceAll("double\\(", "decimal(");
        content = content.replaceAll("USING BTREE", "");
        content = content.replaceAll("DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", " AS CURRENT_TIMESTAMP");

        content = uniqueKey(content);

        System.out.println(content);
    }

    /**
     * h2的索引名必须全局唯一
     *
     * @param content sql建表脚本
     * @return 替换索引名为全局唯一
     */
    private static String uniqueKey(String content) {
        int inc = 0;
        Pattern pattern = Pattern.compile("(?<=KEY )(.*?)(?= \\()");
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group() + inc++);
        }
        matcher.appendTail(sb);
        content = sb.toString();
        return content;
    }

}