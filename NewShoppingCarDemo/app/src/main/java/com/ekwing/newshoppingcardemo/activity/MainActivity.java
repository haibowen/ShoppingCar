package com.ekwing.newshoppingcardemo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekwing.newshoppingcardemo.R;
import com.ekwing.newshoppingcardemo.adapter.ShoppingAdapter;
import com.ekwing.newshoppingcardemo.bean.ShoppingDataBean;
import com.ekwing.newshoppingcardemo.utils.Httputils;
import com.ekwing.newshoppingcardemo.utils.ToastUtil;
import com.ekwing.newshoppingcardemo.view.RoundCornerDialog;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Toolbar tb_title;//标题栏
    private ImageView iv_all_selet;//底部全选
    private ImageView iv_select_all;//全选按钮
    private TextView tv_price;//价钱
    private Button bt_jiesuan;//结算
    private Button bt_delete;//删除
    private ExpandableListView el_list;//列表数据
    private String result=null;
    //模拟的购物车数据（实际开发中使用后台返回的数据）
    private String shoppingCarData = "{\n" +
            "    \"code\": 200,\n" +
            "    \"mData\": [\n" +
            "        {\n" +
            "            \"goodsBeans\": [\n" +
            "                {\n" +
            "                    \"goodsId\": \"111111\",\n" +
            "                    \"goodsImage\": \"http://pic.58pic.com/58pic/15/62/69/34K58PICbmZ_1024.jpg\",\n" +
            "                    \"goodsName\": \"JAVA核心技术卷(第二卷)(平装)\",\n" +
            "                    \"goodsNum\": \"1\",\n" +
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
            "                    \"goodsPrice\": \"28.00\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goodsId\": \"222222\",\n" +
            "                    \"goodsImage\": \"http://img01.taopic.com/150424/240473-1504240U13615.jpg\",\n" +
            "                    \"goodsName\": \"时间简史\",\n" +
            "                    \"goodsNum\": \"2\",\n" +
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
            "                    \"goodsPrice\": \"38.00\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goodsId\": \"333332\",\n" +
            "                    \"goodsImage\": \"http://pic.58pic.com/58pic/14/71/50/40e58PICy54_1024.jpg\",\n" +
            "                    \"goodsName\": \"专业课本\",\n" +
            "                    \"goodsNum\": \"3\",\n" +
            "                    \"goodsPrice\": \"338.00\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goodsId\": \"333333\",\n" +
            "                    \"goodsImage\": \"http://img01.taopic.com/150518/318750-15051PS40671.jpg\",\n" +
            "                    \"goodsName\": \"Android Kotlin基本使用\",\n" +
            "                    \"goodsNum\": \"3\",\n" +
            "                    \"goodsPrice\": \"3.80\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"stroreId\": \"3\",\n" +
            "            \"storeName\": \"三号大书店\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    private List<ShoppingDataBean.DataBean> mData;
    private ShoppingAdapter shoppingAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        initExpandListView();
        getDataFromServer();
        initData();

    }
    private void initView() {
        tb_title = findViewById(R.id.toolbar);
        tb_title.setTitle("");
        setSupportActionBar(tb_title);
        iv_all_selet = findViewById(R.id.iv_select);
        iv_select_all = findViewById(R.id.iv_select);
        tv_price = findViewById(R.id.tv_price);
        bt_jiesuan = findViewById(R.id.bt_jiesuan);
        bt_delete = findViewById(R.id.bt_delete);
        el_list=findViewById(R.id.el_list);
    }

    /**
     * 初始化展示列表
     */
    public void  initExpandListView(){
        shoppingAdapter = new ShoppingAdapter(mContext, iv_all_selet, iv_select_all, bt_jiesuan, bt_delete, tv_price);
        el_list.setAdapter(shoppingAdapter);
        shoppingAdapter.setChangeCountListener(new ShoppingAdapter.ChangeCountListener() {
            @Override
            public void changeCount(String goodsId) {

            }
        });
        shoppingAdapter.setOnDeleteListener(new ShoppingAdapter.OnDeleteListener() {
            @Override
            public void onDelete() {
                initDelete();

            }
        });
    }

    /**
     * 初始化列表数据
     * @param mData
     */

    private void initExpandableListViewData(List<ShoppingDataBean.DataBean> mData) {
        if (mData != null && mData.size() > 0) {
            //刷新数据时，保持当前位置
            shoppingAdapter.setmData(mData);
            //使所有组展开
            for (int i = 0; i < shoppingAdapter.getGroupCount(); i++) {
                el_list.expandGroup(i);
            }
            //使组点击无效果
            el_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
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
        ShoppingDataBean shoppingDataBean = gson.fromJson(shoppingCarData, ShoppingDataBean.class);
        mData = shoppingDataBean.getmData();
        initExpandableListViewData(mData);
    }

    /**
     * 创建菜单栏
     *
     * @param menu
     * @return
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }
    /**
     * 菜单栏的点击事件
     *
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.bt_edit:

                bt_delete.setVisibility(View.VISIBLE);
                tv_price.setVisibility(View.INVISIBLE);

                break;
            case R.id.bt_complte:
                bt_delete.setVisibility(View.GONE);
                tv_price.setVisibility(View.VISIBLE);

                break;
        }
        return super.onOptionsItemSelected(item);
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

    public String getDataFromServer(){

        Httputils.sendOkHttpRequest("http://172.17.5.183:8080/htdoc/shoppingCarData.json", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result =response.body().string();
                Log.e("22222", "getDataFromServer: "+result);
            }
        });

       return result;

    }
}
