package com.medusa.gruul.common.rabbitmq.core;

/**
 * @author wangpeng
 * @data 2018-07-08下午1:41:10
 * @description TODO
 * @version V1.0
 */
public class MqSelector {

    public static final MqSelector FL_C = new MqSelector(MqType.CONSUMER, MdsConstant.RABBITMQFL);
    public static final MqSelector GSYHA_C = new MqSelector(MqType.CONSUMER, MdsConstant.RABBITMQGYSHA);
    public static final MqSelector FL_P = new MqSelector(MqType.PRODUCER, MdsConstant.RABBITMQFL);
    public static final MqSelector MDS_P = new MqSelector(MqType.PRODUCER, MdsConstant.RABBITMQGYS);
    public static final MqSelector DL_P = new MqSelector(MqType.PRODUCER, MdsConstant.RABBITMQGYS);
    public static final MqSelector GSYHA_P = new MqSelector(MqType.PRODUCER, MdsConstant.RABBITMQGYSHA);
    public static final MqSelector CALLBACK_P = new MqSelector(MqType.PRODUCER, MdsConstant.RABBITMQCALLBACKHA);
    public static final MqSelector LOG_P = new MqSelector(MqType.PRODUCER, MdsConstant.RABBITMQLOG);
    public static final MqSelector NOTIFY_P = new MqSelector(MqType.PRODUCER, MdsConstant.RABBITMQNOTIFY);

    private MqType mqType;
    private String key;

    private MqSelector(MqType mqType, String key) {
        this.mqType = mqType;
        this.key = key;
    }

    public MqType getMqType() {
        return mqType;
    }

    public void setMqType(MqType mqType) {
        this.mqType = mqType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static MqSelector getMqSelectorP(String key) {
        if (FL_P.getKey().equals(key)) {
            return FL_P;
        } else if (MDS_P.getKey().equals(key)) {
            return MDS_P;
        } else if (GSYHA_P.getKey().equals(key)) {
            return GSYHA_P;
        } else if (CALLBACK_P.getKey().equals(key)) {
            return CALLBACK_P;
        } else if (LOG_P.getKey().equals(key)) {
            return LOG_P;
        } else if (NOTIFY_P.getKey().equals(key)) {
            return NOTIFY_P;
        }
        return null;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MqSelector){
            return mqType == ((MqSelector) obj).getMqType() && key.equals(((MqSelector) obj).getKey());
        }
        return false;
    }

    @Override
    public String toString() {
        return "MqType:" + this.mqType.name() + " MqHost:" + this.getKey();
    }
}
