package com.poomoo.ohmygod.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.CommodityBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.listeners.AdvertisementListener;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;
import com.poomoo.ohmygod.view.bigimage.ImagePagerActivity;
import com.poomoo.ohmygod.view.custom.SlideShowView;

import java.util.ArrayList;

/**
 * 商品详情(不能抢)
 * <p>
 * 作者: 李苜菲
 * 日期: 2015/11/13 16:15.
 */
public class CommodityInformation2Activity extends BaseActivity {
    private SlideShowView slideShowView;
    private TextView nameTxt;//商品名称
    private TextView priceTxt;//商品价格
    private TextView startDate;//开抢时间
    private WebView commodityWeb;//商品详情
    private ImageView detailsImg;
    private TextView shopNameTxt;//店铺名称
    private TextView shopAddressTxt;//店铺地址
    private LinearLayout merchantInfoLlayout;//商家信息
    private LinearLayout merchantInfoLlayout2;//底部商家信息


    private CommodityBO commodityBO;

    private int activeId;//--活动编号
    private String activityName;//活动名字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_information2);
        addActivityToArrayList(this);
        initView();
        getData();
        activityName = getIntent().getStringExtra(getString(R.string.intent_activityName));
    }


    protected void initView() {
        initTitleBar();

        nameTxt = (TextView) findViewById(R.id.txt_commodityStatement);
        priceTxt = (TextView) findViewById(R.id.txt_price);
        startDate = (TextView) findViewById(R.id.txt_startDate);
        slideShowView = (SlideShowView) findViewById(R.id.flipper_commodity);
        commodityWeb = (WebView) findViewById(R.id.web_commodity);
        detailsImg = (ImageView) findViewById(R.id.img_commodityDetail);

        shopNameTxt = (TextView) findViewById(R.id.txt_shopName);
        shopAddressTxt = (TextView) findViewById(R.id.txt_shopAddress);
        merchantInfoLlayout = (LinearLayout) findViewById(R.id.llayout_merchantInfo);
        merchantInfoLlayout2 = (LinearLayout) findViewById(R.id.llayout_merchantInfo2);

    }

    private void getData() {
        activeId = getIntent().getIntExtra(getString(R.string.intent_activeId), 0);
        activityName = getIntent().getStringExtra(getString(R.string.intent_activityName));
        LogUtils.i(TAG, "activeId:" + activeId + "activityName:" + activityName);

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

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//
        ImageLoader.getInstance().displayImage(commodityBO.getSmallPic(), detailsImg, defaultOptions);
        if (TextUtils.isEmpty(commodityBO.getShopsName()) || TextUtils.isEmpty(commodityBO.getShopsAddress())) {
            merchantInfoLlayout.setVisibility(View.GONE);
            merchantInfoLlayout2.setVisibility(View.GONE);
        } else {
            shopNameTxt.setText(commodityBO.getShopsName());
            shopAddressTxt.setText(commodityBO.getShopsAddress());
        }

        //商品详情
//        commodityWeb.getSettings().setUseWideViewPort(true);
//        commodityWeb.getSettings().setLoadWithOverviewMode(true);
        commodityWeb.getSettings().setDefaultTextEncodingName("UTF-8");
        commodityWeb.getSettings().setJavaScriptEnabled(true);
        commodityWeb.loadData(commodityBO.getContent(), "text/html; charset=UTF-8", null);// 这种写法可以正确解码
        // 添加js交互接口类，并起别名 imagelistner
        commodityWeb.addJavascriptInterface(new JavascriptInterface(), "imagelistner");
        commodityWeb.setWebViewClient(new MyWebViewClient());
        commodityWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100)
                    merchantInfoLlayout2.setVisibility(View.VISIBLE);
            }
        });
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

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        commodityWeb.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "var temp='';" +
                "for(var i=0;i<objs.length;i++)  " +
                "{" + "temp+=objs[i].src+';'" + "}" +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(temp,this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    // js通信接口
    class JavascriptInterface {

        @android.webkit.JavascriptInterface
        public void openImage(String imgList, String src) {
            LogUtils.i(TAG, "imgList" + imgList);
            int position = 0;
            String img = src;
            String[] temp = imgList.split(";");
            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i < temp.length; i++) {
                if (img.equals(temp[i]))
                    position = i;
                arrayList.add(temp[i]);
            }
            imageBrowse(position, arrayList);
        }
    }

    // 监听
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            view.getSettings().setJavaScriptEnabled(true);

            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);

        }
    }

    protected void imageBrowse(int position, ArrayList<String> urls2) {
        LogUtils.i(TAG, "position:" + position + " size:" + urls2.size());
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

    /**
     * 跳转到地图
     *
     * @param view
     */
    public void toPosition(View view) {
        Bundle bundle = new Bundle();
        bundle.putDouble(getString(R.string.intent_latitude), Double.parseDouble(commodityBO.getShopsLat()));
        bundle.putDouble(getString(R.string.intent_longitude), Double.parseDouble(commodityBO.getShopsLng()));
        bundle.putString(getString(R.string.intent_shopName), commodityBO.getShopsName());
        openActivity(MapActivity.class, bundle);
    }

    /**
     * 联系商家
     *
     * @param view
     */
    public void toContact(View view) {
        Dialog dialog = new AlertDialog.Builder(this).setMessage("拨打电话" + commodityBO.getShopsTel()).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + commodityBO.getShopsTel()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).create();
        dialog.show();
    }

}
