package com.yocn.floatenterece;

import android.content.Context;

public class FloatMenuGiftbag {
    private CSFloatView mGiftBagFloatView;
    private boolean register = false;

    private FloatMenuGiftbag() {

    }

    private volatile static FloatMenuGiftbag mFloatMenuGiftbag = null;

    public static FloatMenuGiftbag getInstance() {
        if (mFloatMenuGiftbag == null) {
            synchronized (FloatMenuGiftbag.class) {
                if (mFloatMenuGiftbag == null) {
                    mFloatMenuGiftbag = new FloatMenuGiftbag();
                }
            }
        }
        return mFloatMenuGiftbag;
    }

    public void showFloatMenu(final Context context) {
        if (mGiftBagFloatView == null) {
            mGiftBagFloatView = new CSFloatView(context, register);
        }
        mGiftBagFloatView.setIsRegister(register);
    }

    public void hideFloatMenu() {
        if (mGiftBagFloatView != null) {
            mGiftBagFloatView.hideFloatView();
            mGiftBagFloatView = null;
        }
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

    public void destroyFloatView() {
        if (mGiftBagFloatView != null) {
            mGiftBagFloatView.hideFloatView();
            mGiftBagFloatView = null;
        }
    }

}
