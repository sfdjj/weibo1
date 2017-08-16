package com.weibo.weibo.util;


import java.util.Map;

/**
 * Created by jwc on 2017/7/29.
 */
public class SensitiveTree {
    boolean end = false;
    public Map<Character,SensitiveTree> map;
    public SensitiveTree(Map<Character,SensitiveTree> map) {
        this.map = map;
    }

    public void setEnd() {
        this.end = true;
    }
    public void setSensitiveTree(Character character,SensitiveTree sensitiveTree) {
        map.put(character,sensitiveTree);
    }
    public SensitiveTree getSensitiveTree(Character character) {
        return map.get(character);
    }
}
