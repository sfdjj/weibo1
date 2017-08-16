package com.weibo.weibo.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.weibo.weibo.aspect.LoginRequired;
import com.weibo.weibo.model.HostHolder;
import com.weibo.weibo.model.Message;
import com.weibo.weibo.model.User;
import com.weibo.weibo.service.MessageService;
import com.weibo.weibo.service.UserService;
import com.weibo.weibo.util.WeiboUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by jwc on 2017/7/26.
 */
@Controller
public class MessageController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;


    @RequestMapping(path={"/addMessage"})
    @LoginRequired(loginRequired = true)
    @ResponseBody
    public String addMessage(@RequestParam("content") String content,
                             @RequestParam(value = "fromId", defaultValue = "0") int fromId,
                             @RequestParam(value = "toId", defaultValue = "0") int toId,
                             @RequestParam(value = "toName") String toName) {
        try {
            User user = hostHolder.getUser();
//            if (user == null) {
//                httpServletResponse.sendRedirect("/login");
//            }
            User toUser = userService.getUserByName(toName);
            Map<String,Object> map = messageService.addMessage(user.getId(),toUser.getId(),content);
            if(map.containsKey("success")) {
                return WeiboUtil.getJSONString(0,"添加成功");
            }
            return WeiboUtil.getJSONString(1,map);
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return WeiboUtil.getJSONString(1,"添加失败").toString();
        }
    }

    @RequestMapping(path={"/conversation"})
    @LoginRequired(loginRequired = true)
    public String readMessage(@RequestParam("id") String conversationId,
                              @RequestParam(value = "offset", defaultValue = "0") int offset,
                              @RequestParam(value = "count", defaultValue = "10") int count,
                              HttpServletResponse response,
                              Model model) {
        try{
            User user = hostHolder.getUser();
//            if(user==null) {
//                return "redirect:/login";
//            }
            List<Message> messages = messageService.getMessage(conversationId,offset,count,user.getId());
            JSONArray messageArray = new JSONArray();
//            int a = Integer.parseInt(conversationId.split("_")[0]);
//            int b = Integer.parseInt(conversationId.split("_")[1]);
//            User anotherUser = userService.getUserById(user.getId()==a?b:a);
            for(Message message:messages) {
                JSONObject obj = new JSONObject();
                if(user.getId()==message.getToId()) {
                    messageService.updateHasRead(message.getId());
                }
                User fromUser = userService.getUserById(message.getFromId());
                obj.put("mid", message.getId());
                obj.put("content", message.getContent());
                obj.put("createdDate", message.getCreatedDate());
                obj.put("username", fromUser.getName());
                obj.put("userid", fromUser.getId());
                obj.put("userhead", fromUser.getHeadUrl());
                messageArray.add(obj);
            }
            model.addAttribute("messages",messageArray);
//            model.addAttribute("anotherUser",anotherUser);
//            model.addAttribute("messages",messages);
//            model.addAttribute("user",user);
        }catch (Exception e) {
            System.out.print(e.getMessage());
//            return WeiboUtil.getJSONString(1,"读取错误");
        }
        return "letterDetail";
    }

    @RequestMapping(path={"/messagebox"})
    @LoginRequired(loginRequired = true)
    public String index(Model model, @RequestParam(value = "offset", defaultValue = "0") int offset,
                               @RequestParam(value = "count", defaultValue = "10") int count,
                               HttpServletResponse response) {
        try{
            User user = hostHolder.getUser();
//            if(user==null) {
//                httpServletResponse.sendRedirect("/login");
//            }
          //  List<Map<String,Object>> list = getList(user.getId());
            // model.addAttribute("message",list);
            //model.addAttribute("user",user);
            model.addAttribute("messages", getList(user.getId(),offset,count));

        }catch (Exception e) {
            System.out.print(e.getMessage());
//            return WeiboUtil.getJSONString(0,"异常");
        }
        return "letter";
    }

    @RequestMapping(path={"/deleteMessage"})
    @LoginRequired(loginRequired = true)
    @ResponseBody
    public String deleteMessage(@RequestParam("messageId") int messageId) {
        try{
            User user = hostHolder.getUser();
//            if(user==null) {
//                return "redirect:/login";
//            }
            Map<String,Object> map = messageService.deleteMessage(messageId,user.getId());
            if(map.containsKey("success")) {
                return WeiboUtil.getJSONString(0,"删除成功");
            }
            return WeiboUtil.getJSONString(1,"删除失败");
        }catch (Exception e) {
            System.out.print(e.getMessage());
            return WeiboUtil.getJSONString(1,"删除异常");
        }
    }

    public JSONArray getList(int userId,int offset,int count) {
        List<Message> list = messageService.getConversationList(userId,offset,count);
//        List<Map<String,Object>> result = new ArrayList<>();
//        User user = hostHolder.getUser();
        JSONArray messageArray = new JSONArray();
        for(Message message:list) {
            //Map<String,Object> map = new HashMap<>();
            JSONObject obj = new JSONObject();
            User toUser = userService.getUserById(message.getFromId()==userId?message.getToId():message.getFromId());
//            int unReadCount = messageService.getConvesationUnreadCount(userId,message.getConversationId());
            obj.put("cid", message.getConversationId());
            obj.put("mid", message.getId());
            obj.put("content", message.getContent());
            obj.put("createdDate", message.getCreatedDate());
            obj.put("username", toUser.getName());
            obj.put("userid", toUser.getId());
            obj.put("userhead", toUser.getHeadUrl());
            messageArray.add(obj);
//            map.put("messageDetail",message);
//            map.put("toUser",toUser);
//            map.put("unReadCount",unReadCount);
//            result.add(map);
            //System.out.println(message.getContent()+" "+message.getId());
        }
//
        return messageArray;
    }



}
