package com.pipongteam.autodata.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.pipongteam.autodata.R;
import com.pipongteam.autodata.entity.Info;
import com.pipongteam.autodata.provider.DBManager;
import com.pipongteam.autodata.provider.DatabaseHelper;
import com.pipongteam.autodata.ui.main.CustomCursonAdapter;
import com.pipongteam.autodata.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import static com.pipongteam.autodata.utils.PreferencesUtils.ENABLE_SERVICE;
import static com.pipongteam.autodata.utils.PreferencesUtils.PREFNAME;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class AccessibilityServiceHandle extends AccessibilityService {
    private String TAG = "nvtamcntt....";
    private String ROOT_APP = "multi.parallel.dualspace.cloner";
    private String SKIP_BUTTON_STEP1 = "jp.aeonretail.aeon.kidsrepublic:id/skip_view";

    private String SKIP_BUTTON_STEP2 = "jp.aeonretail.aeon.kidsrepublic:id/member_register_bt";
    private String SKIP_BUTTON_STEP2_TEXT = "jp.aeonretail.aeon.kidsrepublic:id/member_register_bt_text";
    private String SKIP_BUTTON_STEP3 = "jp.aeonretail.aeon.kidsrepublic:id/become_a_member_bt";
    private String SKIP_BUTTON_STEP4 = "jp.aeonretail.aeon.kidsrepublic:id/select_card_layout_3";
    private String CARD_POINT_NUMBER_STEP = "jp.aeonretail.aeon.kidsrepublic:id/member_card_number";
    private String CARD_SECURITY_NUMBER_STEP = "jp.aeonretail.aeon.kidsrepublic:id/member_security_code";
    private String NEXT_BUTTON = "jp.aeonretail.aeon.kidsrepublic:id/btnNext";
    private String CHECKBOX_SECURITY = "jp.aeonretail.aeon.kidsrepublic:id/agreement_checkbox";

    private String FINISH_BUTTON = "jp.aeonretail.aeon.kidsrepublic:id/thankyou_image";

    private String ERROR_BUTTON_ALERT = "カード情報を認識できません。";
//    private String mPointCode = "1000001650148194";
//    private String mSecurityCode = "068791";

    private boolean isFilledPointCode = false;
    private boolean isFilledSecurityCode = false;
    private boolean isCheckPolicy = false;

    private Context mContext;

    private boolean isEnableService = false;

    private Handler mHandler = new Handler();
    private Handler mLoadDataHandler = new Handler();

    private Info mInfo = null;
    private ArrayList<Info> mInfoList = new ArrayList<>();
    private boolean isNeedReloadData = true;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            isNeedReloadData = false;
            initDatabase();
            if (mInfoList.size() != 0) {
                mInfo = mInfoList.get(0);
            }
            Log.v(TAG, "Get data size " + mInfoList.size());
        }
    };

    private DBManager dbManager;

    private void initDatabase() {
        dbManager.open();
        mInfoList.clear();
        mInfoList = dbManager.fetchAllRecordRegisted();
        dbManager.close();
    }

    private void updateData() {
        dbManager.open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.IS_REGISTED, 1);
        int i = dbManager.update(mInfo.getId(), contentValues);
        dbManager.close();
    }

    private void updateErrorData() {
        dbManager.open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.IS_REGISTED, 2);
        int i = dbManager.update(mInfo.getId(), contentValues);
        dbManager.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

        if (accessibilityEvent != null && accessibilityEvent.getPackageName() != null) {
            int eventType = accessibilityEvent.getEventType();
            switch (eventType) {
                case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                    if (ROOT_APP.equals(accessibilityEvent.getPackageName())) {
                        Log.v(TAG, "TYPE_WINDOW_CONTENT_CHANGED ");
                    }
                case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:

                    if (ROOT_APP.equals(accessibilityEvent.getPackageName())) {
                        isEnableService = PreferencesUtils.getSharedPrefAppEnable(mContext);
                        if (isEnableService) {
                            if (isNeedReloadData) {
                                mLoadDataHandler.removeCallbacks(mRunnable);
                                mLoadDataHandler.postDelayed(mRunnable, 1000);
                            }
                            Log.v(TAG, "TYPE_WINDOW_STATE_CHANGED ");
                            AccessibilityNodeInfo source = accessibilityEvent.getSource();
                            if (source != null) {
                                source.refresh();
                                try {
                                    for (AccessibilityNodeInfo accessibilityNodeInfoElement : source.findAccessibilityNodeInfosByViewId(SKIP_BUTTON_STEP1)) {
                                        accessibilityNodeInfoElement.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                        Log.v(TAG, "SKIP_BUTTON_STEP1 ");
                                    }
                                } catch (Exception e) {
                                    Log.v(TAG, "ERROR  " + e.getMessage());
                                    e.printStackTrace();
                                }

                                try {
                                    List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId(SKIP_BUTTON_STEP2);
                                    if (!(findAccessibilityNodeInfosByViewId == null || findAccessibilityNodeInfosByViewId.isEmpty())) {
                                        for (AccessibilityNodeInfo accessibilityNodeInfo : findAccessibilityNodeInfosByViewId) {
                                            if (accessibilityNodeInfo.getChildCount() > 0) {
                                                AccessibilityNodeInfo node = accessibilityNodeInfo.getChild(0);
                                                String contentText = node.getText().toString();
                                                if (contentText.equals(mContext.getString(R.string.member_register_bt_text))) {
                                                    accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                                    Log.v(TAG, "SKIP_BUTTON_STEP 2 " + System.currentTimeMillis());
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.v(TAG, "ERROR  " + e.getMessage());
                                    e.printStackTrace();
                                }

                                try {
                                    List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId(SKIP_BUTTON_STEP3);
                                    if (!(findAccessibilityNodeInfosByViewId == null || findAccessibilityNodeInfosByViewId.isEmpty())) {
                                        for (AccessibilityNodeInfo accessibilityNodeInfo : findAccessibilityNodeInfosByViewId) {
                                            Log.v(TAG, "SKIP_BUTTON_STEP 3 " + System.currentTimeMillis());
//                                            mHandler.removeCallbacksAndMessages(null);

                                            mHandler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                                }
                                            }, 500);
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.v(TAG, "ERROR  " + e.getMessage());
                                    e.printStackTrace();
                                }
                                try {
                                    List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId(SKIP_BUTTON_STEP4);
                                    if (!(findAccessibilityNodeInfosByViewId == null || findAccessibilityNodeInfosByViewId.isEmpty())) {
                                        for (AccessibilityNodeInfo accessibilityNodeInfo : findAccessibilityNodeInfosByViewId) {

                                            accessibilityNodeInfo.performAction(16);
                                            Log.v(TAG, "SKIP_BUTTON_STEP 4 " + findAccessibilityNodeInfosByViewId.size());

                                        }
                                    }
                                } catch (Exception e) {
                                    Log.v(TAG, "ERROR  " + e.getMessage());
                                    e.printStackTrace();
                                }

                                try {
                                    List findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId(CARD_POINT_NUMBER_STEP);
                                    if (!(findAccessibilityNodeInfosByViewId == null || findAccessibilityNodeInfosByViewId.isEmpty())) {
                                        AccessibilityNodeInfo accessibilityNodeInfo = (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
                                        if (accessibilityNodeInfo.isClickable()) {
                                            if (mInfoList.size() != 0) {
                                                Bundle arguments = new Bundle();
                                                arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                                                        mInfo.getPointCode());
                                                accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                                                isFilledPointCode = true;
                                                Log.v(TAG, "SKIP_BUTTON_STEP 4 SET POINT ");
                                            }

                                        }

                                    }
                                } catch (Exception e) {
                                    Log.v(TAG, "ERROR  " + e.getMessage());
                                    e.printStackTrace();
                                }

                                try {
                                    List findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId(CARD_SECURITY_NUMBER_STEP);
                                    if (!(findAccessibilityNodeInfosByViewId == null || findAccessibilityNodeInfosByViewId.isEmpty())) {
                                        AccessibilityNodeInfo accessibilityNodeInfo = (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
                                        if (mInfoList.size() != 0) {
                                            Bundle arguments = new Bundle();
                                            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                                                    mInfo.getSecurityCode());
                                            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                                            isFilledSecurityCode = true;
                                            Log.v(TAG, "SKIP_BUTTON_STEP  4 SET SECURITY ");
                                        }

                                    }
                                } catch (Exception e) {
                                    Log.v(TAG, "ERROR  " + e.getMessage());
                                    e.printStackTrace();
                                }

                                try {
                                    List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByText(ERROR_BUTTON_ALERT);
                                    if (findAccessibilityNodeInfosByViewId.size() > 0) {
                                        mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                updateErrorDatabase();
                                                initDatabase();
                                                if (mInfoList.size() != 0) {
                                                    mInfo = mInfoList.get(0);
                                                }
                                            }
                                        });

                                    }
                                } catch (Exception e) {
                                    Log.v(TAG, "ERROR  " + e.getMessage());
                                    e.printStackTrace();
                                }

                                if (isFilledSecurityCode && isFilledPointCode) {
                                    isFilledSecurityCode = false;
                                    isFilledPointCode = false;
                                    clickNextButton(source, getString(R.string.member_register_bt_member_info));
                                }
                                /* click next button after when input point code and */
                                clickNextButton(source, getString(R.string.tutorial_member_register));
                                clickNextButton(source, getString(R.string.member_register_bt_followshop));
                                try {
                                    List findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId(CHECKBOX_SECURITY);
                                    if (!(findAccessibilityNodeInfosByViewId == null || findAccessibilityNodeInfosByViewId.isEmpty())) {
                                        AccessibilityNodeInfo accessibilityNodeInfo = (AccessibilityNodeInfo) findAccessibilityNodeInfosByViewId.get(0);
                                        if (!isCheckPolicy) {
                                            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                            isCheckPolicy = true;
                                            Log.v(TAG, "CHECK BOX SECURITY ");
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.v(TAG, "ERROR  " + e.getMessage());
                                    e.printStackTrace();
                                }

                                if (isCheckPolicy) {
                                    clickNextButton(source, getString(R.string.member_register_bt_register));
                                }

                                clickNextButton(source, getString(R.string.member_register_1_bt));

                                finishRegisterAction(source);
                                /* reload */
                                accessibilityEvent.recycle();
                            }
                        }
                    }
                    break;
            }

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void updateErrorDatabase() {
        try {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    updateErrorData();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void finishRegisterAction(AccessibilityNodeInfo source) {
        try {
            List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId(FINISH_BUTTON);
            if (!(findAccessibilityNodeInfosByViewId == null || findAccessibilityNodeInfosByViewId.isEmpty())) {
                isCheckPolicy = false;
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        updateData();
                        isNeedReloadData = true;
                    }
                });
                // TODO save database

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void clickNextButton(AccessibilityNodeInfo source, String label) {
        try {
            List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId = source.findAccessibilityNodeInfosByViewId(NEXT_BUTTON);
            if (!(findAccessibilityNodeInfosByViewId == null || findAccessibilityNodeInfosByViewId.isEmpty())) {
                for (AccessibilityNodeInfo accessibilityNodeInfo : findAccessibilityNodeInfosByViewId) {
                    String contentText = accessibilityNodeInfo.getText().toString();
                    if (contentText.equals(label)) {
                        if (label.equals(getString(R.string.member_register_bt_followshop))) {
//                            mHandler.removeCallbacksAndMessages(null);
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                }
                            }, 3000);
                        } else if (label.equals(getString(R.string.member_register_bt_followshop))) {
                            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        } else {
                            accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        }

                        Log.v(TAG, "NEXT BUTTON CLICK");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInterrupt() {
    }


    @Override
    public void onServiceConnected() {
        Log.v(TAG, "onServiceConnected");
        mContext = getApplicationContext();

        AccessibilityServiceInfo accessibilityServiceInfo = new AccessibilityServiceInfo();
        accessibilityServiceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

        accessibilityServiceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        accessibilityServiceInfo.flags = 64;
        accessibilityServiceInfo.notificationTimeout = 0;
        setServiceInfo(accessibilityServiceInfo);

        dbManager = new DBManager(mContext);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.v(TAG, "onCreate");
        mContext = getApplicationContext();
    }
}
