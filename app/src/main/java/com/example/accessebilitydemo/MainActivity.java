package com.example.accessebilitydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int ACCESSIBILITY_REQUEST_CODE = 124;
    public static final int REQUEST_OVERLAY_PERMISSION = 121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
     private void init()
     {
         if (hasOverlayPermission()) {
             if (!isAccessibilitySettingsOn(this)) {
                 Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                 this.startActivityForResult(intent, ACCESSIBILITY_REQUEST_CODE);
             }
         } else {
             requestOverlayPermission();
         }

     }
    private boolean hasOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this);
        }
        return true;
    }
    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
        }
    }

     private boolean isAccessibilitySettingsOn(Context context){
         int accessibilityEnabled = 0;
         String service = context.getPackageName() + "/" + BlockSearchKeywordService.class.getCanonicalName();

         try {
             accessibilityEnabled = Settings.Secure.getInt(
                     getContentResolver(),
                     Settings.Secure.ACCESSIBILITY_ENABLED);
         } catch (Settings.SettingNotFoundException ignored) {
         }
         TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
         if (accessibilityEnabled == 1) {
             String settingValue = Settings.Secure.getString(getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
             if (settingValue != null) {
                 mStringColonSplitter.setString(settingValue);
                 while (mStringColonSplitter.hasNext()) {
                     String accessibilityService = mStringColonSplitter.next();
                     if (accessibilityService.equalsIgnoreCase(service)) {
                         return true;
                     }
                 }
                 }
         }
         return false;

     }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (hasOverlayPermission()) {
                if (!isAccessibilitySettingsOn(this)) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    this.startActivityForResult(intent, ACCESSIBILITY_REQUEST_CODE);
                }
            } else {
                Toast.makeText(this, "Overlay permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}