package com.medusa.gruul.goods.util;

import cn.hutool.core.lang.Console;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvRow;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * csv文件处理工具类
 * </p>
 *
 * @author lcysike
 * @since 2020-05-12
 */
public class CsvFileUtil {

    public static List<Map<String, Object>> transCsvRowToList(CsvData data){
        List<CsvRow> csvRows = data.getRows();
        List<CsvRow> rows = new ArrayList(csvRows);
        rows.remove(0);
        rows.remove(1);
        CsvRow titleRow = rows.get(0);
        rows.remove(0);
        String[] keys = titleRow.get(0).split("\\t");
        List<Map<String, Object>> list = new ArrayList<>(rows.size());
        //遍历行
        for (CsvRow csvRow : rows) {
            //getRawList返回一个List列表，列表的每一项为CSV中的一个单元格（既逗号分隔部分）
            Console.log(csvRow.getRawList());
            String[] values = csvRow.get(0).split("\\t");
            Map<String, Object> map = new HashMap<>(values.length);
            for (int i = 0; i < values.length; i++) {
                map.put(keys[i], values[i]);
            }
            list.add(map);
        }
        return list;
    }

    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (!("").equals(file) && file.getSize() > 0) {
            InputStream ins;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    /**
     * 获取流文件
     *
     * @param file
     * @throws Exception
     */
    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
