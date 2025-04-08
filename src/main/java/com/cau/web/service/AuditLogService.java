package com.cau.web.service;

import com.cau.web.entity.AuditLog;
import com.cau.web.mapper.AuditLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogMapper auditLogMapper;

    // 保存日志记录
    public void saveLog(String username, String operation, String details) {
        AuditLog log = new AuditLog();
        log.setUsername(username);
        log.setOperation(operation);
        log.setDetails(details);
        log.setOperationTime(LocalDateTime.now());

        auditLogMapper.insert(log);  // 插入日志记录
    }

    // 获取所有日志记录
    public List<AuditLog> getAllLogs() {
        return auditLogMapper.selectList(null); // 获取所有日志记录
    }
}
