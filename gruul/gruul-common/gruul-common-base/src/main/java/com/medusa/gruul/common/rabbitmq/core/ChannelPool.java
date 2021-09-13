package com.medusa.gruul.common.rabbitmq.core;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author wangpeng
 * @data 2019-11-15下午1:41:10
 * @description
 * @version V1.0
 */
public class ChannelPool {
    private final static Logger logger = LoggerFactory.getLogger(ChannelPool.class);
    private ConnectionFactory factory ;
    private Map<String,String> config ;
    private Queue<PConnection> connections = new ConcurrentLinkedQueue<PConnection>();

    public ChannelPool(ConnectionFactory factory, Map<String,String> config){
        this.factory = factory;
        this.config = config;
        int connectionNum = config == null || !config.containsKey("CONNECTNUM") ? 3 : Integer.valueOf(config.get("CONNECTNUM"));
        int min = config == null || !config.containsKey("CHANNELMIN") ? 1 : Integer.valueOf(config.get("CHANNELMIN"));
        int max = config == null || !config.containsKey("CHANNELMAX") ? 3 : Integer.valueOf(config.get("CHANNELMAX"));
        for(int i =0 ;i < connectionNum;i++){
            try {
                Connection conn = factory.newConnection();
                connections.add(new PConnection(conn,min,max));
            }catch (Exception ex){
                logger.error("connection error",ex);
            }
        }
    }

    public synchronized Channel getChannel(){
        if(connections.size() == 1){
            return connections.peek().getChannel();
        }else{
            int size = connections.size();
            while(size-- > 0){
                PConnection connection = connections.poll();
                connections.add(connection);
                if(connection.canUse()){
                    return connection.getChannel();
                }
            }
        }
        return null;
    }

    public synchronized void returnChannel(Channel channel){
        for (PConnection connection:connections){
            if(connection.contain(channel)){
                connection.returnChannel(channel);
                break;
            }
        }
    }

    public void close(){
        for(PConnection connection:connections){
            try {
                connection.close();
            }catch (Exception ex){
                logger.error("connection close error",ex);
            }
        }
    }

    public boolean isOk(){
        try {
            if (CollectionUtils.isNotEmpty(connections)) {
                for (PConnection pConnection : connections) {
                    if (!pConnection.isOk()) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("ChannelPool isOk error",e);
            return false;
        }
    }
}
