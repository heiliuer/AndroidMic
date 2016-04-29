package com.heiliuer.androidmic_server.udpmulti;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
public class MulticastInfo {

    private String name;
    private String more;
    private int recordPort = 9999;
    private String recordIp;

    public String getRecordIp() {
        return recordIp;
    }

    public void setRecordIp(String recordIp) {
        this.recordIp = recordIp;
    }

    public MulticastInfo(String name, String more) {
        this.more = more;
        this.name = name;
    }

    public String getMore() {
        return more;
    }

    public int getRecordPort() {
        return recordPort;
    }

    public void setRecordPort(int recordPort) {
        this.recordPort = recordPort;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
