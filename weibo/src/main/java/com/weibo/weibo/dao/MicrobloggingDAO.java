package com.weibo.weibo.dao;

import com.weibo.weibo.model.Microblogging;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by jwc on 2017/7/21.
 */
@Mapper
public interface MicrobloggingDAO {
    String TABLE_NAME = "microblogging";
    String INSET_FIELDS = " content, image, comment_count, like_count, created_date, user_id, status ";
    String SELECT_FIELDS = "id, "+INSET_FIELDS;

    @Insert({"insert into ",TABLE_NAME,"( ",INSET_FIELDS,")"," values (#{content},#{image}," +
            "#{commentCount},#{likeCount},#{createdDate},#{userId},#{status})"})
    int addMicroblogging(Microblogging microblogging);

    @Update({"update ",TABLE_NAME," set like_count = #{likeCount} where id = #{id}"})
    void updateLikeCount(@Param("likeCount") int likeCount,@Param("id") int id);

    @Update({"update ",TABLE_NAME," set comment_count = #{commentCount} where id = #{id}"})
    void updateCommentCount(@Param("commentCount") int commentCount,@Param("id") int id);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,"where id = #{id}"})
    Microblogging selectMicroblogginById(int id);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME,"where user_id in (","}"})
    List<Microblogging> selectMicroblogginByFollow(@Param("userId") String userId,
                                                   @Param("offset") int offset,
                                                   @Param("limit") int limit);

    List<Microblogging> selectByUserIdAndOffset(@Param("offset") int offset,
                                           @Param("limit") int limit,
                                                @Param("userId") int userId);
}
