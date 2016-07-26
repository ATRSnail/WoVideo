package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.order.BillAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.BillList;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.widget.RecycleViewDivider;
import com.lt.hm.wovideo.widget.SecondTopbar;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/6
 */
public class BillsPage extends BaseActivity implements SecondTopbar.myTopbarClicklistenter,BillAdapter.OnPayBtnClick, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.bills_topbar)
    SecondTopbar billsTopbar;
    @BindView(R.id.order_list)
    RecyclerView orderList;
    @BindView(R.id.order_refresh_layout)
    SwipeRefreshLayout order_refresh_layout;

    BillAdapter billAdapter;
    List<BillList.OrderListBean> mList;
    String userinfo;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_bills;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        userinfo= SharedPrefsUtils.getStringPreference(getApplicationContext(),"userinfo");
        billsTopbar.setRightIsVisible(false);
        billsTopbar.setLeftIsVisible(true);
        billsTopbar.setOnTopbarClickListenter(this);
        mList = new ArrayList<>();
        orderList.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.VERTICAL));
        orderList.setLayoutManager(new LinearLayoutManager(this));

//        for (int i = 0; i < 5; i++) {
//            Order order = new Order();
//            order.setName("Name" + i);
//            order.setType("type" + i);
//            order.setPrice(i + "");
//            order.setStatus("翼支付");
//            mList.add(order);
//        }
        order_refresh_layout.setColorSchemeResources(android.R.color.holo_green_light,android.R.color.holo_blue_bright,android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        order_refresh_layout.setOnRefreshListener(this);
    }


   private void initList(List<BillList.OrderListBean> mList){
        billAdapter = new BillAdapter(this, mList);
        orderList.setAdapter(billAdapter);
        billAdapter.notifyDataSetChanged();
        billAdapter.setListener(this);
        billAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
            }
        });
    }
    @Override
    public void initViews() {

    }

    @Override
    public void initDatas() {
        getBills();
    }

    private void getBills() {
//        String userinfo = ACache.get(this).getAsString("userinfo");
        if (StringUtils.isNullOrEmpty(userinfo)){

        }else{
            UserModel model= new Gson().fromJson(userinfo,UserModel.class);
            HashMap<String,Object> map= new HashMap<>();
            map.put("pageNum",1);
            map.put("numPerPage",100);
            map.put("userid",model.getId());
            HttpApis.getBills(map, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    TLog.log("error:"+e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    TLog.log(response);
                    ResponseObj<BillList,RespHeader>  resp = new ResponseObj<BillList, RespHeader>();
                    ResponseParser.parse(resp,response,BillList.class,RespHeader.class);
                    if (resp.getHead().getRspCode().equals(ResponseCode.Success)){
                        mList.addAll(resp.getBody().getOrderList());
                        initList(mList);
                    }else{
                        Toast.makeText(getApplicationContext(),resp.getHead().getRspMsg(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {

    }

    @Override
    public void onPay(BillList.OrderListBean bean) {
        ToPay(bean);
    }

    private void ToPay(BillList.OrderListBean bean) {
        purchOrder(bean.getOrderNo());
    }
    private void purchOrder(String orderId) {
        HashMap<String,Object> map= new HashMap<>();
        String string= SharedPrefsUtils.getStringPreference(getApplicationContext(),"userinfo");
        UserModel model = new Gson().fromJson(string,UserModel.class);
        map.put("cellphone",model.getPhoneNo());
        map.put("spid",orderId);
        HttpApis.purchOrder(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<String,RespHeader> resp = new ResponseObj<String, RespHeader>();
                ResponseParser.parse(resp,response,String.class,RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)){
                    // TODO: 16/6/15 吊起  完成订单接口
                    finishOrder(orderId,model.getId());
                }else{
                    Toast.makeText(getApplicationContext(),resp.getHead().getRspMsg(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void finishOrder(String orderId,String userid) {
        HashMap<String,Object> map= new HashMap<>();
        map.put("orderId",orderId);
        map.put("userid",userid);
        HttpApis.finishOrder(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:"+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<String,RespHeader> resp = new ResponseObj<String, RespHeader>();
                ResponseParser.parse(resp,response,String.class,RespHeader.class);
                Toast.makeText(getApplicationContext(),resp.getHead().getRspMsg(),Toast.LENGTH_SHORT).show();

                if (resp.getHead().getRspCode().equals(ResponseCode.Success)){
                    // TODO: 16/6/15 UI 布局变更
//                    imgKing.setImageResource(R.drawable.icon_vip_opened);
//                    open_get_vip_layout.setVisibility(View.GONE);
//                    imgVipAuthers.setImageResource(R.drawable.img_vip_opened);
                }else{
//                    imgKing.setImageResource(R.drawable.icon_vip_unopened);
//                    open_get_vip_layout.setVisibility(View.VISIBLE);
//                    imgVipAuthers.setImageResource(R.drawable.img_vip_unopened);
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        if (mList.size()>0){
            mList.clear();
        }
        initDatas();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (order_refresh_layout!=null && order_refresh_layout.isRefreshing()){
                    order_refresh_layout.setRefreshing(false);
                }
            }
        },3000);

    }
}
