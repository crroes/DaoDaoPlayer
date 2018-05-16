package com.cross.fxwiz_widget_player.utils;

import android.view.View;

/*
 * Copyright (C) 2010-2017 Alibaba Group Holding Limited.
 */
public abstract class OnClickNoMutiListener implements View.OnClickListener {

    private long lastClickTime = 0;
    private final int SPACE_TIME = 500;


    public boolean isMultipleClick() {
        long currentTime = System.currentTimeMillis();
        boolean isDoubleClick;
        isDoubleClick = currentTime - lastClickTime <= SPACE_TIME;
        lastClickTime = currentTime;
        return isDoubleClick;
    }
}
