package com.test;

import com.util.Merkle;

public class TeseMerkel {
    public static void main(String[] args) {
        Merkle merkle = new Merkle("/Users/yuanxuteng/Desktop/EHRrecord",
                "/Users/yuanxuteng/Desktop/log/tree.root");
        //merkle.updateRoot();
        System.out.println(merkle.checkRoot());
    }
}
