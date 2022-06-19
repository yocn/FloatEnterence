package com.yocn.floatenterece;

import android.content.Context;

public class FloatMenuManager {

    //  private FloatMenu  mFloatMenu ;
    private CSFloatView mfloatview;
    private boolean register = false;

    private FloatMenuManager() {

    }

    private volatile static FloatMenuManager mFloatMenuManager = null;

    public static FloatMenuManager getInstance() {
        if (mFloatMenuManager == null) {
            synchronized (FloatMenuManager.class) {
                if (mFloatMenuManager == null) {
                    mFloatMenuManager = new FloatMenuManager();
                }
            }
        }
        return mFloatMenuManager;
    }

    public void showFloatMenu(final Context context) {
        if (mfloatview == null) {
            mfloatview = new CSFloatView(context, register);
        }
        mfloatview.setIsRegister(register);
    }

    public void hideFloatMenu() {
        if (mfloatview != null) {
            mfloatview.hideFloatView();
            mfloatview = null;
        }
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

    public void destroyFloatView() {
        if (mfloatview != null) {
            mfloatview.hideFloatView();
            mfloatview = null;
        }
    }

}
