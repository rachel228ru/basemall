package com.medusa.gruul.common.rabbitmq.core;
import com.alibaba.fastjson.JSON;
import com.medusa.gruul.common.core.util.YmlUtil;
import com.medusa.gruul.common.rabbitmq.interfaces.ReConnectionListener;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangpeng
 */

public class BaseMq {
    private static final Integer PORT =  Integer.parseInt(String.valueOf(YmlUtil.getValue("bootstrap.yml", "rabbitmq.port")));
    private static final String HOST =  String.valueOf(YmlUtil.getValue("bootstrap.yml", "rabbitmq.host"));
    private static final String USER =  String.valueOf(YmlUtil.getValue("bootstrap.yml", "rabbitmq.user"));
    private static final String PASSWORD =  String.valueOf(YmlUtil.getValue("bootstrap.yml", "rabbitmq.password"));
    private static final String VIRTUAL_HOST = "/";
    private static final int THREAD_NUM = 32;
    private static final int HEART_BEAT = 20;
    private static final int NETWORK_RECOVERY_INTERVAL = 10000;
    private static final int CONNECTION_TIMEOUT = 15000;
    private static Map<MqSelector, ConnectionFactory> typeFactoryMap = new ConcurrentHashMap<MqSelector, ConnectionFactory>();
    private static Map<MqSelector, ChannelPool> typeChannelPoolMap = new ConcurrentHashMap<MqSelector, ChannelPool>();
    private final static Logger logger = LoggerFactory.getLogger(BaseMqPublish.class);
    private static Map<MqSelector, BaseMq> instances = new ConcurrentHashMap<MqSelector, BaseMq>();
    private MqSelector selector;
    private BaseMq(MqSelector selector) {
        this.selector = selector;
    }

    public synchronized static BaseMq getInitedInstance(MqSelector selector) throws Exception {
        BaseMq instance = instances.get(selector);
        if (instance == null) {
            instance = makeInstance(selector);
            instances.put(selector, instance);
        }
        return instance;
    }

    public synchronized static void initMany(MqSelector... selectors) throws Exception {
        if (selectors == null || selectors.length == 0){
            return;
        } 
        for (MqSelector selector : selectors) {
            getInitedInstance(selector);
        }
    }

    public synchronized static void destroyAll() {
        if (instances == null || instances.size() == 0) {
            return;
        }
        for (BaseMq instance : instances.values()) {
            instance.destroy();
        }
    }


    private static BaseMq makeInstance(MqSelector selector) throws Exception {
        BaseMq instance = new BaseMq(selector);
        instance.doInit();
        return instance;
    }

    private void doInit() throws Exception {
        if (selector == null) {
            throw new Exception("must init mqSelector");
        }

        if (!typeFactoryMap.containsKey(selector)) {
            logger.info(selector + "类型的mq连接池为空，开始初始化！！！");
            connection();
        } else {
            logger.info(selector + "类型的mq连接池已初始化！！！");
        }
    }


    /**
     * MQ连接
     *
     * @param
     */

