package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.base.BaseFragment;
import com.lt.hm.wovideo.fragment.VipItemPage;
import com.lt.hm.wovideo.http.HttpApis;
import com.lt.hm.wovideo.http.RespHeader;
import com.lt.hm.wovideo.http.ResponseCode;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.http.parser.ResponseParser;
import com.lt.hm.wovideo.model.TypeList;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.widget.SecondTopbar;
import com.lt.hm.wovideo.widget.ViewPagerIndicator;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/8
 * @deprecated
 */
@Deprecated
public class ClassDetailPage extends BaseActivity implements SecondTopbar.myTopbarClicklistenter {


    @BindView(R.id.class_view_indicator)
    ViewPagerIndicator classViewIndicator;
    @BindView(R.id.class_view_page)
    ViewPager classViewPage;
    @BindView(R.id.class_details_topbar)
    SecondTopbar classDetailsTopbar;
    String lx;
    String sx;
    String dq;
    String nd;
    String id;
    private List<String> mTitles;
    private List<BaseFragment> fragments = new ArrayList<>();
    private FragmentPagerAdapter mAdapter;
    private List<TypeList.TypeListBean> mList;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_class_details;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("key")) {
            String title = bundle.getString("key");
            classDetailsTopbar.setTvTitle(title);
        }
        if (bundle.containsKey("id")) {
            id = bundle.getString("id");
        }

        mList = new ArrayList<>();
        mTitles = new ArrayList<>();
    }


    @Override
    public void initViews() {
        classDetailsTopbar.setRightIsVisible(true);
        classDetailsTopbar.setLeftIsVisible(true);
        classDetailsTopbar.setOnTopbarClickListenter(this);
    }

    @Override
    public void initDatas() {
        getClassInfos();
    }

    private void initTop() {
        for (int i = 0; i < mList.size(); i++) {

            mTitles.add(mList.get(i).getTypeName());
        }
        for (int i = 0; i < mTitles.size(); i++) {
            BaseFragment hotfragment = new VipItemPage();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            hotfragment.setArguments(bundle);
            fragments.add(hotfragment);
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
        };
        classViewIndicator.setVisibleTabCount(5);
        classViewIndicator.setTabItemTitles(mTitles);
        classViewPage.setAdapter(mAdapter);
        classViewIndicator.setViewPager(classViewPage, 0);
    }

    private void getClassInfos() {
        HashMap<String, Object> map = new HashMap<>();
        HttpApis.getClassesInfo(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log("error:" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
                ResponseObj<TypeList, RespHeader> resp = new ResponseObj<TypeList, RespHeader>();
                ResponseParser.parse(resp, response, TypeList.class, RespHeader.class);
                if (resp.getHead().getRspCode().equals("0")) {
                    TLog.log(resp.toString());
                    mList.addAll(resp.getBody().getTypeList());
                    initTop();

                } else {
                    TLog.log(resp.getHead().getRspMsg());
                }

            }
        });
    }

    public void getSelectedDatas(String lx, String sx, String dq, String nd) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("typeid", id);
        map.put("pageNum", 1);
        map.put("numPerPage", 60);
        map.put("isvip", "0");
        map.put("lx", lx);
        map.put("sx", sx);
        map.put("dq", dq);
        map.put("nd", nd);

        HttpApis.getListByType(map, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TLog.log(e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                TLog.log(response);
//                ResponseObj<VideoList,RespHeader> resp= new ResponseObj<VideoList, RespHeader>();
//                ResponseParser.parse(resp,response,VideoList.class,RespHeader.class);
//                if (resp.getHead().getRspCode().equals(ResponseCode.Success)) {
//                    // TODO: 16/7/9 待定
//
//                }
            }
        });
    }


    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {
        // TODO: 16/7/3 侦听 当前所在页面为哪一个，然后弹出对应的菜单项
//        SelectMenuPop pop = new SelectMenuPop(this, Integer.parseInt(id));
////        pop.showPopupWindow(classDetailsTopbar);
//        pop.setListener(new SelectMenuPop.OnRadioClickListener() {
//            @Override
//            public void clickListener(String key, String value) {
//                if (key.equals("类型")) {
//                    if (value.equals("全部")) {
//                        lx = "";
//                    } else {
//                        lx = value;
//                    }
//                }
//                if (key.equals("属性")) {
//                    if (value.equals("全部")) {
//                        sx = "";
//                    } else {
//                        sx = value;
//                    }
//                }
//                if (key.equals("地区")) {
//                    if (value.equals("全部")) {
//                        dq = "";
//                    } else {
//                        dq = value;
//                    }
//                }
//                if (key.equals("年代")) {
//                    if (value.equals("全部")) {
//                        nd = "";
//                    } else {
//                        nd = value;
//                    }
//                }
//                getSelectedDatas(lx,sx,dq,nd);
//
//            }
//        });
    }
}
