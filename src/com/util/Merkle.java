package com.util;

import com.entity.User;
import com.mapper.userMapper;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Merkle {

    private String hashposition;
    private String foldername;
    private String key;
    public Merkle(String foldernamevar,String hashpositionvar){
        this.foldername = foldernamevar;
        this.hashposition = hashpositionvar;

        SqlSession session = getSqlSession.creatSqlSession();
        userMapper user_mapper = session.getMapper(userMapper.class);
        User user = user_mapper.queryUserByName("genesis");
        //System.out.println(user.getUserPassword());
        this.key = user.getUserPassword();
    }
    public void updateRoot()
    {
        String merkleroot = folderHash();
        // System.out.println(merkleroot);
        merkleroot = AES.aes_encrypt(merkleroot,key);
        FileWriter writer = null;
        try {
            writer = new FileWriter(hashposition, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.write(merkleroot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public boolean checkRoot()
    {
        File file = new File(hashposition);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String abc =  scanner.nextLine();
        String oldhash = AES.aes_decrypt(abc,key);
        String newhash = folderHash();
        // System.out.println(oldhash);
        // System.out.println(newhash);
        return oldhash.equals(newhash);
    }


    private String merkleRoot(ArrayList<String> oldhashlist)
    {
        if (oldhashlist.size()==1)
        {
            return oldhashlist.get(0);
        }

        while (oldhashlist.size()>1)
        {
            ArrayList<String> newhashlist = new ArrayList<String>();
            int i = oldhashlist.size();
            int j = 0;
            for ( j = 0; j < i; j = j+2 )
            {
                if ( j == i-1)
                {
                    newhashlist.add(oldhashlist.get(j));
                }
                else
                {
                    String abc = oldhashlist.get(j) + oldhashlist.get(j+1);
                    MessageDigest md = null;
                    try {
                        md = MessageDigest.getInstance("SHA-256");
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                    md.update(abc.getBytes());
                    byte [] digest = md.digest();
                    newhashlist.add(bytesToHex(digest));
                }

            }
            oldhashlist = newhashlist;
        }
        return oldhashlist.get(0);
    }
    private String folderHash(){
        File folder = new File(foldername);
        ArrayList<String> filehashs = new ArrayList<String>();
        File[] files = folder.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return Long.valueOf(o1.lastModified()).compareTo(o2.lastModified());
            }
        });
        for (File i : files)
        {
            if (i.isFile()) {
                String abc = fileHash(i);
                filehashs.add(abc);
            }
        }
        String root = merkleRoot(filehashs);
        return root;
    }
    private static String fileHash(File filename){
        try {
            FileInputStream file = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        MessageDigest md= null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        FileInputStream inputfile= null;
        try {
            inputfile = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        byte[] buffer=new byte[8192];
        int numBytesRead;
        while (true) {
            try {
                if (!((numBytesRead=inputfile.read(buffer)) != -1)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            md.update(buffer, 0, numBytesRead);
        }
        try {
            inputfile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = md.digest();
        String str = bytesToHex(hash);
        return str;
    }
    public static String bytesToHex(byte[] inputbytes) {
        StringBuilder aaa = new StringBuilder();
        for (byte b : inputbytes) {
            aaa.append(String.format("%02x", b));
        }
        return aaa.toString();
    }

}
