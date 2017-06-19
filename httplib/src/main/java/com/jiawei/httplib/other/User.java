package com.jiawei.httplib.other;

import java.util.List;

/**
 * Created by jiawei on 2017/6/19.
 */

public class User {


    /**
     * ret : 200
     * msg : 有心课堂,传递给你的不仅仅是技术✈️
     * data : [{"name":0,"timestamp":1497856497000}]
     */

    private int ret;
    private String msg;
    private List<DataBean> data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : 0
         * timestamp : 1497856497000
         */

        private int name;
        private long timestamp;

        public int getName() {
            return name;
        }

        public void setName(int name) {
            this.name = name;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "ret=" + ret +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
