package mmbialas.pl.workday.ui.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import mmbialas.pl.workday.R;
import mmbialas.pl.workday.database.DBManager;

/**
 * Created by Michal Bialas on 19/07/14.
 */
public class FragmentThree extends Fragment {


    @InjectView(R.id.list_items)
    ListView listItems;

    private Context mContext;
    private SimpleAdapter listItemAdapter = null;
    ArrayList<HashMap<String, Object>> listData;
    private DBManager dbManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        // 初始化DBManager
        dbManager = new DBManager(mContext);
        getAllData();

        View view = inflater.inflate(R.layout.fragment_outline, containter, false);
        Log.d("FragmentThree", " listData " + listData);
        listItemAdapter = new SimpleAdapter(mContext,
                listData,// 数据源
                R.layout.outline_list_item,// ListItem的XML实现
                // 动态数组与ImageItem对应的子项
                new String[]{"accountId", "date", "info"},
                // ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.accountId, R.id.expireDate, R.id.accountInfo});
        listItems.setAdapter(listItemAdapter);
        //listItems.setOnCreateContextMenuListener(listviewLongPress);

        ButterKnife.inject(this, view);


        return view;
    }

    public void getAllData() {
        Cursor c = dbManager.queryTheCursor();
        int columnsSize = c.getColumnCount();
        Log.d("FragmentThree", " columnsSize " + columnsSize);
        listData = new ArrayList<HashMap<String, Object>>();
        // 获取表的内容
        while (c.moveToNext()) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            //for (int i = 0; i < columnsSize; i++) {
            map.put("id", c.getString(0));
            map.put("accountId", c.getString(1));
            map.put("date", c.getString(2));
            map.put("info", c.getString(3));
            //}
            listData.add(map);

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }



}
