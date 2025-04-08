package com.cau.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cau.web.entity.AuditLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuditLogMapper extends BaseMapper<AuditLog> {

    @Insert("INSERT INTO audit_log (username, operation, details) VALUES (#{username}, #{operation}, #{details})")
    void insertAuditLog(String username, String operation, String details);
}
