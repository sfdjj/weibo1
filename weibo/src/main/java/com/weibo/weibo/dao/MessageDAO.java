package com.weibo.weibo.dao;


import com.weibo.weibo.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by nowcoder on 2016/7/9.
 */
@Mapper
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, has_read, conversation_id, created_date,from_delete ,to_delete  ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate},#{fromDelete},#{toDelete})"})
    int addMessage(Message message);

    @Update({"update ",TABLE_NAME,"set from_delete=#{fromDelete} where id=#{id}"})
    int DeleteFromMessage(@Param("fromDelete") int fromDelete,
                          @Param("id") int id);

    @Update({"update ",TABLE_NAME,"set has_read=#{hasRead} where id=#{id}"})
    int updateHasRead(@Param("hasRead") int hasRead,
                          @Param("id") int id);

    @Select({"select ",SELECT_FIELDS," from ", TABLE_NAME,"where id=#{id}"})
    Message selectMessageById(@Param("id") int id);

    @Update({"update ",TABLE_NAME,"set to_delete=#{toDelete} where id=#{id}"})
    int DeleteToMessage(@Param("toDelete") int toDelete,
                          @Param("id") int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " " +
            "where conversation_id=#{conversationId} and ((from_id=#{userId} and from_delete=0) or (to_id=#{userId} and to_delete=0)) order by id desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset, @Param("limit") int limit,@Param("userId") int userId);

    @Select({"select count(id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId}" +
            " and conversation_id=#{conversationId} and to_delete=0"})
    int getConvesationUnreadCount(@Param("userId") int userId,
                                  @Param("conversationId") String conversationId);

    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, " " +
            "where (from_id=#{userId} and from_delete=0) or (to_id=#{userId} and to_delete=0) order by created_date desc) " +
            "tt group by conversation_id order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset, @Param("limit") int limit);
}
