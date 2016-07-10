package com.lt.hm.wovideo.http;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/12
 */
public class ResponseObj<T,K> {
    private T body;
    private K head;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public K getHead() {
        return head;
    }

    public void setHead(K head) {
        this.head = head;
    }

    @Override
    public String toString() {
        return "ResponseObj{" +
                "body=" + body +
                ", head=" + head +
                '}';
    }
}
