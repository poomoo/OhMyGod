package com.poomoo.ohmygod.view.activity.pics;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.poomoo.model.ImageItem;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.ImageGridAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.picUtils.Bimp;
import com.poomoo.ohmygod.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class ImageGridActivity extends BaseActivity {
    private List<ImageItem> dataList;
    private GridView gridView;
    private Button btn;
    private ImageGridAdapter adapter;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyUtil.showToast(getApplicationContext(), getString(R.string.toast_9_pics));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        initView();
    }

    private void initView() {
        initTitleBar();

        gridView = (GridView) findViewById(R.id.grid_image);
        btn = (Button) findViewById(R.id.btn_completed);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ImageGridAdapter(ImageGridActivity.this, mHandler);
        gridView.setAdapter(adapter);
        dataList = (List<ImageItem>) getIntent().getSerializableExtra(MyConfig.EXTRA_IMAGE_LIST);
        adapter.setItems(dataList);

        adapter.setTextCallback(new ImageGridAdapter.TextCallback() {
            public void onListen(int count) {
                btn.setText(getString(R.string.btn_completed) + "(" + count + ")");
            }
        });

        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_bucket);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void toCompleted(View view) {
        ArrayList<String> list = new ArrayList<String>();
        Collection<String> c = adapter.map.values();
        Iterator<String> it = c.iterator();
        for (; it.hasNext(); ) {
            list.add(it.next());
        }
        if (Bimp.act_bool) {
            finish();
            Bimp.act_bool = false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (Bimp.drr.size() < 9) {
                Bimp.drr.add(list.get(i));
            }
        }
        finish();
    }
}
