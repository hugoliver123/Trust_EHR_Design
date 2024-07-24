package com.test.blockTest;

import com.audit.BlockCheck;

public class TestBlockCheck {

    //return 0表示DB中查无此人。
    //如果查询者是病人，query只能为自己的ID，否则报错，返回-1
    //如果成功 return 1

    //0表示某个本应该存在的file不存在或者文件内容有问题，
    //1表示签名是对的————正常的
    //2表示issuer不存在
    //3表示issuer的pk与数据库对不上
    //4表示签名不对

    //Block check
    // -1表示文件夹无file，0表示某个本应该存在的file不存在或者文件内容有问题，1表示true，2表示false

    public static void main(String[] args) {

        //BlockCheck.queryLog(421, "AnyuYang", 0);
//        try{
//            String strings = BlockCheck.queryLog(1024, "USC", 0).getAuditMsg();
//            String newStr = strings.replace("||", "\\t");
//            //System.out.println(newStr);
//            // queryID是audit company ，query 是0 就查所有
//            // query是某个数字就查这个数字对应的病人的log
//            // queryID是病人的话， query必须和queryID一致
//        }catch (Exception e){
//            System.out.println("区块链被篡改");
//        }
        //BlockCheck.BlockCheck();
        //System.out.println(BlockCheck.BlockCheck());
        System.out.println(BlockCheck.SignatureCheck(1000));
    }
}
