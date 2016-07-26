package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.acache.ACache;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.handler.UnLoginHandler;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.HttpUtils;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.PlaceOrder;
import com.lt.hm.wovideo.model.UserModel;
import com.lt.hm.wovideo.utils.SharedPrefsUtils;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.widget.CircleImageView;
import com.lt.hm.wovideo.widget.SecondTopbar;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by songchenyu on 16/6/3.
 */
public class OpenVipActivity extends BaseActivity implements SecondTopbar.myTopbarClicklistenter {
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/WoVideo/Portrait/";
    @BindView(R.id.open_vip_topbar)
    SecondTopbar openVipTopbar;
    @BindView(R.id.img_photos)
    CircleImageView imgPhotos;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.img_king)
    ImageView imgKing;
    @BindView(R.id.tv_validity)
    TextView tvValidity;
    @BindView(R.id.tv_day_of_vip)
    TextView tvDayOfVip;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.btn_buying)
    Button btnBuying;
    @BindView(R.id.img_vip_authers)
    ImageView imgVipAuthers;
    @BindView(R.id.btn_abort_service)
    Button btnAbortService;
    @BindView(R.id.open_get_vip_layout)
    LinearLayout open_get_vip_layout;
    String phoneNum = null;
    boolean flag = false;
    String userId = null;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_openvip;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        openVipTopbar.setRightIsVisible(false);
        openVipTopbar.setLeftIsVisible(true);
        openVipTopbar.setOnTopbarClickListenter(this);
        tvValidity.setText(StringUtils.getCurrentTime(OpenVipActivity.this));
//        String string = ACache.get(this).getAsString("userinfo");
        String string = SharedPrefsUtils.getStringPreference(getApplicationContext(),"userinfo");

        if (!StringUtils.isNullOrEmpty(string)) {
            UserModel model = new Gson().fromJson(string, UserModel.class);
            if (model.getIsLogin()!=null&& model.getIsLogin().equals("true")){
                phoneNum = model.getPhoneNo();
                userId = model.getId();
                username.setText("账号：" + phoneNum.substring(0, phoneNum.length() - (phoneNum.substring(3)).length()) + "****" + phoneNum.substring(7));
                if (FILE_SAVEPATH != null) {
                    Glide.with(this).load(ACache.get(this).getAsString("img_url")).centerCrop().error(R.drawable.icon_head).into(imgPhotos);
                }
                if (!StringUtils.isNullOrEmpty(model.getHeadImg())){
                    Glide.with(this).load(HttpUtils.appendUrl(model.getHeadImg())).asBitmap().centerCrop().into(imgPhotos);
                }else{
                    imgPhotos.setImageDrawable(getResources().getDrawable(R.drawable.icon_head));
                }
                if (model.getIsVip().equals("1")) {
                    imgKing.setImageResource(R.drawable.icon_vip_opened);
                    open_get_vip_layout.setVisibility(View.GONE);
                    imgVipAuthers.setImageResource(R.drawable.img_vip_opened);
                    btnAbortService.setVisibility(View.VISIBLE);
                } else {
                    imgKing.setImageResource(R.drawable.icon_vip_unopened);
                    open_get_vip_layout.setVisibility(View.VISIBLE);
                    imgVipAuthers.setImageResource(R.drawable.img_vip_unopened);
                    btnAbortService.setVisibility(View.GONE);
                }
            }else{
                UnloginState();
            }
        } else {
            UnloginState();
        }



    }

    private void UnloginState() {
        btnAbortService.setVisibility(View.GONE);
        imgKing.setImageResource(R.drawable.icon_vip_unopened);
        open_get_vip_layout.setVisibility(View.VISIBLE);
        imgVipAuthers.setImageResource(R.drawable.img_vip_unopened);
        username.setText("尚未登录,请先登录");
    }

    @Override
    public void initViews() {
        if (flag) {
            imgKing.setImageResource(R.drawable.icon_vip_unopened);
            imgVipAuthers.setImageResource(R.drawable.img_vip_unopened);
        } else {
            imgKing.setImageResource(R.drawable.icon_vip_unopened);
            imgVipAuthers.setImageResource(R.drawable.img_vip_unopened);
        }
        btnBuying.setOnClickListener((View v) -> {
            String userinfo = ACache.get(this).getAsString("userinfo");
            if (StringUtils.isNullOrEmpty(userinfo)) {
                Toast.makeText(getApplicationContext(), "您尚未登录", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),"沃视频会员限时免费中",Toast.LENGTH_SHORT).show();
//                placeOrder();
            }
        });

    }

    /**
     * submit order
     */
    private void placeOrder() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("spid", "953");
        if (userId != null) {
            map.put("userid", userId);
            TLog.log("userId" + userId);
        } else {
            UnLoginHandler.unLogin(OpenVipActivity.this);
            return;
        }
        HttpApis.getPlaceOrder(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<PlaceOrder, RespHeader> resp = new ResponseObj<PlaceOrder, RespHeader>();
                ResponseParser.parse(resp, response, PlaceOrder.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    purchOrder(resp.getBody().getOrderid());
                } else {
                    Toast.makeText(getApplicationContext(), resp.getHead().getRspMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 支付
     * @param orderId
     */
    private void purchOrder(String orderId) {
        HashMap<String, Object> map = new HashMap<>();
        String string = ACache.get(this).getAsString("userinfo");
        if (StringUtils.isNullOrEmpty(string)) {
            UnLoginHandler.unLogin(OpenVipActivity.this);
        } else {
            UserModel model = new Gson().fromJson(string, UserModel.class);
            map.put("cellphone", model.getPhoneNo());
            map.put("spid", "953");
            HttpApis.purchOrder(map, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    TLog.log("error:" + e.getMessage());
                }

                @Override
                public void onResponse(String response, int id) {
                    TLog.log(response);
                    ResponseObj<String, RespHeader> resp = new ResponseObj<String, RespHeader>();
                    ResponseParser.parse(resp, response, String.class, RespHeader.class);
                    if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                        // TODO: 16/6/15 吊起  完成订单接口
                        finishOrder(orderId, userId);
                    } else {
                        Toast.makeText(getApplicationContext(), resp.getHead().getRspMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /**
     * 完成订单接口
     * @param orderId
     * @param userid
     */
    private void finishOrder(String orderId, String userid) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("userid", userid);
        HttpApis.finishOrder(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<String, RespHeader> resp = new ResponseObj<String, RespHeader>();
                ResponseParser.parse(resp, response, String.class, RespHeader.class);
                Toast.makeText(getApplicationContext(), resp.getHead().getRspMsg(), Toast.LENGTH_SHORT).show();

                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
                    // TODO: 16/6/15 UI 布局变更
                    imgKing.setImageResource(R.drawable.icon_vip_opened);
                    open_get_vip_layout.setVisibility(View.GONE);
                    imgVipAuthers.setImageResource(R.drawable.img_vip_opened);
                } else {
                    imgKing.setImageResource(R.drawable.icon_vip_unopened);
                    open_get_vip_layout.setVisibility(View.VISIBLE);
                    imgVipAuthers.setImageResource(R.drawable.img_vip_unopened);
                }
            }
        });
    }

    @Override
    public void initDatas() {
    }

    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {

    }
}
