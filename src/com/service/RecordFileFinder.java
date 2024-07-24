package com.service;

import com.entity.RecordFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.*;

public class RecordFileFinder {

    public static ArrayList<RecordFile> getAllEhrFiles() {
       File dir = new File("/Users/yuanxuteng/Desktop/EHRrecord");
       ArrayList <RecordFile> ehrdFile = new ArrayList<>();
       File [] files = dir.listFiles();
       for(File file: files){
           if(file.getAbsolutePath().endsWith("ehrd")){
               try {
                   FileTime creatTime = Files.readAttributes(Paths.get(file.getAbsolutePath()), BasicFileAttributes.class).creationTime();
                   String pattern = "yyyy-MM-dd HH:mm:ss";
                   SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                   String formatted = simpleDateFormat.format( new Date( creatTime.toMillis() ) );//获取和转换时间

                   int pId = Integer.parseInt(file.getName().split("-")[0]); //获取病人id

                   RecordFile recordFile = new RecordFile(file.getName(), pId, formatted);
                   ehrdFile.add(recordFile);
               } catch (IOException e) {
                   e.printStackTrace();
               }//获取创建时间
           }
       }
       Collections.sort(ehrdFile);
       System.out.println(ehrdFile.toString());
       return ehrdFile;
    }
}
