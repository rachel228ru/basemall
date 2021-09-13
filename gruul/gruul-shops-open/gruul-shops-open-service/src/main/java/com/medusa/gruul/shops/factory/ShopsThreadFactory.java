package com.medusa.gruul.shops.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @author create by zq
 * @date created in 2019/11/07
 */
public class ShopsThreadFactory implements ThreadFactory {

    private final String namePrefix;

    private final int priority;

    private final AtomicLong atomicLong = new AtomicLong(0);


    public ShopsThreadFactory(String namePrefix) {
        this(namePrefix, Thread.NORM_PRIORITY);
    }


    public ShopsThreadFactory(String namePrefix, int priority) {
        this.namePrefix = namePrefix;
        if (priority < Thread.MIN_PRIORITY) {
            this.priority = Thread.MIN_PRIORITY;
        } else if (priority > Thread.MAX_PRIORITY) {
            this.priority = Thread.MAX_PRIORITY;
        } else {
            this.priority = priority;
        }
    }


    @Override
    public Thread newThread(Runnable r) {
        String threadName = String.format("%s-%d", namePrefix, atomicLong.getAndIncrement());
        Thread thread = new Thread(r, threadName);
        thread.setDaemon(Boolean.FALSE);
        thread.setPriority(this.priority);
        return thread;
    }

}
