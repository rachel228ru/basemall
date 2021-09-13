package com.medusa.gruul.common.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 同步检测结果
 *
 * @author : wangpeng
 * @version : 1.0
 * @since : 2019/10/27 下午11:28
 */
public class YmlUtil {
    private final static Logger logger = LoggerFactory.getLogger(YmlUtil.class);
    /**
     * key:文件名索引
     * value:配置文件内容
     */
    private static Map<String, LinkedHashMap> ymls = new HashMap<>();

    /**
     * string:当前线程需要查询的文件名
     */
    private static ThreadLocal<String> nowFileName = new ThreadLocal<>();

    /**
     * 加载配置文件
     * @param fileName
     */
    public static void loadYml(String fileName) {
        nowFileName.set(fileName);
        if (!ymls.containsKey(fileName)) {
            ymls.put(fileName, new Yaml().loadAs(YmlUtil.class.getResourceAsStream("/" + fileName), LinkedHashMap.class));
        }
    }

    public static Object getValue(String key) throws Exception {
        // 首先将key进行拆分
        String[] keys = key.split("[.]");

        // 将配置文件进行复制
        Map ymlInfo = (Map) ymls.get(nowFileName.get()).clone();
        if (ymlInfo.containsKey(keys[0])) {
            for (int i = 0; i < keys.length; i++) {
                Object value = ymlInfo.get(keys[i]);
                if (i < keys.length - 1) {
                    ymlInfo = (Map) value;
                } else if (value == null) {
                    throw new Exception("key不存在");
                } else {
                    return value;
                }
            }
        }
        return null;
    }

    public static Object getValue(String fileName, String key)  {
        try {
            loadYml(fileName);
            return getValue(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return 0;
        }

    }


    public static void main(String[] args) throws Exception {
        System.out.println((getValue("application.yml", "server.port")));
    }


}