    private void connection() {

        Map<String, String> serverInfo = new HashMap<>();
        serverInfo.put("PORT", String.valueOf(PORT));
        serverInfo.put("USER", USER);
        serverInfo.put("PASSWORD", PASSWORD);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(serverInfo.get("USER"));
        factory.setPassword(serverInfo.get("PASSWORD"));
        //最多起64个并行
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_NUM);
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setVirtualHost(VIRTUAL_HOST);
        factory.setSharedExecutor(threadPool);
        //心跳超时时间
        factory.setRequestedHeartbeat(HEART_BEAT);
        //支持重联
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(NETWORK_RECOVERY_INTERVAL);
        factory.setConnectionTimeout(CONNECTION_TIMEOUT);
        typeFactoryMap.put(selector, factory);
        if (selector.getMqType() == MqType.PRODUCER) {
            ChannelPool channelPool = newChannelPool(factory, serverInfo);
            typeChannelPoolMap.put(selector, channelPool);
        }
    }

    private static ChannelPool newChannelPool(ConnectionFactory factory, Map<String, String> serverInfo) {
        return new ChannelPool(factory, serverInfo);
    }


    /**
     * 索取
     *
     * @param
     * @return
     */

    public Channel getChannel() {
        Channel channel = null;
        if (typeChannelPoolMap.containsKey(selector)) {
            ChannelPool channelPool = typeChannelPoolMap.get(selector);
            try {
                channel = channelPool.getChannel();
            } catch (Exception ex) {
                logger.error("getChannel error try connection", ex);
                return null;
            }
        }
        return channel;
    }


     /**
     * 回收
     *
     * @param channel
     */

    public void returnChannel(Channel channel) {
        if (typeChannelPoolMap.containsKey(selector)) {
            ChannelPool channelPool = typeChannelPoolMap.get(selector);
            if (null != channelPool) {
                channelPool.returnChannel(channel);
            }
        }
    }


   /**
     * 消费者获取连接工厂
     *
     * @return
     */

    public ConnectionFactory getConnectionFactory() {
        return typeFactoryMap.get(selector);
    }



      /**
     * 销毁
     *
     * @param
     */

    public void destroy() {
        if (typeChannelPoolMap.containsKey(selector)) {
            try {
                ChannelPool channelPool = typeChannelPoolMap.get(selector);
                if (null != channelPool) {
                    channelPool.close();
                }
            } catch (Exception ex) {
                logger.error("channelPool close error", ex);
            }
            typeChannelPoolMap.remove(selector);
        }

        if (typeFactoryMap.containsKey(selector)) {
            typeFactoryMap.remove(selector);
        }

        if (instances.containsKey(selector)) {
            instances.remove(selector);
        }
    }

    private static Map<MqSelector, ReConnectionListener> reConnectionListenerMap = new ConcurrentHashMap<MqSelector, ReConnectionListener>();


     /**
     * 注册回调方法
     *
     * @param listener
     */

    public void registerListener(ReConnectionListener listener) {
        if (null != listener) {
            reConnectionListenerMap.put(selector, listener);
        }
    }

    private final RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3 * 1000).setConnectTimeout(3 * 1000).build();

    private boolean getNodeStatus(String host, int port, String nodeName) {
        try {
            HttpHost httpHost = new HttpHost(host, port);
            HttpGet httpGet = new HttpGet("/api/nodes/" + nodeName);
            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(new AuthScope(host, port), new UsernamePasswordCredentials("smstest", "4r43RW41wa"));
            CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
            httpGet.setConfig(requestConfig);
            HttpResponse response = client.execute(httpHost, httpGet);
            HttpEntity entity = response.getEntity();
            String retInfo = EntityUtils.toString(entity);
            if (StringUtils.isNotBlank(retInfo)) {

                Map map = JSON.parseObject(retInfo, Map.class);
                if (null != map) {
                    Object running = map.get("running");
                    if (running instanceof Boolean) {
                        return (Boolean) running;
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("查询节点状态失败" + ex);
        }
        return Boolean.FALSE;
    }


   /**
     * 心跳检查(生产者)
     */

    private void heartBeat4P(final ConnectionFactory factory) {
        final int maxCount = 4;
        final AtomicInteger count = new AtomicInteger(0);
        logger.info("heartBeat start host:" + factory.getHost());
        final ScheduledExecutorService scheduled = new ScheduledThreadPoolExecutor(2, new BasicThreadFactory.Builder().namingPattern("heartBeat-mq-pool-%d").build());
        scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                String host = factory.getHost();
                try {
                    ChannelPool pool = typeChannelPoolMap.get(selector);
                    if (!pool.isOk() ){
                        logger.error("rabbitMq:" + selector.getKey() + " host:" + host + " channel is error !!!");
                        if (count.getAndIncrement() >= maxCount){
                            logger.error("rabbitMq:" + selector.getKey() + " host:" + host + " channel close !!! need to reconnection!!!");
                            reConnection(host);
                        } else {
                            logger.error("rabbitMq:" + selector.getKey() + " host:" + host + " channel close !!! wait " + (maxCount - count.get()) + " to reconnection!!!");
                        }
                    } else {
                        count.set(0);
                        logger.info("rabbitMq:" + selector.getKey() + " host:" + host + " channel is ok !!!");
                    }
                } catch (Exception ex) {
                    logger.error("check connection or channel error", ex);
                    logger.error("rabbitMq:" + selector.getKey() + " host:" + host + " channel close !!! need to reconnection!!!");
                    reConnection(host);
                }
            }

            void reConnection(String host) {
                count.set(0);
                scheduled.shutdownNow();
                //0
                destroy();
                //2 重联初始化
                connection();
              //  WeChatWarnMsgUtil.sendMsg(2, "IP:" + IPUtil.getHostIp() +  ",BUSI_TYPE:" + Params.params.getOther(BUSI_TYPE) + ",BUSI_TAG:" + Params.params.getOther(BUSI_TAG) + ",HOST:" + host + "的rabbitmq服务已重连！！！");
            }
        }, 5, 5, TimeUnit.SECONDS);
    }
}

