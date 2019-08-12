package com.ekwing.newshoppingcardemo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekwing.newshoppingcardemo.R;
import com.ekwing.newshoppingcardemo.adapter.InvidateAdapter;
import com.ekwing.newshoppingcardemo.adapter.LikeGoodsAdapter;
import com.ekwing.newshoppingcardemo.adapter.ShoppingAdapter;
import com.ekwing.newshoppingcardemo.bean.LikeGoodsBean;
import com.ekwing.newshoppingcardemo.bean.ShoppingDataBean;
import com.ekwing.newshoppingcardemo.helper.ItemLongClickMaskHelper;
import com.ekwing.newshoppingcardemo.utils.Constring;
import com.ekwing.newshoppingcardemo.utils.Httputils;
import com.ekwing.newshoppingcardemo.utils.LogUtil;
import com.ekwing.newshoppingcardemo.utils.ToastUtil;
import com.ekwing.newshoppingcardemo.view.RoundCornerDialog;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LikeGoodsAdapter.OnItemClickLongCallBack, ItemLongClickMaskHelper.ItemMaskClickListener {
    private Toolbar tbTitle;//标题栏
    private ImageView ivAllSelet;//底部全选
    private ImageView ivSelectAll;//全选按钮
    private TextView tvPrice;//价钱
    private Button btJiesuan;//结算
    private Button btDelete;//删除
    private ExpandableListView elList;//列表数据
    private String mResult = null;
    private RecyclerView rvLikeList;//喜欢的列表
    private LikeGoodsAdapter mLikeGoodsAdapter;//喜欢列表的适配器
    private List<LikeGoodsBean> mLikeData = new ArrayList<>();//喜欢的数据
    private TextView tvAdShow;//展示广告的文字
    private TextView tvEdit;//管理按钮
    private CollapsingToolbarLayout ctlToolbarTitle;//折叠式标题栏
    private AppBarLayout appBarLayout;//滑动标题栏
    private TextView tvShoppingCar;//购物车字
    private TextView tvShoppingCarHide;//隐藏的购物车字
    private ItemLongClickMaskHelper mMaskHelper;//长按的辅助类
    private int mItemLongClickPosition;
    private String mShoppingCarData; //模拟的购物车数据（实际开发中使用后台返回的数据）
    private List<ShoppingDataBean.DataBean> mData;
    private ShoppingAdapter mShoppingAdapter;//适配器
    private Context mContext;
    private RecyclerView rvInvide;//失效的列表
    private InvidateAdapter mInvidateAdapter;//失效列表的适配器
    private List<LikeGoodsBean> mDataInvidata = new ArrayList<>();//失效类表数据
    private TextView tvInvidat;//失效列表的字
    private Bundle mDataBundle = new Bundle();//接收数据
    private Handler mInvidataHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constring.INVIDATE_GOODS:

                    mDataBundle = msg.getData();
                    initRecyclerInvidateData();
                    mInvidateAdapter.notifyDataSetChanged();
                    mShoppingAdapter.notifyDataSetChanged();
                    Log.e("34567", "handleMessage: " + mDataBundle.getString("pic"));
                    break;


            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        getServerResult();
        initView();
        initExpandListView();
        getDataFromServer();
        initData();
        initRecyclerViewData();
        initRecyclerView();
        appOnClickListener();
        initRecyclerViewInvi();

    }
    private void initView() {
        tbTitle = findViewById(R.id.toolbar);
        tbTitle.setTitle("");
        setSupportActionBar(tbTitle);
        ivAllSelet = findViewById(R.id.iv_select);
        ivSelectAll = findViewById(R.id.iv_select);
        tvPrice = findViewById(R.id.tv_price);
        btJiesuan = findViewById(R.id.bt_jiesuan);
        btDelete = findViewById(R.id.bt_delete);
        elList = findViewById(R.id.el_list);
        elList.setGroupIndicator(null);
        rvLikeList = findViewById(R.id.rv_list);
        tvAdShow = findViewById(R.id.tv_ad_show);
        ImmersionBar.with(this).statusBarColor(R.color.colorAction).init();
        tvEdit = findViewById(R.id.tv_edit);
        tvEdit.setOnClickListener(this);
        ctlToolbarTitle = findViewById(R.id.col_Layout);
        appBarLayout = findViewById(R.id.appbar);
        tvShoppingCar = findViewById(R.id.tv_shopping_car);
        tvShoppingCarHide = findViewById(R.id.tv_shoping_gone);
        rvInvide = findViewById(R.id.rv_invalid);//失效的列表
        tvInvidat=findViewById(R.id.tv_inividata);


    }

    private String getServerResult() {

        mShoppingCarData = "{\n" +
                "    \"code\": 200,\n" +
                "    \"mData\": [\n" +
                "        {\n" +
                "            \"goodsBeans\": [\n" +
                "                {\n" +
                "                    \"goodsId\": \"111111\",\n" +
                "                    \"goodsImage\": \"http://file06.16sucai.com/2016/0511/9711205e4c003182edeed83355e6f1c7.jpg\",\n" +
                "                    \"goodsName\": \"JAVA核心技术卷(第二卷)(平装)\",\n" +
                "                    \"goodsNum\": \"1\",\n" +
                "                    \"goodsNumAll\": \"3\",\n" +
                "                    \"goodsPrice\": \"18.00\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"stroreId\": \"1\",\n" +
                "            \"storeName\": \"一号小书店\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"goodsBeans\": [\n" +
                "                {\n" +
                "                    \"goodsId\": \"222221\",\n" +
                "                    \"goodsImage\": \"http://file06.16sucai.com/2016/0511/9711205e4c003182edeed83355e6f1c7.jpg\",\n" +
                "                    \"goodsName\": \"GreenDao的使用\",\n" +
                "                    \"goodsNum\": \"2\",\n" +
                "                    \"goodsNumAll\": \"3\",\n" +
                "                    \"goodsPrice\": \"28.00\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"goodsId\": \"222222\",\n" +
                "                    \"goodsImage\": \"http://file06.16sucai.com/2016/0511/9711205e4c003182edeed83355e6f1c7.jpg\",\n" +
                "                    \"goodsName\": \"时间简史\",\n" +
                "                    \"goodsNum\": \"2\",\n" +
                "                    \"goodsNumAll\": \"3\",\n" +
                "                    \"goodsPrice\": \"228.00\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"stroreId\": \"2\",\n" +
                "            \"storeName\": \"二号中书店\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"goodsBeans\": [\n" +
                "                {\n" +
                "                    \"goodsId\": \"333331\",\n" +
                "                    \"goodsImage\": \"http://pic22.nipic.com/20120718/8002769_100147127333_2.jpg\",\n" +
                "                    \"goodsName\": \"心的重建\",\n" +
                "                    \"goodsNum\": \"3\",\n" +
                "                    \"goodsNumAll\": \"3\",\n" +
                "                    \"goodsPrice\": \"38.00\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"goodsId\": \"333332\",\n" +
                "                    \"goodsImage\": \"http://file06.16sucai.com/2016/0511/9711205e4c003182edeed83355e6f1c7.jpg\",\n" +
                "                    \"goodsName\": \"专业课本\",\n" +
                "                    \"goodsNum\": \"3\",\n" +
                "                    \"goodsNumAll\": \"3\",\n" +
                "                    \"goodsPrice\": \"338.00\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"goodsId\": \"333333\",\n" +
                "                    \"goodsImage\": \"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565257038293&di=37896fd9ac8fbd985fbc017dab398351&imgtype=0&src=http%3A%2F%2Fpic2.zhimg.com%2Fv2-9bfd84bde84525954f32a002d100a981_b.jpg\",\n" +
                "                    \"goodsName\": \"Android Kotlin基本使用\",\n" +
                "                    \"goodsNum\": \"0\",\n" +
                "                    \"goodsNumAll\": \"0\",\n" +
                "                    \"goodsPrice\": \"3.80\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"stroreId\": \"3\",\n" +
                "            \"storeName\": \"三号大书店\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        return mShoppingCarData;
    }

    /**
     * 折叠式标题栏的监听事件
     */

    public void appOnClickListener() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i == 0) {
                    tvShoppingCar.setVisibility(View.VISIBLE);
                    tvShoppingCarHide.setVisibility(View.INVISIBLE);

                } else {
                    tvShoppingCar.setVisibility(View.INVISIBLE);
                    tvShoppingCarHide.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    /**
     * 初始化展示列表
     */
    public void initExpandListView() {
        mShoppingAdapter = new ShoppingAdapter(mContext, ivAllSelet, ivSelectAll, btJiesuan, btDelete, tvPrice, mInvidataHandler);
        elList.setAdapter(mShoppingAdapter);
        mShoppingAdapter.setChangeCountListener(new ShoppingAdapter.ChangeCountListener() {
            @Override
            public void changeCount(String goodsId) {

            }
        });
        mShoppingAdapter.setOnDeleteListener(new ShoppingAdapter.OnDeleteListener() {
            @Override
            public void onDelete() {
                initDelete();

            }
        });
    }

    /**
     * 初始化列表数据
     *
     * @param mData
     */

    private void initExpandableListViewData(List<ShoppingDataBean.DataBean> mData) {
        if (mData != null && mData.size() > 0) {
            //刷新数据时，保持当前位置
            mShoppingAdapter.setmData(mData);
            //使所有组展开
            for (int i = 0; i < mShoppingAdapter.getGroupCount(); i++) {
                elList.expandGroup(i);

            }
            //使组点击无效果
            elList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    return true;
                }
            });
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //使用Gson解析购物车数据，
        //ShoppingCarDataBean为bean类，Gson按照bean类的格式解析数据
        /**
         * 实际开发中，通过请求后台接口获取购物车数据并解析
         */
        Gson gson = new Gson();
        ShoppingDataBean shoppingDataBean = gson.fromJson(mShoppingCarData, ShoppingDataBean.class);
        mData = shoppingDataBean.getmData();
        initExpandableListViewData(mData);
    }

    /**
     * 判断是否要弹出删除的dialog
     * 通过bean类中的DatasBean的isSelect_shop属性，判断店铺是否被选中；
     * GoodsBean的isSelect属性，判断商品是否被选中，
     */
    private void initDelete() {
        //判断是否有店铺或商品被选中
        //true为有，则需要刷新数据；反之，则不需要；
        boolean hasSelect = false;
        //创建临时的List，用于存储没有被选中的购物车数据
        List<ShoppingDataBean.DataBean> datasTemp = new ArrayList<>();

        for (int i = 0; i < mData.size(); i++) {
            List<ShoppingDataBean.DataBean.GoodsBean> goods = mData.get(i).getGoodsBeans();
            boolean isSelect_shop = mData.get(i).isSelect_shop();

            if (isSelect_shop) {
                hasSelect = true;
                //跳出本次循环，继续下次循环。
                continue;
            } else {
                datasTemp.add(mData.get(i).clone());
                datasTemp.get(datasTemp.size() - 1).setGoodsBeans(new ArrayList<ShoppingDataBean.DataBean.GoodsBean>());
            }

            for (int y = 0; y < goods.size(); y++) {
                ShoppingDataBean.DataBean.GoodsBean goodsBean = goods.get(y);
                boolean isSelect = goodsBean.isSelect();

                if (isSelect) {
                    hasSelect = true;
                } else {
                    datasTemp.get(datasTemp.size() - 1).getGoodsBeans().add(goodsBean);
                }
            }
        }
        if (hasSelect) {
            showDeleteDialog(datasTemp);
        } else {
            ToastUtil.makeText(mContext, "请选择要删除的商品");

        }
    }


    /**
     * 展示删除的dialog（可以自定义弹窗，不用删除即可）
     *
     * @param datasTemp
     */
    private void showDeleteDialog(final List<ShoppingDataBean.DataBean> datasTemp) {
        View view = View.inflate(mContext, R.layout.dialog_two_btn, null);
        final RoundCornerDialog roundCornerDialog = new RoundCornerDialog(mContext, 0, 0, view, R.style.RoundCornerDialog);
        roundCornerDialog.show();
        roundCornerDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        roundCornerDialog.setOnKeyListener(keylistener);//设置点击返回键Dialog不消失

        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
        TextView tv_logout_confirm = (TextView) view.findViewById(R.id.tv_logout_confirm);
        TextView tv_logout_cancel = (TextView) view.findViewById(R.id.tv_logout_cancel);
        tv_message.setText("确定要删除商品吗？");

        //确定
        tv_logout_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundCornerDialog.dismiss();
                mData = datasTemp;
                initExpandableListViewData(mData);
            }
        });
        //取消
        tv_logout_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundCornerDialog.dismiss();
            }
        });
    }

    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };

    /**
     * 模拟从服务端请求数据
     *
     * @return
     */

    public String getDataFromServer() {

        Httputils.sendOkHttpRequest("http://172.17.5.183:8080/htdoc/shoppingCarData.json", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mResult = response.body().string();
                Log.e("22222", "getDataFromServer: " + mResult);
            }
        });

        return mResult;

    }

