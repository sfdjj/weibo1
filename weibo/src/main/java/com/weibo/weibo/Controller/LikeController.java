package com.weibo.weibo.Controller;

import com.weibo.weibo.aspect.LoginRequired;
import com.weibo.weibo.async.EventModel;
import com.weibo.weibo.async.EventType;
import com.weibo.weibo.async.Producter;
import com.weibo.weibo.model.HostHolder;
import com.weibo.weibo.model.User;
import com.weibo.weibo.service.CommentService;
import com.weibo.weibo.service.LikeService;
import com.weibo.weibo.service.MicrobloggingService;
import com.weibo.weibo.util.WeiboUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by jwc on 2017/7/29.
 */
@Controller
public class LikeController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    MicrobloggingService microbloggingService;

    @Autowired
    CommentService commentService;

    @Autowired
    Producter producter;

    @RequestMapping(path = {"/dislike"})
    @LoginRequired(loginRequired = true)
    @ResponseBody
    public String dislike(@RequestParam("entityType") int entityType,
                       @RequestParam("entityId") int entityId) {
        try {
            User user = hostHolder.getUser();
///            if (user == null) {
//                return WeiboUtil.getJSONString(1, "你还没有登陆");
//            }
            Map<String, Object> map = likeService.dislike(entityType, entityId, user.getId());
            if (map.containsKey("success")) {
                return  WeiboUtil.getJSONString(0,"取消点赞成功");
            }
            return WeiboUtil.getJSONString(1,map);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return WeiboUtil.getJSONString(1,"取消点赞失败");
        }

    }

    @RequestMapping(path={"/like"})
    @LoginRequired(loginRequired = true)
    @ResponseBody
    public String like(@RequestParam("entityType") int entityType,
                       @RequestParam("entityId") int entityId) {
        try {
            User user = hostHolder.getUser();
//            if (user == null) {
//                return WeiboUtil.getJSONString(1, "你还没有登陆");
//            }
            Map<String, Object> map = likeService.like(entityType, entityId, user.getId());
            if (map.containsKey("success")) {
                int actorId = user.getId();
                int entityOwner = 0;
                if(entityType==WeiboUtil.ENTITY_TYPE_WEIBO) {
                    entityOwner = microbloggingService.getMicrobloggingById(entityId).getUserId();
                }else if(entityType==WeiboUtil.ENTITY_TYPE_COMMENT) {
                    entityOwner = commentService.getCommentById(entityId).getUserId();
                }
                producter.pushEvent(new EventModel().setEventType(EventType.LIKE).setActorId(actorId)
                        .setEntityOwner(entityOwner).setEntityType(entityType).setEntityId(entityId));
                return  WeiboUtil.getJSONString(0,"点赞成功");
            }
            return WeiboUtil.getJSONString(1,map);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return WeiboUtil.getJSONString(1,"点赞失败");
        }

    }

}
