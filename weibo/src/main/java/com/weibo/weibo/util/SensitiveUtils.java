package com.weibo.weibo.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

/**
 * Created by jwc on 2017/7/28.
 */
@Component
public class SensitiveUtils implements InitializingBean {

    public static SensitiveTree root = new SensitiveTree(new HashMap<>());

    @Override
    public void afterPropertiesSet() throws Exception {
        String s = new String();
        File file = new File("E:/weibo/src/main/java/com/weibo/weibo/test/word.txt");
        try{
            FileInputStream fileInputStream = new FileInputStream(file);
            byte []a = new byte[fileInputStream.available()];
            fileInputStream.read(a);
            s = new String(a,"utf-8");
            System.out.println("s");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String a[] = s.split(" ");
        for(int i=0;i<a.length;i++) {
            addTreeNode(a[i]);
        }
    }
    public void addTreeNode(String str) {
        char a[] = str.toCharArray();
        SensitiveTree node = root;
        for(int i=0;i<a.length;i++) {
            if(node.getSensitiveTree(a[i])!=null) {
                node = node.getSensitiveTree(a[i]);
            }else {
                SensitiveTree p = new SensitiveTree(new HashMap<>());
                node.setSensitiveTree(a[i],p);
                node = node.getSensitiveTree(a[i]);
            }
        }
        node.setEnd();
    }

    public String sensitiveService(String s) {
        int left = 0;
        int right = 0;
        StringBuilder stringBuilder = new StringBuilder();
        SensitiveTree node = root;
        while(left<s.length()) {
            if(right==s.length()) {
                stringBuilder.append(s.charAt(left));
                left++;
                right = left;
                continue;
            }
            if(node.getSensitiveTree(s.charAt(right))==null) {
                stringBuilder.append(s.charAt(left));
                node = root;
                left++;
                right = left;
                continue;
            }
            if(node.getSensitiveTree(s.charAt(right)).end == false) {
                node = node.getSensitiveTree(s.charAt(right));
                right++;
                continue;
            }
            if(node.getSensitiveTree(s.charAt(right)).end == true) {
                for(int i=left;i<=right;i++) {
                    stringBuilder.append("*");
                }
                left = right+1;
                right = left;
                continue;
            }

        }
        return new String(stringBuilder);
    }

}
