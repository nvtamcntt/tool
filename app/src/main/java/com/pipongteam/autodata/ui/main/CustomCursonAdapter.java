package com.pipongteam.autodata.ui.main;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.pipongteam.autodata.R;
import com.pipongteam.autodata.provider.DatabaseHelper;


public class CustomCursonAdapter extends CursorAdapter {
    private Context mContext;
    private Cursor cursor;
    private final LayoutInflater inflater;

    public CustomCursonAdapter(Context context, Cursor cursor, int flag) {
        super(context, cursor, flag);
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.cursor = cursor;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView id = view.findViewById(R.id.id);
        TextView secutity_code_id = view.findViewById(R.id.secutity_code_id);
        TextView point_code_id = view.findViewById(R.id.point_code_id);
        TextView is_registed = view.findViewById(R.id.is_registed);
        int ids = cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID));

        id.setText(ids + "");
        secutity_code_id.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.SECURITY_NUMBER)));
        point_code_id.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.POINT_NUMBER)));
        is_registed.setText(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.IS_REGISTED)) == 1 ? mContext.getString(R.string.registed) : mContext.getString(R.string.not_register));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.activity_view_record, parent, false);
    }


}
