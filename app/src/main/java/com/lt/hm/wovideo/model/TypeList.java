package com.lt.hm.wovideo.model;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/13
 */
public class TypeList {
    private List<TypeListBean> typeList;

    public List<TypeListBean> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<TypeListBean> typeList) {
        this.typeList = typeList;
    }

    @Override
    public String toString() {
        return "TypeList{" +
                "typeList=" + typeList +
                '}';
    }

    public static class TypeListBean {
        private String typeName;//分类名称
        private String id;//主键
        private Object utime;
        private String state;
        private String img;//封面图片路径
        private long ctime;

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getUtime() {
            return utime;
        }

        public void setUtime(Object utime) {
            this.utime = utime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        @Override
        public String toString() {
            return "TypeListBean{" +
                    "typeName='" + typeName + '\'' +
                    ", id=" + id +
                    ", utime=" + utime +
                    ", state='" + state + '\'' +
                    ", img='" + img + '\'' +
                    ", ctime=" + ctime +
                    '}';
        }
    }
}
