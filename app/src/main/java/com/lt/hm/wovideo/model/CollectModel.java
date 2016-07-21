package com.lt.hm.wovideo.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/15
 */
public class CollectModel implements Serializable {

    /**
     * iid : 2
     * pid : 13
     * cid : eaf22
     * superUrl :
     * typeid : 2
     * name : 最好的我们
     * img : /recommendations/45a31bb07a9044418ff4558d64b6ec0a.jpg
     * fluentUrl : http://111.206.133.39:9910/video/wovideo/sp1/sp1.m3u8
     * standardUrl :
     * fkUrl :
     * blueUrl :
     * highUrl :
     * indexs : 1
     */

    private List<CollListBean> collList;

    public List<CollListBean> getCollList() {
        return collList;
    }

    public void setCollList(List<CollListBean> collList) {
        this.collList = collList;
    }

    public static class CollListBean {
        private String iid;//视频主信息ID
        private String pid;//单集ID
        private String cid;//主键 暂时无用
        private String superUrl;//超清源地址
        private String typeid;//视频类型
        private String name;//视频名称
        private String img;//封面图(竖)
        private String fluentUrl;//流畅源地址
        private String standardUrl;//标清源地址
        private String fkUrl;//4K源地址
        private String blueUrl; //蓝光源地址
        private String highUrl; //高清播放地址
        private String indexs;//第几集
        private String flag;

        public String getIid() {
            return iid;
        }

        public void setIid(String iid) {
            this.iid = iid;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getSuperUrl() {
            return superUrl;
        }

        public void setSuperUrl(String superUrl) {
            this.superUrl = superUrl;
        }

        public String getTypeid() {
            return typeid;
        }

        public void setTypeid(String typeid) {
            this.typeid = typeid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getFluentUrl() {
            return fluentUrl;
        }

        public void setFluentUrl(String fluentUrl) {
            this.fluentUrl = fluentUrl;
        }

        public String getStandardUrl() {
            return standardUrl;
        }

        public void setStandardUrl(String standardUrl) {
            this.standardUrl = standardUrl;
        }

        public String getFkUrl() {
            return fkUrl;
        }

        public void setFkUrl(String fkUrl) {
            this.fkUrl = fkUrl;
        }

        public String getBlueUrl() {
            return blueUrl;
        }

        public void setBlueUrl(String blueUrl) {
            this.blueUrl = blueUrl;
        }

        public String getHighUrl() {
            return highUrl;
        }

        public void setHighUrl(String highUrl) {
            this.highUrl = highUrl;
        }

        public String getIndexs() {
            return indexs;
        }

        public void setIndexs(String indexs) {
            this.indexs = indexs;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
    }
}
