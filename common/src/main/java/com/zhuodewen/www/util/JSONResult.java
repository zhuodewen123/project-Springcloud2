package com.zhuodewen.www.util;


public class JSONResult {
    private boolean success = true;

    private String msg;

    //成功时返回的数据
    private Object result;

    public JSONResult mark(String msg) {
        this.msg = msg;
        this.success = false;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
