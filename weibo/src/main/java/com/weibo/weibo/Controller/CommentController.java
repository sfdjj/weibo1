package com.weibo.weibo.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weibo.weibo.aspect.LoginRequired;
import com.weibo.weibo.async.EventModel;
import com.weibo.weibo.async.EventType;
import com.weibo.weibo.async.Producter;
import com.weibo.weibo.model.Comment;
import com.weibo.weibo.model.HostHolder;
import com.weibo.weibo.model.User;
import com.weibo.weibo.service.CommentService;
import com.weibo.weibo.service.MicrobloggingService;
import com.weibo.weibo.service.UserService;
import com.weibo.weibo.util.WeiboUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by jwc on 2017/7/25.
 */
@Controller
public class CommentController {


    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    Producter producter;

    @Autowired
    MicrobloggingService microbloggingService;

    @RequestMapping(path = {"/addComment"})
    @LoginRequired(loginRequired = true)
    @ResponseBody
    public String addWeiboComment(@RequestParam("content") String content, @RequestParam("entityType") int entityType,
                                  @RequestParam("entityId") int microbloggingId) {
        try{
            User user = hostHolder.getUser();
//            if(user==null) {
//                return "登陆后可评论";
//            }
            int code = commentService.addComment(content,microbloggingId, WeiboUtil.ENTITY_TYPE_WEIBO,user.getId());
            if(code!=1) {
                return WeiboUtil.getJSONString(code,"评论失败");
            }
            producter.pushEvent(new EventModel().setEventType(EventType.COMMENT).setEntityType(WeiboUtil.ENTITY_TYPE_WEIBO).
                    setEntityId(microbloggingId).setActorId(user.getId()).
                    setEntityOwner(microbloggingService.getMicrobloggingById(microbloggingId).getUserId()));
            return WeiboUtil.getJSONString(0,"添加评论成功");
        }catch (Exception e) {
            return WeiboUtil.getJSONString(1,"添加评论失败");
        }
    }

    @RequestMapping(path = {"/addCommentComment/"})
    @LoginRequired(loginRequired = true)
    @ResponseBody
    public String addCommentComment(@RequestParam("content") String content,
                             @RequestParam("commentId") int commentId) {
        try{
            User user = hostHolder.getUser();
//            if(user==null) {
//                return "登陆后可评论";
//            }
            int code = commentService.addComment(content,commentId, WeiboUtil.ENTITY_TYPE_WEIBO,user.getId());
            if(code!=1) {
                return WeiboUtil.getJSONString(code,"添加评论失败");
            }
            producter.pushEvent(new EventModel().setEventType(EventType.COMMENT).setEntityType(WeiboUtil.ENTITY_TYPE_COMMENT).
                    setEntityId(commentId).setActorId(user.getId()).
                    setEntityOwner(commentService.getCommentById(commentId).getUserId()));
            return WeiboUtil.getJSONString(0,"添加评论成功");
        }catch (Exception e) {
            return WeiboUtil.getJSONString(1,"添加评论失败");
        }
    }

    @RequestMapping(value={"/delete"})
    @LoginRequired(loginRequired = true)
    @ResponseBody
    public String delete(@RequestParam("id") int id) {
        User user = hostHolder.getUser();
//        if(user==null) {
//            return WeiboUtil.getJSONString(1,"你还没有登陆");
//        }
        Map<String,Object> map ;
        try{
            map = commentService.deleteComment(id,user.getId());
            if(map.containsKey("success")) {
                return WeiboUtil.getJSONString(0,"删除成功");
            }
            return WeiboUtil.getJSONString(1,map);
        }catch (Exception e) {
            return WeiboUtil.getJSONString(1,"删除失败");
        }
    }

    @RequestMapping(path={"/listComments"})
    @LoginRequired(loginRequired = true)
    @ResponseBody
    public String comment(@RequestParam("entityId") int entityId,
                          @RequestParam("entityType") int entityType,
                          @RequestParam(value = "offset",defaultValue = "0") int offset,
                          @RequestParam(value = "limit",defaultValue = "10") int limit,
                          Model model) {
//        User user = hostHolder.getUser();
//        if(user==null) {
//            return "redirect:/login";
//        }
        JSONArray jsonArray = getList(entityId,WeiboUtil.ENTITY_TYPE_WEIBO,offset,limit);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("comments",jsonArray);
        //model.addAttribute("comments",jsonArray);
        //model.addAttribute("user",user);
        jsonObject.put("code",0);
        return jsonObject.toJSONString();
    }

    public JSONArray getList(int entityId, int entityType, int offset, int limit) {
        JSONArray jsonArray = new JSONArray();
        List<Comment> comments = commentService.getCommentList(entityId,entityType,offset,limit);
        //List<Map<String,Object>> result = new ArrayList<>();
        for(Comment comment:comments) {
            //Map<String,Object> map = new HashMap<>();
            User commentUser = userService.getUserById(comment.getUserId());
            if(commentUser==null) {
                continue;
            }
//            map.put("commentUser",commentUser);
//            map.put("comment",comment);
//            result.add(map);
            JSONObject obj = new JSONObject();
            obj.put("cid", comment.getId());
            obj.put("content", comment.getContent());
            obj.put("likeCount", comment.getLikeCount());
            obj.put("username", commentUser.getName());
            obj.put("userid", commentUser.getId());
            obj.put("userhead", commentUser.getHeadUrl());
            //obj.put("liked", likeService.isLiked(hostHolder.get().getId(), EntityType.COMMENT.getValue(), comment.getId()));
            jsonArray.add(obj);

        }
        return jsonArray;
    }

}
