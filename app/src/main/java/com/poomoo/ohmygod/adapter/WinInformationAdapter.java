package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.model.WinInformationBO;
import com.poomoo.ohmygod.R;

/**
 * 中奖信息
 * 作者: 李苜菲
 * 日期: 2015/11/13 11:18.
 */
public class WinInformationAdapter extends MyBaseAdapter<WinInformationBO> {
    public WinInformationAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_list_win_information, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.item_list_win_information_textView_title);
            viewHolder.winner = (TextView) view.findViewById(R.id.item_list_win_information_textView_winner);
            viewHolder.win_date = (TextView) view.findViewById(R.id.item_list_win_information_textView_win_date);
            viewHolder.address = (TextView) view.findViewById(R.id.item_list_win_information_textView_address);
            viewHolder.end_date = (TextView) view.findViewById(R.id.item_list_win_information_textView_end_date);
            viewHolder.reason = (TextView) view.findViewById(R.id.item_list_win_information_textView_reason);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.item_list_win_information_imageView_pic);

            view.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) view.getTag();

        WinInformationBO winInformationBO = itemList.get(i);
        viewHolder.title.setText(winInformationBO.getTitle());
        viewHolder.winner.setText(TextUtils.isEmpty(winInformationBO.getWinNickName()) ? winInformationBO.getWinTel() : winInformationBO.getWinNickName());
        viewHolder.win_date.setText(winInformationBO.getPlayDt());
        viewHolder.address.setText(winInformationBO.getGetAddress());
        viewHolder.end_date.setText(winInformationBO.getGetEndDt());
        viewHolder.reason.setText(winInformationBO.getTransferMsg());

        ImageLoader.getInstance().displayImage(winInformationBO.getPicture(), viewHolder.imageView);

        return view;
    }

    static class ViewHolder {
        TextView title;
        TextView winner;
        TextView win_date;
        TextView address;
        TextView end_date;
        TextView reason;
        ImageView imageView;
    }
}