//******************************************************************************8

    /**
     * 初始化话RecyclerView的适配器
     */

    public void initRecyclerView() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        rvLikeList.setLayoutManager(gridLayoutManager);
        mMaskHelper = new ItemLongClickMaskHelper(this);
        mMaskHelper.setMaskItemListener(this);
        mLikeGoodsAdapter = new LikeGoodsAdapter(mContext, mLikeData);
        mLikeGoodsAdapter.setOnItemClickLongCallBack(this);
        rvLikeList.setAdapter(mLikeGoodsAdapter);
    }


    /**
     * 初始化RecyclerView的数据
     */
    public void initRecyclerViewData() {
        for (int i = 0; i < 8; i++) {
            LikeGoodsBean likeGoodsBean1 = new LikeGoodsBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565256972352&di=12f7ff897debe8842eb907774c02b84f&imgtype=0&src=http%3A%2F%2Fs9.knowsky.com%2Fbk%2F2012%2F201206030829144213.jpg", "JAVA入门", "￥20元");
            mLikeData.add(likeGoodsBean1);
            LikeGoodsBean likeGoodsBean2 = new LikeGoodsBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565257038293&di=37896fd9ac8fbd985fbc017dab398351&imgtype=0&src=http%3A%2F%2Fpic2.zhimg.com%2Fv2-9bfd84bde84525954f32a002d100a981_b.jpg", "JavaWeb学习", "￥20元");
            mLikeData.add(likeGoodsBean2);

        }
    }

    /**
     * 失效列表
     */
    public void initRecyclerViewInvi() {
        mInvidateAdapter = new InvidateAdapter(mContext, mDataInvidata);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvInvide.setLayoutManager(layoutManager);
        rvInvide.setAdapter(mInvidateAdapter);
        mInvidateAdapter.notifyDataSetChanged();


    }

    /**
     * 失效列表的数据
     */
    public void initRecyclerInvidateData() {
        tvInvidat.setVisibility(View.VISIBLE);
        String pic = mDataBundle.getString("pic");
        String name = mDataBundle.getString("name");
        LikeGoodsBean likeGoodsBean = new LikeGoodsBean(pic, name);
        mDataInvidata.add(likeGoodsBean);
    }

    /**
     * 点击事件
     *
     * @param view
     */

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_edit:
                if (tvEdit.getText().equals("管理")) {
                    tvEdit.setText("完成");
                    btDelete.setVisibility(View.VISIBLE);
                    btJiesuan.setVisibility(View.GONE);
                    tvPrice.setVisibility(View.GONE);
                } else {
                    tvEdit.setText("管理");
                    btDelete.setVisibility(View.GONE);
                    btJiesuan.setVisibility(View.VISIBLE);
                    tvPrice.setVisibility(View.VISIBLE);
                }

                break;
        }

    }

    //*****************************************************长按点击事件的回调

    @Override
    public void itemLongClick(View view, int postion) {
        mItemLongClickPosition = postion;
        mMaskHelper.setRootFrameLayout((FrameLayout) view);
        ToastUtil.makeText(mContext, "我被点击了");
        LogUtil.e("sss", "被点击了");

    }

    /**
     * 长按的回调的方法
     */
    @Override
    public void findSame() {
        ToastUtil.makeText(mContext, "跳转到相似界面" + mItemLongClickPosition);

    }

    @Override
    public void collect() {
        ToastUtil.makeText(mContext, "收藏" + mItemLongClickPosition);
    }
}
