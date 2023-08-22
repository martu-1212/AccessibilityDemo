package com.example.accessebilitydemo;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatTextView;

public class OverlayManager{

    private Context mContext;
    private WindowManager mWindowManager;
    private View mOverlayView;

    public OverlayManager(Context context) {
        mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    public void createOverlayView() {
        if (mOverlayView == null) {
            // Inflate your overlay layout or create the view programmatically
            mOverlayView = LayoutInflater.from(mContext).inflate(R.layout.dialog_keyword, null);

            // Set layout parameters
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                getOverlayType(),
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            );

            params.gravity = Gravity.CENTER;

            // Add the view to the window manager
            mWindowManager.addView(mOverlayView, params);
        }
    }

    public void removeOverlayView() {
        if (mOverlayView != null) {
            mWindowManager.removeView(mOverlayView);
            mOverlayView = null;
        }
    }

    private int getOverlayType() {
        // Use TYPE_APPLICATION_OVERLAY for Android 8.0 and above, and TYPE_SYSTEM_ALERT for older versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            return WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
    }


    public AppCompatTextView getCloseview(){
        return mOverlayView.findViewById(R.id.tvClose);
    }
}
