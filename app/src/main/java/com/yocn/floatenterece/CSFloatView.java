package com.yocn.floatenterece;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class CSFloatView {
    private Context mContext;
    private View floatView;
    private WindowManager mManager;
    private WindowManager.LayoutParams params;
    private LinearLayout leftLayout, ll_csfloat_hint, ll_csfloat_right_hint;
    private ImageView imageview;
    private int mScreenWidth, mScreenHeigh;
    private boolean isDowned = false;
    private int mViewWidth, mViewheight;
    private static boolean existView = false;
    private TextView mTxtAccount, mTxtService, mTxtGame, mTxtHide, mfloatHide;
    private boolean isRegister = false;
    private Timer timer;
    private boolean isLeft = true;

    public CSFloatView(Context context, boolean register) {
        mContext = context;
        isRegister = register;
        initView();
    }

    public void setIsRegister(boolean register) {
        isRegister = register;
        if (isRegister) {
            setTimer();
        }
    }

    private void initView() {
        if (existView) {
            return;
        }
        mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        mScreenHeigh = mContext.getResources().getDisplayMetrics().heightPixels;
        mManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.LAST_APPLICATION_WINDOW,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,// FLAG_LAYOUT_NO_LIMITS使x轴可以超出手机屏幕外
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT | Gravity.CENTER_VERTICAL;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //需要根据横竖屏处理
        params.x = 0;
//      params.y=mScreenHeigh/2-100;
        params.y = 100;
        findViewByid(1);
        mHandler.postDelayed(run, 2000);
    }

    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private int delay;

    class ImageviewOnTouch implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    initialX = params.x;
                    initialY = params.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    mHandler.removeCallbacks(run);
                    break;

                case MotionEvent.ACTION_UP:
                    if (!isDowned) {
                        if (Math.abs((event.getRawX() - initialTouchX)) > 10) {
                            Log.e("tag", "移动：" + (event.getRawX() - initialTouchX));
                            if (initialX < 0) {
                                params.x = 0;
                                mManager.updateViewLayout(floatView, params);
                            }
                            if (initialX > mScreenWidth - mViewWidth) {
                                params.x = mScreenWidth - mViewWidth;
                                mManager.updateViewLayout(floatView, params);
                            }
                            if (params.y < 0) {
                                params.y = 0;
                            }
                            if (params.y + mViewheight >= mScreenHeigh) {
                                params.y = mScreenHeigh - mViewheight;
                            }
                            mHandler.postDelayed(run, 2000);
                        } else {
                            params.width = mViewWidth * 5;
                            if (params.x > mScreenWidth / 2) {
                                isLeft = false;
                                findViewByid(1);
                            } else {
                                isLeft = true;
                                if (params.x < 0) {
                                    params.x = 0;
                                }
                                findViewByid(1);
                            }
                            delay = params.x;
                            isDowned = false;
                            //弹出菜单界面
                            mContext.startActivity(new Intent(mContext, MainActivity.class));
                            mManager.updateViewLayout(floatView, params);
                            mHandler.postDelayed(run, 2000);
                        }
                    } else {
                        mHandler.removeCallbacks(run);
                        isDowned = false;
                        if (!isLeft) {
                            leftLayout.setVisibility(View.GONE);
                        } else {
                            leftLayout.setVisibility(View.GONE);
                            params.width = mViewWidth;
                        }
                        if (delay >= (mScreenWidth - mViewWidth / 2)) {
                            params.x = mScreenWidth - mViewWidth;
                        } else {
                            params.x = delay;
                        }

                        mManager.updateViewLayout(floatView, params);
                        mHandler.postDelayed(run, 2000);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isDowned) {
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mManager.updateViewLayout(floatView, params);
                    }
                    break;
            }
            return true;
        }
    }

    private void updateView() {
        if (params.x > (mScreenWidth / 2)) {
            params.x = (mScreenWidth - mViewWidth / 3);
            params.alpha = 0.5f;
            mManager.updateViewLayout(floatView, params);
        } else {
            params.x = (mViewWidth / 3) - mViewWidth;
            params.alpha = 0.5f;
            mManager.updateViewLayout(floatView, params);
        }
        params.alpha = 1f;
    }

    private Handler mHandler = new Handler();
    Runnable run = new Runnable() {

        @Override
        public void run() {
            updateView();
        }
    };

    @SuppressLint("Range")
    private void findViewByid(int type) {
        if (floatView != null) {
            mManager.removeView(floatView);
        }
        floatView = LayoutInflater.from(mContext).inflate(R.layout.cs_floatview_left, null, false);
        leftLayout = (LinearLayout) floatView.findViewById(R.id.ll_floatview_left_extends);
        ll_csfloat_hint = (LinearLayout) floatView.findViewById(R.id.ll_csfloat_hint);
        ll_csfloat_right_hint = (LinearLayout) floatView.findViewById(R.id.ll_csfloat_right_hint);

        floatView.setAlpha(100);
        imageview = (ImageView) floatView.findViewById(R.id.iv_floatview_left);
        leftLayout.setVisibility(View.GONE);
        ll_csfloat_hint.setVisibility(View.GONE);
        imageview.setOnTouchListener(new ImageviewOnTouch());
        imageview.getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mViewWidth = imageview.getWidth();
                        mViewheight = imageview.getHeight();
                        imageview.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });
        mManager.addView(floatView, params);
        setOnClickListener();
        existView = true;
        if (isRegister) {
            setTimer();
        }
    }

    private void setOnClickListener() {
        mTxtAccount = (TextView) floatView.findViewById(R.id.txt_floatmenu_account);
        mTxtService = (TextView) floatView.findViewById(R.id.txt_floatmenu_service);
        mTxtGame = (TextView) floatView.findViewById(R.id.txt_floatmenu_game);
        mTxtHide = (TextView) floatView.findViewById(R.id.txt_floatmenu_hide);
        TextViewOnClick ciickLitener = new TextViewOnClick();
        mTxtAccount.setOnClickListener(ciickLitener);
        mTxtGame.setOnClickListener(ciickLitener);
        mTxtHide.setOnClickListener(ciickLitener);
        mTxtService.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(mContext, "客服", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class TextViewOnClick implements OnClickListener {

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "" + v.getId(), Toast.LENGTH_SHORT).show();
        }
    }

    public void hideFloatView() {
        if (mManager != null && floatView != null) {
            mHandler.removeCallbacks(run);
            mManager.removeViewImmediate(floatView);
            mManager = null;
            existView = false;
        }
    }

    private void setTimer() {
        timerhandler.removeMessages(1);
        timerhandler.removeMessages(2);
        if (timer == null) {
            timer = new Timer();
        } else {
            timer.cancel();
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                timerhandler.sendEmptyMessage(1);
            }
        }, 5000);
    }

    private final Handler timerhandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (leftLayout != null && leftLayout.getVisibility() == View.GONE && ll_csfloat_hint != null && ll_csfloat_hint.getVisibility() == View.GONE) {
                    ll_csfloat_hint.setVisibility(View.VISIBLE);
                    timerhandler.sendEmptyMessageDelayed(2, 3000);
                    isRegister = false;
                }
            } else if (msg.what == 2) {
                ll_csfloat_hint.setVisibility(View.GONE);
            }
        }
    };
}