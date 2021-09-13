package com.medusa.gruul.common.rabbitmq.core;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author wangpeng
 * @data 2018-07-08下午1:41:10
 * @description TODO
 * @version V1.0
 */
public class PConnection {
    private final static Logger logger = LoggerFactory.getLogger(PConnection.class);
    private Connection connection;
    private List<Channel> channels = new LinkedList<Channel>();
    private Set<Channel> channelSet = new HashSet<Channel>();
    private int now;
    private int min;
    private int max;

    public PConnection(Connection connection, int min, int max) {
        this.connection = connection;
        this.min = min;
        this.max = max;
        for (int i = 0; i < min; i++) {
            try {
                Channel channel = connection.createChannel();
                channels.add(channel);
                channelSet.add(channel);
                now++;
            } catch (Exception ex) {
                logger.error("createChannel error", ex);
            }
        }
    }

    public boolean canUse() {
        return (now < max) && connection.isOpen();
    }

    public Channel getChannel() {
        if (canUse()) {
            Channel channel = null;
            if (channels.size() > 0) {
                channel = channels.remove(0);
                if (!test(channel)) {
                    try {
                        channel.close();
                    } catch (Exception ex) {
                        logger.error("channel close error", ex);
                    }
                    channel = null;
                }
                now--;
            }
            if (channel == null) {
                try {
                    channel = connection.createChannel();
                    channelSet.add(channel);
                    now++;
                } catch (Exception ex) {
                    logger.error("create Channel error", ex);
                }
            }
            return channel;
        }
        return null;
    }

    public boolean test(Channel channel) {
        return channel.isOpen();
    }

    public boolean contain(Channel channel) {
        return channelSet.contains(channel);
    }

    public void returnChannel(Channel channel) {
        if (now < min) {
            channels.add(channel);
        } else {
            try {
                channelSet.remove(channel);
                channel.close();
            } catch (Exception ex) {
                logger.error("channel close error", ex);
            }
            now--;
        }
    }

    public void close() throws Exception {
        if (connection.isOpen()){
            connection.close();
        }
    }

    public boolean isOk() throws Exception {
        if (connection.isOpen()) {
            Channel channel = channelSet.iterator().next();
            try {
                if (channel.isOpen()) {
                    return Boolean.TRUE;
                }
            } catch (Exception ex) {
                logger.error("channel isOk error", ex);
            }
        }
        return Boolean.FALSE;
    }

}