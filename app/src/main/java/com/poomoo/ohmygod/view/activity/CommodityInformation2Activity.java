package com.poomoo.ohmygod.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.CommodityBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.listeners.AdvertisementListener;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.bigimage.ImagePagerActivity;
import com.poomoo.ohmygod.view.custom.SlideShowView;

import java.util.ArrayList;

/**
 * 商品详情(不能抢)
 * <p/>
 * 作者: 李苜菲
 * 日期: 2015/11/13 16:15.
 */
public class CommodityInformation2Activity extends BaseActivity {
    private SlideShowView slideShowView;
    private TextView nameTxt;//商品名称
    private TextView priceTxt;//商品价格
    private TextView startDate;//开抢时间
    private WebView commodityWeb;//商品详情

    private CommodityBO commodityBO;

    private int activeId;//--活动编号
    private String activityName;//活动名字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_information2);

        initView();
        getData();
        activityName = getIntent().getStringExtra(getString(R.string.intent_activityName));
    }


    protected void initView() {
        initTitleBar();

        nameTxt = (TextView) findViewById(R.id.txt_commodityName);
        priceTxt = (TextView) findViewById(R.id.txt_price);
        startDate = (TextView) findViewById(R.id.txt_startDate);
        slideShowView = (SlideShowView) findViewById(R.id.flipper_commodity);
        commodityWeb = (WebView) findViewById(R.id.web_commodity);
    }

    private void getData() {
        activeId = getIntent().getIntExtra(getString(R.string.intent_activeId), 0);

        showProgressDialog(getString(R.string.dialog_message));
        this.appAction.getCommodityInformation(application.getUserId(), activeId + "", new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                commodityBO = (CommodityBO) data.getObj();
                initData();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
                finish();
            }
        });
    }

    private void initData() {
        nameTxt.setText(commodityBO.getGoodsName());
        priceTxt.setText("￥" + commodityBO.getPrice());
        startDate.setText(commodityBO.getStartDt());

        int len = commodityBO.getPicList().size();
        String[] urls = new String[len];
        for (int i = 0; i < len; i++) {
            urls[i] = commodityBO.getPicList().get(i);
            LogUtils.i(TAG, "url:" + urls[i]);
        }
        slideShowView.setPics(urls, new AdvertisementListener() {
            @Override
            public void onResult(int position) {
                imageBrowse(position, commodityBO.getPicList());
            }
        });

        //商品详情
        commodityWeb.getSettings().setUseWideViewPort(true);
        commodityWeb.getSettings().setLoadWithOverviewMode(true);
        commodityWeb.getSettings().setDefaultTextEncodingName("UTF-8");
        commodityWeb.setVerticalScrollBarEnabled(false);
        commodityWeb.setHorizontalScrollBarEnabled(false);
        commodityWeb.loadData(commodityBO.getContent(), "text/html; charset=UTF-8", null);// 这种写法可以正确解码
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_commodity_information);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
    }

    /**
     * 查看中奖列表
     *
     * @param view
     */
    public void catWinnerList(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.intent_activityName), activityName);
        bundle.putInt(getString(R.string.intent_activeId), activeId);
        openActivity(WinnerListActivity.class, bundle);
    }

    protected void imageBrowse(int position, ArrayList<String> urls2) {
        LogUtils.i(TAG, "position:" + position + " size:" + urls2.size());
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

}
