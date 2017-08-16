package com.weibo.weibo.service;

import com.weibo.weibo.dao.CommentDAO;
import com.weibo.weibo.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jwc on 2017/7/25.
 */
@Service
public class CommentService {

    @Autowired
    CommentDAO commentDAO;

    public Comment getCommentById(int id) {
        return commentDAO.getComment(id);
    }

    public int addComment(String content,int entityId,int entityType,int userId) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setEntityId(entityId);
        comment.setEntityType(entityType);
        comment.setCreatedDate(new Date());
        comment.setStatus(0);
        comment.setUserId(userId);
        return commentDAO.addComment(comment);
    }

    public List<Comment> getCommentList(int entityId,int entityType,int offset,int limit) {
        //offset = offset+(page-1)*10;
        List<Comment> comments = commentDAO.selectByEntity(entityId,entityType,offset,limit);/*
        System.out.println(entityId);
        System.out.print(entityType+" "+offset+" "+limit);

        for(Comment comment:comments) {
            System.out.println(comment.getContent());
        }*/
        return comments;
    }

    public Map<String,Object> deleteComment(int id,int userId) {
        Comment comment = commentDAO.getComment(id);
        Map<String,Object> map = new HashMap<>();
        if(comment.getUserId()!=userId) {
            map.put("msg","你没有权限删除此评论");
            return map;
        }
        commentDAO.updateStatus(id,0);
        map.put("success","删除成功");
        return map;
    }

}
