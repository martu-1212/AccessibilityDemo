package com.example.accessebilitydemo;

import android.accessibilityservice.AccessibilityService;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import kotlin.text.StringsKt;

public class BlockSearchKeywordService extends AccessibilityService{

    int mDebugDepth = 0;
    private OverlayManager overlayManager;

    private AppCompatTextView tvClose;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo source = event.getSource();
        mDebugDepth = 0;
        overlayManager = new OverlayManager(this);
        printAllViews(source);
    }

    private void printAllViews(AccessibilityNodeInfo mNodeInfo) {
        if (mNodeInfo != null) {
//            String screenText = (new StringBuilder()).append("(").append(mNodeInfo.getText()).append(" <-- ").append(mNodeInfo.getPackageName()).append(")").toString();
//            Log.e("AccessibilityClickService", screenText);
            if (mNodeInfo.getText() != null && mNodeInfo.getPackageName() != null && mNodeInfo.getText().toString().equals("abcde") && mNodeInfo.getPackageName().toString().equals("com.google.android.youtube")) {
                showOverlay();
            }
        }
    }

    private void navigateToYouTubeHomepage() {
        hideOverlay();
        Intent intent = new Intent(getPackageManager().getLaunchIntentForPackage("com.google.android.youtube"));
        startActivity(intent);
    }

    @Override
    public void onInterrupt() {

    }
    public void showOverlay() {
        overlayManager.createOverlayView();
        tvClose = overlayManager.getCloseview();
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    navigateToYouTubeHomepage();
                } catch (ActivityNotFoundException e) {
                }
            }
        });
    }

    public void hideOverlay() {
        overlayManager.removeOverlayView();
    }

}
