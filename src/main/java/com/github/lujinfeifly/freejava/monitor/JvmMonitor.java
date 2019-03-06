package com.github.lujinfeifly.freejava.monitor;

import com.github.lujinfeifly.freejava.monitor.jvm.JvmStatus;
import org.springframework.beans.factory.InitializingBean;

/**
 * Jvm 的监控类，目前该类为一个单独的线程。每隔一段时间进行收集JVM信息
 *
 */
public class JvmMonitor implements InitializingBean{

    private long heartbeatTime = 5*60*1000;

    private Thread monitorThread;

    private volatile boolean isStop = true;


    private Runnable runnable = new Runnable() {
        public void run() {
            while(isStop) {
                try {
                    JvmStatus.print();
                    Thread.sleep(heartbeatTime);
                } catch (InterruptedException e) {
                }
            }
        }
    };


    public void afterPropertiesSet() throws Exception {
        if(monitorThread == null) {
            monitorThread = new Thread(runnable);
            monitorThread.setName("JvmMonitor thread.");
            monitorThread.setDaemon(true);
            monitorThread.start();
        }
    }

    public long getHeartbeatTime() {
        return heartbeatTime;
    }

    public void setHeartbeatTime(long heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
    }
}
