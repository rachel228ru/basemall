package com.medusa.gruul.logistics.util.express.sf.bean.receive;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author lcysike
 */
@XmlRootElement(name = "rls_info")
public class RlsInfo {

    @XmlAttribute(name = "rls_errormsg")
    private String rlsErrormsg;

    @XmlAttribute(name = "invoke_result")
    private String invokeResult;

    @XmlAttribute(name = "rls_code")
    private String rlsCode;

    @XmlElement(name = "rls_detail")
    private RlsDetail rlsDetail;

    @XmlTransient
    public String getRlsErrormsg() {
        return rlsErrormsg;
    }

    public void setRlsErrormsg(String rlsErrormsg) {
        this.rlsErrormsg = rlsErrormsg;
    }

    @XmlTransient
    public String getInvokeResult() {
        return invokeResult;
    }

    public void setInvokeResult(String invokeResult) {
        this.invokeResult = invokeResult;
    }

    @XmlTransient
    public String getRlsCode() {
        return rlsCode;
    }

    public void setRlsCode(String rlsCode) {
        this.rlsCode = rlsCode;
    }

    @XmlTransient
    public RlsDetail getRlsDetail() {
        return rlsDetail;
    }

    public void setRlsDetail(RlsDetail rlsDetail) {
        this.rlsDetail = rlsDetail;
    }
}
