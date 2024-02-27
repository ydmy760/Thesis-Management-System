package org.user.login.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import org.user.login.entity.User;
@Repository
@Mapper
public interface LoginDao extends BaseMapper<User> {
}
