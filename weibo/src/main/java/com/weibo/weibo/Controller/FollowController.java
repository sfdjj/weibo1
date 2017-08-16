package com.weibo.weibo.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weibo.weibo.aspect.LoginRequired;
import com.weibo.weibo.async.EventModel;
import com.weibo.weibo.async.EventType;
import com.weibo.weibo.async.Producter;
import com.weibo.weibo.model.HostHolder;
import com.weibo.weibo.model.User;
import com.weibo.weibo.service.FollowService;
import com.weibo.weibo.service.UserService;
import com.weibo.weibo.util.JedisUtils;
import com.weibo.weibo.util.WeiboUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by jwc on 2017/7/31.
 */
@Controller
public class FollowController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    @Autowired
    Producter producter;

    @Autowired
    UserService userService;

    @RequestMapping(path = {"/follow"})
    @LoginRequired
    @ResponseBody
    public String follow(@RequestParam("userId") int userId) {
        User user = hostHolder.getUser();
//        if(user==null) {
//            return WeiboUtil.getJSONString(1,"你还没有登陆");
//        }
        try {
            Map<String, Object> map = followService.follow(user.getId(), userId);
            if (map.containsKey("success")) {
                producter.pushEvent(new EventModel().setEventType(EventType.FOLLOW).
                        setActorId(user.getId()).setEntityOwner(userId));
                map.put("msg","关注成功");
                return WeiboUtil.getJSONString(0, map);
            }
            return WeiboUtil.getJSONString(1,map);
        }catch (Exception e) {
            return WeiboUtil.getJSONString(1,"关注失败");
        }
    }

    @RequestMapping(path = {"/unfollow"})
    @ResponseBody
    public String remFollow(@RequestParam("userId")int followUserId) {
        User user = hostHolder.getUser();
        try {
            Map<String,Object> map = followService.remFollow(user.getId(),followUserId);
            if(map.containsKey("success")) {
                return WeiboUtil.getJSONString(0,"取消关注成功");
            }
            return WeiboUtil.getJSONString(1,map);
        }catch (Exception e) {
            return WeiboUtil.getJSONString(1,"取消关注异常");
        }
    }


    @RequestMapping(path = {"/followee/{uid}"})
    public String followee(@PathVariable("uid") int uid, Model model,
                           @RequestParam(value = "offset", defaultValue = "0") int offset,
                           @RequestParam(value = "count", defaultValue = "10") int count) {
        try {
            User u = userService.getUserById(uid);
            if (u == null) {
                return "redirect:/";
            }
            model.addAttribute("hostname", u.getName());
            model.addAttribute("followeeCount", followService.getFolloweeCount(JedisUtils.getFollowKey(uid)));
            List<Integer> followeeIds = followService.getFollowList(JedisUtils.getFollowKey(uid), offset, count);
            model.addAttribute("vos", getFollowUsers(followeeIds));

        } catch (Exception e) {
           System.out.println(e.getMessage());
        }
        return "followee";
    }

    @RequestMapping(path = {"/follower/{uid}"})
    public String follower(@PathVariable("uid") int uid, Model model,
                           @RequestParam(value = "offset", defaultValue = "0") int offset,
                           @RequestParam(value = "count", defaultValue = "10") int count) {
        try {
            User u = userService.getUserById(uid);
            if (u == null) {
                return "redirect:/";
            }
            model.addAttribute("hostname", u.getName());
            model.addAttribute("followerCount", followService.getFolloweeCount(JedisUtils.getFollowerKey(uid)));
            List<Integer> followerIds = followService.getFollowList(JedisUtils.getFollowerKey(uid),offset, count);
            model.addAttribute("vos", getFollowUsers(followerIds));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "follower";
    }

    private JSONArray getFollowUsers(List<Integer> followIds) {
        JSONArray vos = new JSONArray();
        for (int id : followIds) {
            JSONObject obj = new JSONObject();
            User followeeUser = userService.getUserById(id);
            if (followeeUser == null) {
                continue;
            }
            obj.put("userhead", followeeUser.getHeadUrl());
            obj.put("userid", followeeUser.getId());
            obj.put("username",followeeUser.getName());
            if (hostHolder.getUser() != null) {
                obj.put("followed", followService.isFollower(hostHolder.getUser().getId(),
                        JedisUtils.getFollowerKey(followeeUser.getId()), followeeUser.getId()));
            } else {
                obj.put("followed", false);
            }
            vos.add(obj);
        }
        return vos;
    }


}
