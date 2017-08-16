package com.weibo.weibo.test;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.util.HtmlUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by jwc on 2017/7/19.
 */
public class test {
    public static void print(int i,Object object) {
        System.out.println(String.format("%d:"+object.toString(),i));
    }
    public static void StringUtil() {
        String a="";
        String b = "abc ss /t s";
        String c = "afs";
        String test = "This is a test of the abbreviation.";
        String[] s1 = new String[]{"111","abc","sgw"};
        print(1,StringUtils.isBlank(a));
        print(2,StringUtils.contains(b,a));
        print(3,StringUtils.abbreviate(b,4));
        print(4,StringUtils.leftPad(a,3,'#'));
        print(5,StringUtils.trimToNull(b));
        print(6,StringUtils.abbreviate(test,6));
        print(7,StringUtils.abbreviateMiddle(test,c,5));
        print(8,StringUtils.join(s1,"%%-"));
        print(9,StringUtils.join(s1,'-'));
        print(10,StringUtils.join(b,c));
        print(11,StringUtils.contains("aa","bb"));
        print(12,StringUtils.capitalize(b));
        print(13,StringUtils.center(b,20,"ss"));
        print(14,StringUtils.chomp("afeg\n"));
        print(15,StringUtils.containsWhitespace(c));
        print(16,StringUtils.chop("afeg"));
        print(17,StringUtils.containsOnly(b,"a"));
        print(18,StringUtils.getCommonPrefix(b));
        print(19,StringUtils.defaultString(b));
    }
    public static void DatetimeUtil(){
        Date date = new Date();
        print(1,DateUtils.addDays(date,1));
        print(2,DateUtils.addHours(date,2));
        print(3,DateUtils.addWeeks(date,3));
        print(4,DateUtils.ceiling(date,1));
        print(5,DateUtils.getFragmentInDays(date,1));
        print(6,DateUtils.truncate(date,1));
    }

    public static void HtmlUtil() {
        String ss = "<br>we<br>";
        print(1,HtmlUtils.htmlEscape("<br>we<br>"));
        String s = HtmlUtils.htmlEscape("<br>we<br>");
        print(2,HtmlUtils.htmlEscapeDecimal("<br>we<br>"));
        print(3,HtmlUtils.htmlUnescape(ss));
        print(4,HtmlUtils.htmlEscapeHex(ss));
    }

    public static void JSONUtils() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("aaa","aaa");
        jsonObject.put("www","wgw");
        Map<String,String> map = new HashMap<>();
        map.put("123","123");
        map.put("456","456");
        jsonObject.put("rrr",map);
        String c[] = new String []{"122","232","ewf"};
        jsonObject.put("c",c);
        print(1,jsonObject);
        String a = jsonObject.toJSONString();
        print(2,a);
        print(3,jsonObject.getJSONObject("rrr"));
        print(4,jsonObject.getJSONArray("c"));
    }

    public static void XmlUtil(){
        String s = new String();
        File file = new File("E:/code/nowcode/weibo/pom.xml");
        try{
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);
            s=new String(buffer,"utf-8");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //System.out.println(s);
        try{
            Document document = DocumentHelper.parseText(s);
            Element root = document.getRootElement();
            //Document  document  = DocumentHelper.createDocument();
            //Element root = document.addElement(s);
            //Attribute attribute = root.attribute("modelVersion");
            //print(0,attribute.getText());
            //print(1,root.getStringValue());
            //print(2,DocumentHelper.selectNodes("project",root));
            //print(2,DocumentHelper.createElement("project").getName());
            //print(3,DocumentHelper.createText("project").getName());
            /*for(Iterator iterator = root.elementIterator(); iterator.hasNext();) {
                Element element = (Element)iterator.next();
                print(4,element.getStringValue());
            }*/
           s(root,0);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void s(Element root,int i) {
        if(root==null) {
            return ;
        }
        for(int j=0;j<i;j++) {
            System.out.print(" ");
        }
        boolean flag = false;
        Iterator iterator = root.elementIterator();
        if(iterator.hasNext()) {
            System.out.println("<"+root.getName()+">"+root.getTextTrim());
        }else {
            System.out.print("<"+root.getName()+">"+root.getTextTrim());
        }
        while(iterator.hasNext()) {
            flag = true;
            Element element = (Element)iterator.next();
            s(element,i+1);
        }
        if(flag) {
            for(int j=0;j<i;j++) {
                System.out.print(" ");
            }
        }
        System.out.println("</"+root.getName()+">");
    }

    public static void main(String args[]) {
        //StringUtil();
       //DatetimeUtil();
        //HtmlUtil();
        //JSONUtils();
        XmlUtil();
    }

}
