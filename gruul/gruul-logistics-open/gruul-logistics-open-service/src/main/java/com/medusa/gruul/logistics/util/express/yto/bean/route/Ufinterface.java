package com.medusa.gruul.logistics.util.express.yto.bean.route;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * <p>
 * 圆通物流查询请求主体
 * </p>
 *
 * @author lcysike
 * @since 2020-03-12
 */
@XmlRootElement(name = "ufinterface")
public class Ufinterface {

    /**
     * 请求头包装数据
     */
    @XmlElement(name = "Result")
    private Result result;

    @XmlTransient
    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
