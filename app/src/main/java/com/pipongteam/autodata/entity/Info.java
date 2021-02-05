package com.pipongteam.autodata.entity;

public class Info {
    private int id;
    private String securityCode;
    private String pointCode;
    private int isRegisted;

    public Info(String securityCode, String pointCode, int isRegisted) {
        this.securityCode = securityCode;
        this.pointCode = pointCode;
        this.isRegisted = isRegisted;
    }

    public Info(int id, String securityCode, String pointCode, int isRegisted) {
        this.id = id;
        this.securityCode = securityCode;
        this.pointCode = pointCode;
        this.isRegisted = isRegisted;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public String getPointCode() {
        return pointCode;
    }

    public void setPointCode(String pointCode) {
        this.pointCode = pointCode;
    }

}
