package com.entity.vo;

public class AuditMsgModel {
    private int code;
    private String auditMsg;

    public AuditMsgModel(int code, String auditMsg) {
        this.code = code;
        this.auditMsg = auditMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getAuditMsg() {
        return auditMsg;
    }

    public void setAuditMsg(String auditMsg) {
        this.auditMsg = auditMsg;
    }
}
