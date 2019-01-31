package com.app.dixon.resourceparser.core.pub.parser;

/**
 * Created by dixon.xu on 2019/1/31.
 * <p>
 * 错误信息封装
 */

public class ParseError {

    private String msg;

    public ParseError() {
    }

    public ParseError(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
