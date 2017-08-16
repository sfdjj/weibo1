package com.weibo.weibo.test;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by jwc on 2017/8/7.
 */

public class Main1 {

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()) {
            int n = sc.nextInt();
            long x1 = System.currentTimeMillis();
            ArrayList<Integer> list = new ArrayList<>();
            f(n,n,list);
            long x2 = System.currentTimeMillis();
            System.out.println(x2-x1);
        }
    }
    public static void f(int left,int right,ArrayList<Integer> list) {
        if(right==0) {
            print(list);
            return;
        }
        for(int i=Math.min(left,right);i>0;i--) {
            list.add(i);
            f(i,right-i,list);
            list.remove(list.size()-1);
        }
    }
    public static void print(ArrayList<Integer> list) {
        for(int l:list) {
            System.out.print(l+" ");
        }
        System.out.println();
    }
}
