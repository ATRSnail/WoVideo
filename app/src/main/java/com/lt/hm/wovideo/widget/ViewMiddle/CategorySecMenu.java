package com.lt.hm.wovideo.widget.ViewMiddle;

public class CategorySecMenu {
    private int id;
    private String type;
    private int parent_id;
    private String name;
    private int status;
    private int is_hot;
    private long update_time;
    private long create_time;
    private String cover_url;

    public CategorySecMenu() {
        super();
    }

    public CategorySecMenu(int id, String type, int parent_id, String name, int status, int is_hot, long update_time, long create_time,
            String cover_url) {
        super();
        this.id = id;
        this.type = type;
        this.parent_id = parent_id;
        this.name = name;
        this.status = status;
        this.is_hot = is_hot;
        this.update_time = update_time;
        this.create_time = create_time;
        this.cover_url = cover_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(int is_hot) {
        this.is_hot = is_hot;
    }

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    @Override
    public String toString() {
        return "CategorySecMenu [id=" + id + ", type=" + type + ", parent_id=" + parent_id + ", name=" + name + ", status=" + status
                + ", is_hot=" + is_hot + ", update_time=" + update_time + ", create_time=" + create_time + ", cover_url=" + cover_url + "]";
    }

}
