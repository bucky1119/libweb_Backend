package com.cau.web.Controller;

import com.cau.web.entity.AuditLog;
import com.cau.web.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@Secured("ROLE_ADMIN")  // 只有管理员可以访问
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    // 获取所有日志记录
    @GetMapping("/logs")
    public List<AuditLog> getAllLogs() {
        return auditLogService.getAllLogs();
    }
}
