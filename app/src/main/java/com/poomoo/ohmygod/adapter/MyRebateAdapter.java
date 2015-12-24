/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poomoo.model.RebateBO;
import com.poomoo.model.WithdrawDepositBO;
import com.poomoo.ohmygod.R;

/**
 * 我的返现
 * 作者: 李苜菲
 * 日期: 2015/11/23 17:11.
 */
public class MyRebateAdapter extends MyBaseAdapter<RebateBO> {
    private RebateBO rebateBO;
    private String date;
    private String status;
    private String account;

    public MyRebateAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_my_withdraw_deposit, null);
            viewHolder.dateTxt = (TextView) convertView.findViewById(R.id.txt_withdraw_deposit_date);
            viewHolder.statusTxt = (TextView) convertView.findViewById(R.id.txt_withdraw_deposit_status);
            viewHolder.moneyTxt = (TextView) convertView.findViewById(R.id.txt_withdraw_deposit_money);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        rebateBO = new RebateBO();
        rebateBO = itemList.get(position);
        date = rebateBO.getGotDt();
        status = rebateBO.getStatusName();
        account = rebateBO.getGotFee();

        viewHolder.dateTxt.setText(date);
        viewHolder.statusTxt.setText(status);
        viewHolder.moneyTxt.setText("￥" + account);

        return convertView;
    }

    class ViewHolder {
        private TextView dateTxt;
        private TextView statusTxt;
        private TextView moneyTxt;
    }
}
