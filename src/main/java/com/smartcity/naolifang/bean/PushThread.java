package com.smartcity.naolifang.bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PushThread {

    public static class PushRunnable implements Runnable {

        // 创建线程池
        public static ExecutorService es = Executors.newCachedThreadPool();

        private Thread nowThread;
        private String rtspUrl;
        private String token;

        public PushRunnable(String rtspUrl, String token) {
            this.rtspUrl = rtspUrl;
            this.token = token;
        }

        // 中断线程
        public void setInterrupted() {
            nowThread.interrupt();
        }

        @Override
        public void run() {
//            nowThread = Thread.currentThread();
//
//            try {
//                FlvPusher pusher = new FlvPusher().from(rtspUrl).to(token);
//                pusher.go();
//            } catch (Exception e) {
//                this.setInterrupted();
//                e.printStackTrace();
//            }
        }
    }
}
