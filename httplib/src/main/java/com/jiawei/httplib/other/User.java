package com.jiawei.httplib.other;

/**
 * Created by jiawei on 2017/6/19.
 */

public class User {

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

    @Override
    public String toString() {
        return "User{" +
                "name=" + name +
                ", timestamp=" + timestamp +
                '}';
    }
}
