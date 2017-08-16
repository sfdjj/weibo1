package com.weibo.weibo.service;

import com.weibo.weibo.dao.MicrobloggingDAO;
import com.weibo.weibo.model.Microblogging;
import com.weibo.weibo.util.JedisUtils;
import com.weibo.weibo.util.SensitiveUtils;
import com.weibo.weibo.util.WeiboUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by jwc on 2017/7/24.
 */
@Service
public class MicrobloggingService {
    @Autowired
    MicrobloggingDAO microbloggingDAO;


    @Autowired
    SensitiveUtils sensitiveUtils;

    @Autowired
    JedisAdapterService jedisAdapterService;


    public Microblogging getMicrobloggingById(int id) {
        return microbloggingDAO.selectMicroblogginById(id);
    }

    public Map<String,Object> uploadImage(MultipartFile file) {
        Map<String,Object> map = new HashMap<>();
        int postfixIndex = file.getOriginalFilename().lastIndexOf(".");
        if(postfixIndex<0) {
            map.put("msg","图片格式不正确");
        }
        String postfix = file.getOriginalFilename().substring(postfixIndex+1);
        if(!WeiboUtil.isImage(postfix)) {
            map.put("msg","图片格式不正确");
            return map;
        }
        String fileName = UUID.randomUUID().toString().replaceAll("-","")+"."+postfix;
        try{
            Files.copy(file.getInputStream(),new File(WeiboUtil.IMAGE_DIR+fileName).toPath());
            map.put("fileUrl",fileName);
            return map;
        }catch (Exception e) {
            map.put("msg","上传异常");
            return map;
        }
    }

    public Map<String,Object> addMicroblogging(String content,String image,int userId) {
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isBlank(content)) {
            map.put("msg","不能发布空内容");
        }
        content = sensitiveUtils.sensitiveService(content);
        Microblogging microblogging = new Microblogging();
        microblogging.setUserId(userId);
        microblogging.setContent(content);
        microblogging.setImage(image);
        microblogging.setCommentCount(0);
        microblogging.setLikeCount(0);
        microblogging.setStatus(0);
        microblogging.setCreatedDate(new Date());
        microbloggingDAO.addMicroblogging(microblogging);
        int id = microblogging.getId();
        map.put("id",id);
        map.put("success","发布成功");
        return map;
    }

    public List<Microblogging> getMicroblogginByFollow(int userId) {
        List<Microblogging> list = new ArrayList<>();
        List<Integer> list1 = new ArrayList<>();
        String key = JedisUtils.getPushKey(userId);
        while(jedisAdapterService.llen(key)>0) {
            int id = Integer.parseInt(jedisAdapterService.lpop(key));
            list1.add(id);
        }
        return list;
    }

    public List<Microblogging> getMicroblogginByUser(int userId,int offset,int limit) {
        return microbloggingDAO.selectByUserIdAndOffset(offset,limit,userId);
    }

}
