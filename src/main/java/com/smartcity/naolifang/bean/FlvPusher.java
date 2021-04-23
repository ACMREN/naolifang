package com.smartcity.naolifang.bean;

public class FlvPusher {
//    private String rtmpUrl = "rtmp://localhost:1935/live/";
//
//    private FFmpegFrameGrabber grabber;
//    private FFmpegFrameRecorder recorder;
//
//    private Integer imageWidth = 0;
//    private Integer imageHeight = 0;
//
//    public FlvPusher from(String rtspUrl) {
//        grabber = new FFmpegFrameGrabber(rtspUrl);
//
//        grabber.setVideoCodec(avcodec.AV_CODEC_ID_H264);
//        grabber.setOption("rtsp_transport", "tcp");
//        grabber.setOption("y", "");
//        grabber.setOption("stimeout", "2000000");
//
//        avutil.av_log_set_level(avutil.AV_LOG_ERROR);
//        FFmpegLogCallback.set();
//        try {
//            grabber.start();
//        } catch (FrameGrabber.Exception ex) {
//            try {
//                grabber.stop();
//                grabber.close();
//            } catch (FrameGrabber.Exception ex1) {
//                ex1.printStackTrace();
//            }
//            ex.printStackTrace();
//        }
//        imageHeight = grabber.getImageHeight();
//        imageWidth = grabber.getImageWidth();
//
//        return this;
//    }
//
//    public FlvPusher to(String token) {
//        rtmpUrl += token;
//        recorder = new FFmpegFrameRecorder(rtmpUrl, imageWidth, imageHeight);
//
//        recorder.setFormat("flv");
//        AVFormatContext fc = null;
//        fc = grabber.getFormatContext();
//
//        try {
//            recorder.start(fc);
//        } catch (FrameRecorder.Exception ex) {
//            try {
//                recorder.stop();
//                recorder.close();
//                grabber.stop();
//                grabber.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            ex.printStackTrace();
//        }
//        return this;
//    }
//
//    public String go() throws Exception {
//        grabber.flush();
//        while (true) {
//            AVPacket packet;
//            packet = grabber.grabPacket();
//            if (null == packet || packet.size() == 0) {
//                continue;
//            }
//
//            recorder.recordPacket(packet);
//        }
//    }
}
