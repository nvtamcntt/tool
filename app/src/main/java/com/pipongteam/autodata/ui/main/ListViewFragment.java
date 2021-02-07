package com.pipongteam.autodata.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.pipongteam.autodata.R;
import com.pipongteam.autodata.provider.DBManager;

import static com.pipongteam.autodata.utils.PreferencesUtils.PREFNAME;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListViewFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    private Context mContext;
    private SharedPreferences mSharedPrefs;
    public ListViewFragment update(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        this.setArguments(bundle);
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

        final View root = inflater.inflate(R.layout.fragment_list_data, container, false);
        initDatabase(root);
        pageViewModel.getText().observe(this, new Observer<String>() {

            @Override
            public void onChanged(@Nullable String s) {
                reloadData();
            }
        });
        return root;
    }


    private DBManager dbManager;
    private CustomCursonAdapter mAdapter;

    private ListView mListView;
    private void initDatabase (View view) {
        dbManager = new DBManager(mContext);
        dbManager.open();
        Cursor cursor = dbManager.fetch();
        mListView = view.findViewById(R.id.list_view);
        mListView.setEmptyView(view.findViewById(R.id.empty));
        mAdapter = new CustomCursonAdapter(mContext, cursor, 0);
        mAdapter.notifyDataSetChanged();
        mListView.setAdapter(mAdapter);
    }

    public void reloadData() {
        Cursor cursor = dbManager.fetch();
        mAdapter = new CustomCursonAdapter(mContext, cursor, 0);
        mAdapter.notifyDataSetChanged();
        mListView.setAdapter(mAdapter);
    }
}