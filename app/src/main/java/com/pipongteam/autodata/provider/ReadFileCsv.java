package com.pipongteam.autodata.provider;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.pipongteam.autodata.R;

import java.io.File;
import java.io.IOException;
import java.io.FileReader;

public class ReadFileCsv {
    private static final String TAG = "ReadFileCsv";
    public Context mContext;
    public String mPatch;
    private DBManager dbManager;

    public ReadFileCsv(Context context, String patch) {
        mPatch = patch;
        mContext = context;
        dbManager = new DBManager(mContext).open();
    }

    public void readDataRaw() {
        try {
            CSVReader reader = new CSVReader(new FileReader(mPatch));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                Log.d(TAG, "nvtamcntt >> " + nextLine[0] + nextLine[1]);
                if (nextLine[0] != null && nextLine[1] != null && nextLine[0].length() == 16){
                    if (dbManager.getItemByCondition(nextLine[0], nextLine[1]) == null) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DatabaseHelper.POINT_NUMBER, nextLine[0]);
                        contentValues.put(DatabaseHelper.SECURITY_NUMBER, nextLine[1]);
                        dbManager.insert(contentValues);
                    }
                }
            }
            Toast.makeText(mContext, mContext.getString(R.string.download_finish), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            dbManager.close();
            Log.d(TAG, "nvtamcntt Error " + e.getMessage());
        }
    }
}
