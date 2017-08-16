package com.weibo.weibo.test;

/**
 * Created by jwc on 2017/8/8.
 */
class Base {
    Base() {
        System.out.print("Base");
    }
}
public class sss extends Base {
    public static void main( String[] args ) {
        new sss();
        //调用父类无参的构造方法
        new Base();
    }
}
