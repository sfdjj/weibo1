package com.weibo.weibo.service;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.weibo.weibo.util.WeiboUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by jwc on 2017/7/24.
 */
@Service
public class QiniuService {

    String accessKey = "zWiQfr69ZFL3y9465t326AegoddktRL-R1i0chi0";
    String secretKey = "7nrgroHHDOYAifKkM5f4Mb6pEOn_8tgbGI_m3gqo";
    String bucket = "rilegou";

    //构造一个带指定Zone对象的配置类
  //  Configuration cfg = new Configuration(Zone.zone1());
    //...其他参数参考类注释
    UploadManager uploadManager = new UploadManager();

    public Map<String,Object> uploadImage(MultipartFile file) throws IOException{
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
        try {
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(file.getBytes(),fileName,upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                map.put("fileUrl",fileName);
                return map;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                map.put("msg","七牛上传失败");
                return map;
            }


        } catch (UnsupportedEncodingException ex) {
            map.put("msg","七牛异常");
        }
        map.put("msg","未知");
        return map;

    }

}
