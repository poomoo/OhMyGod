package com.poomoo.ohmygod.view.activity.pics;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.poomoo.model.ImageBucket;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.ImageBucketAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.utils.picUtils.AlbumHelper;
import com.poomoo.ohmygod.view.activity.BaseActivity;

import java.io.Serializable;
import java.util.List;

public class PhotosActivity extends BaseActivity {
    private List<ImageBucket> imageBucketList;
    private GridView gridView;
    private ImageBucketAdapter adapter;// 自定义的适配器
    private AlbumHelper helper;
    public static Bitmap bimap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_bucket);

        initView();
    }

    protected void initView() {
        initTitleBar();
        initData();

        gridView = (GridView) findViewById(R.id.grid_image_bucket);
        adapter = new ImageBucketAdapter(this);
        gridView.setAdapter(adapter);
        adapter.addItems(imageBucketList);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(PhotosActivity.this,
                        ImageGridActivity.class);
                intent.putExtra(MyConfig.EXTRA_IMAGE_LIST,
                        (Serializable) imageBucketList.get(position).imageList);
                startActivity(intent);
                finish();
            }

        });
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_bucket);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());
        imageBucketList = helper.getImagesBucketList(false);
        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_addpic_unfocused);
    }

}
