package com.weibo.weibo.dao;


import com.weibo.weibo.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by nowcoder on 2016/7/9.
 */
@Mapper
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    /*
    @Update({"update ", TABLE_NAME, " set status=#{status} where entity_id=#{entityId} and entity_type=#{entityType}"})
    void updateStatus(@Param("entityId") int entityId, @Param("entityType") int entityType, @Param("status") int status);
    */
    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id} "})
    void updateStatus(@Param("id") int id ,@Param("status") int status);


    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
            " where entity_id=#{entityId} and entity_type=#{entityType} order by id desc limit #{offset},#{limit}"})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType,
                            @Param("offset") int offset,@Param("limit") int limit);

    @Select({"select ",SELECT_FIELDS," from ", TABLE_NAME, " where id=#{id}"})
    Comment getComment(@Param("id") int id);

    @Select({"select count(id) as id from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType} "})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);
}
