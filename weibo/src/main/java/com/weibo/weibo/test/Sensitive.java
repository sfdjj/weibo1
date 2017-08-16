package com.weibo.weibo.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jwc on 2017/7/26.
 */
public class Sensitive {
    public static void main(String args[]) {
        Sensitive sensitive = new Sensitive();
        TreeNode root = sensitive.buildTree();
        String s = "wneifnw fjiw ranod fweg random wefwabc ";
        sensitive.sensitiveService(s,root);
    }

    public void sensitiveService(String s,TreeNode root) {
        int left = 0;
        int right = 0;
        StringBuilder stringBuilder = new StringBuilder();
        TreeNode node = root;
        while(left<s.length()) {
            if(right==s.length()) {
                stringBuilder.append(s.charAt(left));
                left++;
                right = left;
                continue;
            }
            if(node.getTreeNode(s.charAt(right))==null) {
                stringBuilder.append(s.charAt(left));
                node = root;
                left++;
                right = left;
                continue;
            }
            if(node.getTreeNode(s.charAt(right)).end == false) {
                node = node.getTreeNode(s.charAt(right));
                right++;
                continue;
            }
            if(node.getTreeNode(s.charAt(right)).end == true) {
                for(int i=left;i<=right;i++) {
                    stringBuilder.append("*");
                }
                left = right+1;
                right = left;
                continue;
            }

        }
        System.out.println(stringBuilder);
    }

    public TreeNode buildTree() {
        TreeNode root = new TreeNode(new HashMap<>());
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
            addTreeNode(root,a[i]);
        }
        return root;
    }

    public void addTreeNode(TreeNode root,String str) {
        char a[] = str.toCharArray();
        TreeNode node = root;
        for(int i=0;i<a.length;i++) {
            if(node.getTreeNode(a[i])!=null) {
                node = node.getTreeNode(a[i]);
            }else {
                TreeNode p = new TreeNode(new HashMap<>());
                node.setTreeNode(a[i],p);
                node = node.getTreeNode(a[i]);
            }
        }
        node.setEnd();
    }
}
class TreeNode{
    boolean end = false;
    public Map<Character,TreeNode> map;
    public TreeNode(Map<Character,TreeNode> map) {
        this.map = map;
    }

    public void setEnd() {
        this.end = true;
    }
    public void setTreeNode(Character character,TreeNode treeNode) {
        map.put(character,treeNode);
    }
    public TreeNode getTreeNode(Character character) {
        return map.get(character);
    }
}
