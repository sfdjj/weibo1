package com.weibo.weibo;

import com.weibo.weibo.dao.CommentDAO;
import com.weibo.weibo.dao.MicrobloggingDAO;
import com.weibo.weibo.dao.UserDAO;
import com.weibo.weibo.model.Comment;
import com.weibo.weibo.model.Microblogging;
import com.weibo.weibo.model.User;
import com.weibo.weibo.util.WeiboUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Created by jwc on 2017/7/21.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class initTests {

    @Autowired
    UserDAO userDAO;

    @Autowired
    MicrobloggingDAO microbloggingDAO;

    @Autowired
    CommentDAO commentDAO;


    @Test
    public void contextLoads() {
        for(int i=1;i<11;i++) {
            User user = new User();
            user.setId(i);
            user.setName(String.format("用户%d",i));
            user.setPassword("password");
            user.setSalt("salt");
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
            userDAO.addUser(user);
            Microblogging microblogging = new Microblogging();
            microblogging.setCreatedDate(new Date());
            microblogging.setStatus(0);
            microblogging.setLikeCount(i);
            microblogging.setCommentCount(i);
            microblogging.setImage(String.format("http://images.nowcoder.com/head/%dm.png_http://images.nowcoder.com/head/%dm.png", new Random().nextInt(1000),new Random().nextInt(1000)));
            microblogging.setContent(String.format("假装这里有内容%d",i));
            microblogging.setUserId(i);
            microbloggingDAO.addMicroblogging(microblogging);
            for(int j=1;j<6;j++) {
                Comment comment = new Comment();
                comment.setUserId(3);
                comment.setCreatedDate(new Date());
                comment.setEntityType(WeiboUtil.ENTITY_TYPE_WEIBO);
                comment.setStatus(0);
                comment.setContent(String.format("这是第%d条评论",j));
                comment.setEntityId(i);
                commentDAO.addComment(comment);

            }
        }
        List<Microblogging> list = microbloggingDAO.selectByUserIdAndOffset(0,10,0);
        List<Comment> list1 = commentDAO.selectByEntity(4,0,0,10);
        for(Comment comment:list1) {
            System.out.println(comment.getContent());
        }
    }

}
