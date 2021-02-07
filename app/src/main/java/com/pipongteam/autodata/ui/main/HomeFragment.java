package com.pipongteam.autodata.ui.main;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pipongteam.autodata.R;
import com.pipongteam.autodata.provider.ReadFileCsv;
import com.pipongteam.autodata.utils.PreferencesUtils;
import com.suke.widget.SwitchButton;

import java.io.File;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.pipongteam.autodata.utils.PreferencesUtils.PREFNAME;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private ImageView mPasteButton;
    private AppCompatButton mDownloadButton;
    private com.suke.widget.SwitchButton mSwitchButton;
    private EditText mUrl;
    private Context mContext;
    private String mFileName;
    private SharedPreferences mSharedPrefs;

    public HomeFragment updateParams(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        setArguments(bundle);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();

        mSharedPrefs = mContext.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE| Context.MODE_MULTI_PROCESS);
        View root = inflater.inflate(R.layout.fragment_upload_file, container, false);
        mPasteButton = root.findViewById(R.id.btn_paste);
        mDownloadButton = root.findViewById(R.id.download_button);
        mSwitchButton = root.findViewById(R.id.switchEnable);
        mPasteButton.setOnClickListener(this);
        mDownloadButton.setOnClickListener(this);

        mSwitchButton.setChecked(PreferencesUtils.getSharedPrefAppEnable(mContext));

        mSwitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                PreferencesUtils.setSharedPrefAppEnable(mContext, isChecked);
            }
        });

        mUrl = root.findViewById(R.id.etURL);
        mUrl.setText("");

        mContext.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(onComplete);
    }


    private long mDownloadedFileID;
    BroadcastReceiver onComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Prevents the occasional unintentional call. I needed this.
            if (mDownloadedFileID == -1)
                return;
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), mFileName);

            try {
                if(file.exists()){
                    mDownloadButton.setClickable(true);
                    mDownloadButton.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    mLink = "";
                    mUrl.setText("");
                    mReadFileCSV = new ReadFileCsv(mContext, file.getPath());
                    mReadFileCSV.readDataRaw();
                }
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mContext, "No handler for this type of file.",
                        Toast.LENGTH_LONG).show();
            }
            // Sets up the prevention of an unintentional call. I found it necessary. Maybe not for others.
            mDownloadedFileID = -1;
        }
    };

    private DownloadManager mDownloadManager;
    private ReadFileCsv mReadFileCSV;

    private void downloadFileProcess() {
        mFileName = System.currentTimeMillis() + "file.csv";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mLink));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("download");
        request.setDescription("Downloading ....");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mFileName);
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        mDownloadedFileID = mDownloadManager.enqueue(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    downloadFileProcess();
                } else {
                    Toast.makeText(mContext, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public String pasteUrl() {
        String textToPaste = "";

        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard.hasPrimaryClip()) {
            ClipData clip = clipboard.getPrimaryClip();

            // if you need text data only, use:
            if (clip.getDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))
                // WARNING: The item could cantain URI that points to the text data.
                // In this case the getText() returns null and this code fails!
                textToPaste = clip.getItemAt(0).getText().toString();

            // or you may coerce the data to the text representation:
            textToPaste = clip.getItemAt(0).coerceToText(mContext).toString();
        }

        if (!TextUtils.isEmpty(textToPaste)) {
            return textToPaste;
        }
        return "";
    }

    private String mLink = "";
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_paste:
                String url = pasteUrl();
                if (!url.equals("")) {
                    mUrl.setText(url);
                    mLink = url;
                    mDownloadButton.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    mDownloadButton.setClickable(true);
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.not_url), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.download_button:
                if (!"".equals(mLink)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permissions, 100);
                        } else {

                            mDownloadButton.setBackgroundColor(Color.GRAY);
                            mDownloadButton.setClickable(false);
                            downloadFileProcess();
                        }
                    } else {
                        mDownloadButton.setBackgroundColor(Color.GRAY);
                        mDownloadButton.setClickable(false);
                        downloadFileProcess();
                    }
                }
                break;

        }
    }
}