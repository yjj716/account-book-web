package com.yjj.accountbook.mapper;

import com.yjj.accountbook.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from `user` where username=#{username}")
    User findByUsername(String username);
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into `user`(username,password) values(#{username},#{password})")
    int insert(User user);
    @Select("select  * from `user` where id=#{id}")
    User findById(Integer id);
}
