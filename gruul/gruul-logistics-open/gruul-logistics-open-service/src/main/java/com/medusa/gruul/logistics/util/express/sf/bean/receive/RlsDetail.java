package com.medusa.gruul.logistics.util.express.sf.bean.receive;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author lcysike
 */
@XmlRootElement(name = "rls_detail")
public class RlsDetail {

    @XmlAttribute(name = "waybillNo")
    private String waybillNo;

    @XmlAttribute(name = "sourceTransferCode")
    private String sourceTransferCode;

    @XmlAttribute(name = "sourceCityCode")
    private String sourceCityCode;

    @XmlAttribute(name = "sourceDeptCode")
    private String sourceDeptCode;

    @XmlAttribute(name = "sourceTeamCode")
    private String sourceTeamCode;

    @XmlAttribute(name = "destCityCode")
    private String destCityCode;

    @XmlAttribute(name = "destDeptCode")
    private String destDeptCode;

    @XmlAttribute(name = "destRouteLabel")
    private String destRouteLabel;

    @XmlAttribute(name = "proName")
    private String proName;

    @XmlAttribute(name = "cargoTypeCode")
    private String cargoTypeCode;

    @XmlAttribute(name = "limitTypeCode")
    private String limitTypeCode;

    @XmlAttribute(name = "expressTypeCode")
    private String expressTypeCode;

    @XmlAttribute(name = "codingMappingOut")
    private String codingMappingOut;

    @XmlAttribute(name = "xbFlag")
    private String xbFlag;

    @XmlAttribute(name = "printFlag")
    private String printFlag;

    @XmlAttribute(name = "twoDimensionCode")
    private String twoDimensionCode;

    @XmlAttribute(name = "proCode")
    private String proCode;

    @XmlAttribute(name = "printIcon")
    private String printIcon;

    @XmlTransient
    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    @XmlTransient
    public String getSourceTransferCode() {
        return sourceTransferCode;
    }

    public void setSourceTransferCode(String sourceTransferCode) {
        this.sourceTransferCode = sourceTransferCode;
    }

    @XmlTransient
    public String getSourceCityCode() {
        return sourceCityCode;
    }

    public void setSourceCityCode(String sourceCityCode) {
        this.sourceCityCode = sourceCityCode;
    }

    @XmlTransient
    public String getSourceDeptCode() {
        return sourceDeptCode;
    }

    public void setSourceDeptCode(String sourceDeptCode) {
        this.sourceDeptCode = sourceDeptCode;
    }

    @XmlTransient
    public String getSourceTeamCode() {
        return sourceTeamCode;
    }

    public void setSourceTeamCode(String sourceTeamCode) {
        this.sourceTeamCode = sourceTeamCode;
    }

    @XmlTransient
    public String getDestCityCode() {
        return destCityCode;
    }

    public void setDestCityCode(String destCityCode) {
        this.destCityCode = destCityCode;
    }

    @XmlTransient
    public String getDestDeptCode() {
        return destDeptCode;
    }

    public void setDestDeptCode(String destDeptCode) {
        this.destDeptCode = destDeptCode;
    }

    @XmlTransient
    public String getDestRouteLabel() {
        return destRouteLabel;
    }

    public void setDestRouteLabel(String destRouteLabel) {
        this.destRouteLabel = destRouteLabel;
    }

    @XmlTransient
    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    @XmlTransient
    public String getCargoTypeCode() {
        return cargoTypeCode;
    }

    public void setCargoTypeCode(String cargoTypeCode) {
        this.cargoTypeCode = cargoTypeCode;
    }

    @XmlTransient
    public String getLimitTypeCode() {
        return limitTypeCode;
    }

    public void setLimitTypeCode(String limitTypeCode) {
        this.limitTypeCode = limitTypeCode;
    }

    @XmlTransient
    public String getExpressTypeCode() {
        return expressTypeCode;
    }

    public void setExpressTypeCode(String expressTypeCode) {
        this.expressTypeCode = expressTypeCode;
    }

    @XmlTransient
    public String getCodingMappingOut() {
        return codingMappingOut;
    }

    public void setCodingMappingOut(String codingMappingOut) {
        this.codingMappingOut = codingMappingOut;
    }

    @XmlTransient
    public String getXbFlag() {
        return xbFlag;
    }

    public void setXbFlag(String xbFlag) {
        this.xbFlag = xbFlag;
    }

    @XmlTransient
    public String getPrintFlag() {
        return printFlag;
    }

    public void setPrintFlag(String printFlag) {
        this.printFlag = printFlag;
    }

    @XmlTransient
    public String getTwoDimensionCode() {
        return twoDimensionCode;
    }

    public void setTwoDimensionCode(String twoDimensionCode) {
        this.twoDimensionCode = twoDimensionCode;
    }

    @XmlTransient
    public String getProCode() {
        return proCode;
    }

    public void setProCode(String proCode) {
        this.proCode = proCode;
    }

    @XmlTransient
    public String getPrintIcon() {
        return printIcon;
    }

    public void setPrintIcon(String printIcon) {
        this.printIcon = printIcon;
    }
}
