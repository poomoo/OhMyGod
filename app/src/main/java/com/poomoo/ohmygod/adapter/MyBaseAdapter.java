package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.poomoo.ohmygod.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter抽象基类
 * 作者: 李苜菲
 * 日期: 2015/11/13 11:12.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
    private String TAG = "MyBaseAdapter";
    protected Context context;
    protected LayoutInflater inflater;
    protected List<T> itemList = new ArrayList<>();

    public MyBaseAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 判断数据是否为空
     *
     * @return 为空返回true，不为空返回false
     */
    public boolean isEmpty() {
        return itemList.isEmpty();
    }

    /**
     * 在原有的数据上添加新数据
     *
     * @param itemList
     */
    public void addItems(List<T> itemList) {
//        LogUtils.i(TAG,"addItems");
        this.itemList.addAll(itemList);
        notifyDataSetChanged();
    }

    /**
     * 设置为新的数据，旧数据会被清空
     *
     * @param itemList
     */
    public void setItems(List<T> itemList) {
//        LogUtils.i(TAG,"setItems");
        this.itemList.clear();
        this.itemList = itemList;
//        for(int i=0;i<itemList.size();i++){
//            LogUtils.i(TAG,"i:"+i+"itemList:"+itemList.toString());
//        }
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clearItems() {
        if (this.itemList.size() > 0)
            itemList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    abstract public View getView(int position, View convertView, ViewGroup parent);

    public List<T> getItemList() {
        return itemList;
    }
}
