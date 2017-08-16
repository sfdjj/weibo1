package com.weibo.weibo.test;

import java.util.*;

/**
 * Created by jwc on 2017/8/7.
 */
public class Main {
    private static ArrayList<Integer> list = new ArrayList<>();
    private static Set<String> set = new HashSet<>();
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()) {
            int n = sc.nextInt();
            long x1 = System.currentTimeMillis();
            re(n);
            for(String s:set) {
                System.out.println(s);
            }
            long x2 = System.currentTimeMillis();
            System.out.println(x2-x1);
        }
    }
    public static void re(int n) {
        if(n==0) {
            int a[] = new int[list.size()];
            for(int i=0;i<list.size();i++) {
                a[i] = list.get(i);
            }
            Arrays.sort(a);
            String s = "";
            for(int i=0;i<a.length;i++) {
                s=s+a[i]+"+";
            }
            s = s.substring(0,s.length()-1);
            set.add(s);
//            for(int i=0;i<list.size()-1;i++) {
//                System.out.print(list.get(i)+"+");
//            }
//            System.out.println(list.get(list.size()-1));
            return;
        }
        if(n<0) {
            return;
        }
        for(int i=1;i<=n;i++) {
            list.add(i);
//            if(list.get(0)>x/2) {
//                return;
//            }
            re(n-i);
            list.remove(list.size()-1);
        }
    }
}
