package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/15
 */
public class BillList {

    /**
     * id : Gx2HAweOokeJRvVSroWsDXJ5tvyCG2Yw
     * utime : 1465695918000
     * goodsId : 953
     * moneys : 15
     * orderNo : 747840abd5324163b6225c8cad5c76da
     * userId : naOfSB7LhclfjHZfGx5PsrfmnD2GpuIB
     * state : 2
     * goods : null
     * user : null
     * ctime : 1465295424000
     */

    private List<OrderListBean> orderList;

    public List<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public static class OrderListBean {
        private String id;
        private long utime;
        private String goodsId;
        private int moneys;
        private String orderNo;
        private String userId;
        private String state;//订单状态 1未付款 2已付款 3退订 4已关闭
        private Object goods;
        private Object user;
        private long ctime;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getUtime() {
            return utime;
        }

        public void setUtime(long utime) {
            this.utime = utime;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public int getMoneys() {
            return moneys;
        }

        public void setMoneys(int moneys) {
            this.moneys = moneys;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Object getGoods() {
            return goods;
        }

        public void setGoods(Object goods) {
            this.goods = goods;
        }

        public Object getUser() {
            return user;
        }

        public void setUser(Object user) {
            this.user = user;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }
    }
}
