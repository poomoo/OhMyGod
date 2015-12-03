/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poomoo.model.WithdrawDepositBO;
import com.poomoo.ohmygod.R;

/**
 * 我的提现
 * 作者: 李苜菲
 * 日期: 2015/11/23 17:11.
 */
public class MyWithdrawDepositAdapter extends MyBaseAdapter<WithdrawDepositBO> {
    private WithdrawDepositBO withdrawDepositBO;
    private String date;
    private String status;
    private String account;

    public MyWithdrawDepositAdapter(Context context) {
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

        withdrawDepositBO = new WithdrawDepositBO();
        withdrawDepositBO = itemList.get(position);
        date = withdrawDepositBO.getDrawDt();
        status = withdrawDepositBO.getStatus();
        account = withdrawDepositBO.getDrawFee();

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
