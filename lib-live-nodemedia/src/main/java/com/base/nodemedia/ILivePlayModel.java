package com.base.nodemedia;


import android.view.View;

import com.base.live.model.IBaseLive;


public interface ILivePlayModel extends IBaseLive {

    /**
     * 创建播放
     * 属性 playView
     * 属性 mixBuffer
     * 属性 maxBuffer
     */
    void onCreate(View playView, int mixBuffer, int maxBuffer);


    /**
     * 获取渲染的view
     * @return
     */
    View getRenderView();
}
