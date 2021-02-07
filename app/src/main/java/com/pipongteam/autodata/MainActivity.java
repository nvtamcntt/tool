package com.pipongteam.autodata;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.pipongteam.autodata.ui.main.SectionsPagerAdapter;
import com.pipongteam.autodata.service.AccessibilityServiceHandle;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "nvtamcntt >>> ";
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1111;
    public static int ACTION_ACCESSIBILITY_RESULT_CODE = 100;
    private SectionsPagerAdapter mSectionsPageAdapter;
    ImageView mAppButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSectionsPageAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        mAppButton = findViewById(R.id.fab);
        mAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoApp();
            }
        });
        viewPager.setAdapter(mSectionsPageAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 1:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mSectionsPageAdapter.getListPage().reloadData();
                            }
                        }, 100);
                        viewPager.setCurrentItem(tab.getPosition());
                        Log.d("TAG", "nvtamcntt >>> onChanged tab" + tab.getPosition());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        if (!isAccessibilitySettingsOn()) {
            startActivityForResult(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS), ACTION_ACCESSIBILITY_RESULT_CODE);
        }
    }

    private boolean isAccessibilitySettingsOn() {
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + AccessibilityServiceHandle.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
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

    private void gotoApp() {
        Intent launchIntent = null;
        try {
            launchIntent = getPackageManager().getLaunchIntentForPackage("multi.parallel.dualspace.cloner");
        } catch (Exception ignored) {
        }

        if (launchIntent == null) {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://play.google.com/store/apps/details?id=" + "multi.parallel.dualspace.cloner")));
        } else {
            startActivity(launchIntent);
        }
    }
}