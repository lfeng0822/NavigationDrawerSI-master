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
        ButterKnife.inject(this, view);
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

//    // 删除一条数据
//    public boolean delete(SQLiteDatabase mDb, String table, int id) {
//        String whereClause = "id=?";
//        String[] whereArgs = new String[] { String.valueOf(id) };
//        try {
//            mDb.delete(table, whereClause, whereArgs);
//        } catch (SQLException e) {
//            Toast.makeText(getApplicationContext(), "删除数据库失败",
//                    Toast.LENGTH_LONG).show();
//            return false;
//        }
//        return true;
//    }
//}
//
//// 长按事件响应
//OnCreateContextMenuListener listviewLongPress = new OnCreateContextMenuListener() {
//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenuInfo menuInfo) {
//        // TODO Auto-generated method stub
//        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
//        new AlertDialog.Builder(ListView_SqliteActivity.this)
//                    /* 弹出窗口的最上头文字 */
//                .setTitle("删除当前数据")
//                    /* 设置弹出窗口的图式 */
//                .setIcon(android.R.drawable.ic_dialog_info)
//                    /* 设置弹出窗口的信息 */
//                .setMessage("确定删除当前记录")
//                .setPositiveButton("是",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(
//                                    DialogInterface dialoginterface, int i) {
//                                // 获取位置索引
//                                int mListPos = info.position;
//                                // 获取对应HashMap数据内容
//                                HashMap<String, Object> map = listData
//                                        .get(mListPos);
//                                // 获取id
//                                int id = Integer.valueOf((map.get("id")
//                                        .toString()));
//                                // 获取数组具体值后,可以对数据进行相关的操作,例如更新数据
//                                if (dao.delete(mDb, "student", id)) {
//                                    // 移除listData的数据
//                                    listData.remove(mListPos);
//                                    listItemAdapter.notifyDataSetChanged();
//                                }
//                            }
//                        })
//                .setNegativeButton("否",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(
//                                    DialogInterface dialoginterface, int i) {
//                                // 什么也没做
//
//                            }
//                        }).show();
//    }
//};
//
//    @Override
//    public void finish() {
//        // TODO Auto-generated method stub
//        super.finish();
//        mDb.close();
//    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }



}
