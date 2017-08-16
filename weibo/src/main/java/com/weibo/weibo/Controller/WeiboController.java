package com.weibo.weibo.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weibo.weibo.aspect.LoginRequired;
import com.weibo.weibo.async.EventModel;
import com.weibo.weibo.async.EventType;
import com.weibo.weibo.async.Producter;
import com.weibo.weibo.model.HostHolder;
import com.weibo.weibo.model.Microblogging;
import com.weibo.weibo.model.User;
import com.weibo.weibo.service.*;
import com.weibo.weibo.util.WeiboUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by jwc on 2017/7/24.
 */
@Controller
public class WeiboController {

    @Autowired
    QiniuService qiniuService;

    @Autowired
    MicrobloggingService microbloggingService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    Producter producter;

    @Autowired
    SearchService searchService;

    @RequestMapping(path={"/uploadImage"},method = {RequestMethod.POST})
    @LoginRequired(loginRequired = true)
    @ResponseBody
    public String uploadImage( MultipartFile[] file,
                              @RequestParam(value = "type",defaultValue = "1") int type) {
        String fileName = "";
        for(MultipartFile file1:file) {
            try {
                Map<String, Object> map;
                if(type!=0) {
                     map = qiniuService.uploadImage(file1);
                }else {
                     map = microbloggingService.uploadImage(file1);
                }
                 if (!map.containsKey("fileUrl")) {
                    return WeiboUtil.getJSONString(1, map);
                }
                if(type!=0) {
                    fileName += WeiboUtil.QINIU_IMAGE_DOMAIN + map.get("fileUrl").toString();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code",0);
                    jsonObject.put("url",fileName);
                    return jsonObject.toJSONString();
                }
                else {
                    fileName+=WeiboUtil.IMAGE_DIR+map.get("fileUrl").toString();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code",0);
                    jsonObject.put("url",fileName);
                    return jsonObject.toJSONString();
                }
            } catch (Exception e) {
                System.out.println("上传失败:" + e.getMessage());
                return WeiboUtil.getJSONString(1, "上传失败");
            }
        }
        return WeiboUtil.getJSONString(0,fileName.substring(0,fileName.length()-1)).toString();
    }

    @RequestMapping(path = {"/image"})
    //@LoginRequired(loginRequired = true)
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                           HttpServletResponse httpServletResponse) {
        try {
            httpServletResponse.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new File(WeiboUtil.IMAGE_DIR+imageName)),httpServletResponse.getOutputStream());
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @RequestMapping(path = {"/addWeibo"})
    @LoginRequired(loginRequired = true)
    @ResponseBody
    public String addWeibo(@RequestParam("content") String content,
                           @RequestParam("images") String image) {

        try {
            User user = hostHolder.getUser();
            Map<String, Object> map = microbloggingService.addMicroblogging(content, image,user.getId());
            if (!map.containsKey("success")) {
                return WeiboUtil.getJSONString(1, map);
            }
            producter.pushEvent(new EventModel().setEventType(EventType.PUSH).
                    setActorId(user.getId()).setEntityType(WeiboUtil.ENTITY_TYPE_WEIBO).
                    setEntityId(Integer.parseInt(map.get("id").toString())));
            return WeiboUtil.getJSONString(0, map);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return WeiboUtil.getJSONString(1,"发布异常");
        }
    }

    @RequestMapping(path={"/profile/{uid}"})
    public String userIndex(Model model,
                            @PathVariable("uid") int userId) {
      try {
          model.addAttribute("weibos", getMicrobloggingService(10, 0, userId));
      }catch (Exception e) {
          System.out.println(e.getMessage());
      }
        return "profile";
    }

    @RequestMapping(path={"/","/index"})
    public String index(Model model) {
        /*List<Microblogging> list = microbloggingService.getMicroblogginByUser(0,10,0);
        JsonArray jsonArray = new JsonArray();
        for(Microblogging microblogging:list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("microblogging",microblogging);
            String image[] = microblogging.getImage().split("-");
            jsonObject.put("image",image);

        }
        return "";
        */
//        model.addAttribute("modelViews",getMicrobloggingService(10,0,0));
//        if(hostHolder.getUser()!=null) {
//            model.addAttribute("user",hostHolder.getUser());
//        }
        try {
            model.addAttribute("weibos", getMicrobloggingService(10,0,0));

        } catch (Exception e) {

        }
        return "index";
    }

    @RequestMapping(path = {"/search"})
    public String search(Model model,@RequestParam("q") String query) {
        try{
            JSONArray vos = new JSONArray();
            List<Integer> list = searchService.search(query);
            for(int id:list) {
                Microblogging microblogging = microbloggingService.getMicrobloggingById(id);
                JSONObject vo = new JSONObject();
                vo.put("wb",microblogging);
                vo.put("user",userService.getUserById(microblogging.getUserId()));
                vo.put("images",microblogging.getImage().split("\\|"));
                vo.put("liked",false);
                vo.put("followed",false);
                vos.add(vo);
            }
            model.addAttribute("weibos",vos);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "profile";
    }

    public JSONArray getMicrobloggingService(int limit, int offset, int userId) {
        JSONArray vos = new JSONArray();
        List<Microblogging> list = microbloggingService.getMicroblogginByUser(userId,offset,limit);
        for(Microblogging microblogging:list) {
            JSONObject vo = new JSONObject();
            vo.put("wb",microblogging);
            JSONObject jsonObject = new JSONObject();
            vo.put("user",userService.getUserById(microblogging.getUserId()));
            vo.put("images",microblogging.getImage().split("\\|"));
            vo.put("liked",false);
            vo.put("followed",false);
            vos.add(vo);
        }
        //System.out.println(vos.toJSONString());
        return vos;
//        List<Microblogging> list = microbloggingService.getMicroblogginByUser(userId,offset,limit);
//        List<Map<String,Object>> modelViews = new ArrayList<>();
//        for(Microblogging microblogging:list) {
//            List<Comment> comments = commentService.getCommentList(microblogging.getId(),WeiboUtil.ENTITY_TYPE_WEIBO
//                    ,WeiboUtil.DEFAULT_OFFSET,WeiboUtil.DEFAULT_LIMIT);
//            List<Map<String,Object>> commentsList = new ArrayList<>();
//            for(Comment comment :comments) {
//                Map<String,Object> map1 = new HashMap<>();
//                map1.put("comment",comment);
//                User commentUser = userService.getUserById(comment.getUserId());
//                commentUser.setSalt(null);
//                commentUser.setPassword(null);
//                map1.put("commentUser",commentUser);
//                commentsList.add(map1);
//            }
//            Map<String,Object> map = new HashMap<>();
//            String image[] = microblogging.getImage().split("_");
//            map.put("image",image);
//            map.put("microbloggin",microblogging);
//            User user = userService.getUserById(microblogging.getUserId());
//            user.setPassword(null);
//            user.setSalt(null);
//            map.put("user",user);
//            map.put("comments",commentsList);
//            modelViews.add(map);
//        }
//        return modelViews;
    }

}
