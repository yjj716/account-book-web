package com.yjj.accountbook.mapper;

import com.yjj.accountbook.entity.AccountRecord;
import com.yjj.accountbook.entity.dto.RecordStatistics;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface RecordMapper {
    @Select("select*from `record` where id=#{id} and user_id=#{userId}")
    AccountRecord findById(@Param("id") Integer id,@Param("userId") Integer userId);
    @Select("select * from `record` where user_id=#{userId} order by id")
    List<AccountRecord> findAll(@Param("userId")Integer userId);
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into `record`(user_id,amount,type,category,note,record_date) values(#{userId},#{amount},#{type},#{category}" +
            ",#{note},#{recordDate})")
    int insert(AccountRecord record);
    @Delete("delete from `record` where id=#{id} and user_id=#{userId}")
    int deleteById(@Param("id")Integer id,@Param("userId")Integer userId);
    @Select("""
select ifnull(sum(case when type=2 then amount else 0 end),0) as income,
ifnull(sum(case when type=1 then amount else 0 end),0) as expense
from `record` where record_date>=#{start} and record_date<#{end} and user_id = #{userId}
""")
    RecordStatistics  statistics(@Param("start")LocalDate start,@Param("end")LocalDate end,@Param("userId")Integer userId);


    List<AccountRecord> findPage(@Param("start") LocalDate start,
                                 @Param("end") LocalDate end,
                                 @Param("type") Integer type,
                                 @Param("category") String category,
                                 @Param("userId") Integer userId,
                                 @Param("offset") int offset,
                                 @Param("size") int size);


    long countPage(@Param("start") LocalDate start,
                   @Param("end") LocalDate end,
                   @Param("type") Integer type,
                   @Param("userId") Integer userId,
                   @Param("category") String category);
}
