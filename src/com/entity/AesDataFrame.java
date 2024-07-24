package com.entity;

public class AesDataFrame {
    private String name;
    private String key;
    private String builder;
    private String shaHash;
    private String ehrMac;

    public AesDataFrame(String name, String key, String builder, String shaHash, String ehrMac) {
        this.name = name;
        this.key = key;
        this.builder = builder;
        this.shaHash = shaHash;
        this.ehrMac =ehrMac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getShaHash(){ return shaHash;}

    public String getEhrMac(){ return ehrMac;}
}
