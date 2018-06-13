package com.base.nodemedia;


import android.graphics.Bitmap;
import android.view.View;


import com.base.live.model.BaseLiveModel;

import java.io.File;
import java.io.FileOutputStream;

import cn.nodemedia.NodeCameraView;
import cn.nodemedia.NodePublisher;
import cn.nodemedia.NodePublisherDelegate;

/**
 *
 * 推流
 */

public class LivePushModel extends BaseLiveModel implements NodePublisherDelegate, ILivePushModel {
    private boolean isStarting = false;
    private NodePublisher np;
    private NodeCameraView npv;
    @Override
    public void capturePicture(final String capFilePath) {
        np.capturePicture(new NodePublisher.CapturePictureListener() {
            @Override
            public void onCaptureCallback(Bitmap bitmap) {
                if (bitmap == null) {
                    mCallBackHandler.sendEmptyMessage(2103);
                    return;
                }
                try {

                    File SavePath = new File(capFilePath);
                    FileOutputStream out = new FileOutputStream(SavePath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                    out.close();
                    bitmap.recycle();
                    mCallBackHandler.sendEmptyMessage(2102);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onCreate(View playView) {
        isStarting = false;
        npv = (NodeCameraView) playView;
        np = new NodePublisher(mActivity);
        np.setNodePublisherDelegate(this);
        np.setCameraPreview(npv, NodePublisher.CAMERA_FRONT, true);
        np.setAudioParam(32 * 1000, NodePublisher.AUDIO_PROFILE_HEAAC);
        np.setVideoParam(NodePublisher.VIDEO_PPRESET_16X9_360, 24, 500 * 1000, NodePublisher.VIDEO_PROFILE_MAIN, false);
        np.setDenoiseEnable(true);
        np.setBeautyLevel(3);
//        np.setOutputUrl(pubUrl);
        /**
         * @brief rtmpdump 风格的connect参数
         * Append arbitrary AMF data to the Connect message. The type must be B for Boolean, N for number, S for string, O for object, or Z for null.
         * For Booleans the data must be either 0 or 1 for FALSE or TRUE, respectively. Likewise for Objects the data must be 0 or 1 to end or begin an object, respectively.
         * Data items in subobjects may be named, by prefixing the type with 'N' and specifying the name before the value, e.g. NB:myFlag:1.
         * This option may be used multiple times to construct arbitrary AMF sequences. E.g.
         */
        np.setConnArgs("S:info O:1 NS:uid:10012 NB:vip:1 NN:num:209.12 O:0");
        np.startPreview();
    }
    @Override
    public void onDestroy() {
        np.stopPreview();
        np.stop();
        np.release();
    }
    @Override
    public void start(String url) {
        np.setOutputUrl(url);
        np.start();

    }
    @Override
    public void stop() {
        np.stop();
    }
    @Override
    public void setAudioEnable(boolean enable) {
        if (isStarting) {

            np.setAudioEnable(enable);
            if (enable) {
                mCallBackHandler.sendEmptyMessage(3101);
            } else {
                mCallBackHandler.sendEmptyMessage(3100);
            }
        }
    }
    @Override
    public void setVideoEnable(boolean enable) {
        np.setVideoEnable(enable);
        if (enable) {
            mCallBackHandler.sendEmptyMessage(3103);
        } else {
            mCallBackHandler.sendEmptyMessage(3102);
        }
    }
    @Override
    public void setFlsh(boolean isOpen) {

        np.setFlashEnable(isOpen);

    }
    @Override
    public void reStar(String url) {

        np.stop();
        np.setOutputUrl(url);
        np.start();


    }
    @Override
    public void switchCamera() {
        np.switchCamera();
        np.setFlashEnable(false);
    }
    @Override
    public void onEventCallback(NodePublisher nodePublisher, int event, String msg) {
        mCallBackHandler.sendEmptyMessage(event);
    }

//    private Handler handler = new Handler() {
//        // 回调处理
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 2000:
////                    Toast.makeText(LivePublisherDemoActivity.this, "正在发布视频", Toast.LENGTH_SHORT).show();
//                    break;
//                case 2001:
////                    Toast.makeText(LivePublisherDemoActivity.this, "视频发布成功", Toast.LENGTH_SHORT).show();
//                    isStarting = true;
//                    break;
//                case 2002:
////                    Toast.makeText(LivePublisherDemoActivity.this, "视频发布失败", Toast.LENGTH_SHORT).show();
//                    break;
//                case 2004:
////                    Toast.makeText(LivePublisherDemoActivity.this, "视频发布结束", Toast.LENGTH_SHORT).show();
//                    isStarting = false;
//                    break;
//                case 2005:
////                    Toast.makeText(LivePublisherDemoActivity.this, "网络异常,发布中断", Toast.LENGTH_SHORT).show();
//                    break;
//                case 2100:
//                    // 发布端网络阻塞，已缓冲了2秒的数据在队列中
////                    Toast.makeText(LivePublisherDemoActivity.this, "网络阻塞，发布卡顿", Toast.LENGTH_SHORT).show();
//                    break;
//                case 2101:
//                    // 发布端网络恢复畅通
////                    Toast.makeText(LivePublisherDemoActivity.this, "网络恢复，发布流畅", Toast.LENGTH_SHORT).show();
//                    break;
//                case 2102:
////                    Toast.makeText(LivePublisherDemoActivity.this, "截图保存成功", Toast.LENGTH_SHORT).show();
//                    break;
//                case 2103:
////                    Toast.makeText(LivePublisherDemoActivity.this, "截图保存失败", Toast.LENGTH_SHORT).show();
//                    break;
//                case 2104:
////                    Toast.makeText(LivePublisherDemoActivity.this, "网络阻塞严重,无法继续推流,断开连接", Toast.LENGTH_SHORT).show();
//                    break;
//                case 2300:
////                    Toast.makeText(LivePublisherDemoActivity.this, "摄像头和麦克风都不能打开,用户没有给予访问权限或硬件被占用", Toast.LENGTH_SHORT).show();
//                    break;
//                case 2301:
////                    Toast.makeText(LivePublisherDemoActivity.this, "麦克风无法打开", Toast.LENGTH_SHORT).show();
//                    break;
//                case 2302:
////                    Toast.makeText(LivePublisherDemoActivity.this, "摄像头无法打开", Toast.LENGTH_SHORT).show();
//                    break;
//                case 3100:
//                    // mic off
////                    Toast.makeText(LivePublisherDemoActivity.this, "麦克风静音", Toast.LENGTH_SHORT).show();
//                    break;
//                case 3101:
//                    // mic on
////                    Toast.makeText(LivePublisherDemoActivity.this, "麦克风恢复", Toast.LENGTH_SHORT).show();
//                    break;
//                case 3102:
//                    // camera off
////                    Toast.makeText(LivePublisherDemoActivity.this, "摄像头传输关闭", Toast.LENGTH_SHORT).show();
//                    break;
//                case 3103:
//                    // camera on
////                    Toast.makeText(LivePublisherDemoActivity.this, "摄像头传输打开", Toast.LENGTH_SHORT).show();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
}
