package com.base.nodemedia;


import com.base.live.model.IBaseLive;

public interface ILivePushModel extends IBaseLive {



    /**
     * 闪光灯
     * 属性 isOpen
     */
    void setFlsh(boolean isOpen);

    /**
     * 摄像头
     */
    void switchCamera();

}
