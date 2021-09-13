package com.medusa.gruul.logistics.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * <p>
 * XML转换工具类
 * </p>
 *
 * @author lcysike
 * @since 2020-03-12
 */
public class CoverXMLUtil {

    /**
     * JavaBean转换成xml.
     *
     * @param obj
     *            bean实体
     * @param encoding
     *            默认编码UTF-8
     * @return
     */
    public static String convertToXml(Object obj, String encoding) {
        String result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            result = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
