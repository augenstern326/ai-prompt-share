package com.aiprompt.sharing.test;

import com.aiprompt.sharing.dto.ReportCreateDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 举报服务测试类
 */
@SpringBootTest
@Transactional
public class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Test
    public void testCreateReport() {
        ReportCreateDTO createDTO = new ReportCreateDTO();
        createDTO.setPromptId(1L); // 假设提示词ID为1
        createDTO.setReason("测试举报原因");
        
        Long userId = 1L; // 假设用户ID为1
        
        Long reportId = reportService.createReport(createDTO, userId);
        
        assertNotNull(reportId);
        assertTrue(reportId > 0);
    }

    @Test
    public void testGetReportList() {
        // 先创建几个举报
        ReportCreateDTO createDTO1 = new ReportCreateDTO();
        createDTO1.setPromptId(1L);
        createDTO1.setReason("举报原因1");
        
        ReportCreateDTO createDTO2 = new ReportCreateDTO();
        createDTO2.setPromptId(2L);
        createDTO2.setReason("举报原因2");
        
        Long userId1 = 1L;
        Long userId2 = 2L;
        
        reportService.createReport(createDTO1, userId1);
        reportService.createReport(createDTO2, userId2);
        
        // 测试获取举报列表
        IPage<ReportVO> reportPage = reportService.getReportList(1, 10, null, null);
        
        assertNotNull(reportPage);
        assertTrue(reportPage.getTotal() >= 2);
    }

    @Test
    public void testHandleReport() {
        // 先创建一个举报
        ReportCreateDTO createDTO = new ReportCreateDTO();
        createDTO.setPromptId(1L);
        createDTO.setReason("待处理举报");
        
        Long userId = 1L;
        Long reportId = reportService.createReport(createDTO, userId);
        
        // 测试处理举报
        Long handlerId = 2L; // 假设处理人ID为2
        Integer status = 1; // 已处理
        Integer action = 1; // 删除提示词
        String handleNote = "测试处理备注";
        
        reportService.handleReport(reportId, handlerId, status, action, handleNote);
        
        // 验证处理结果
        IPage<ReportVO> reportPage = reportService.getReportList(1, 10, null, 1);
        boolean found = false;
        for (ReportVO report : reportPage.getRecords()) {
            if (report.getId().equals(reportId)) {
                assertEquals(1, report.getStatus());
                assertEquals(handlerId, report.getHandlerId());
                assertEquals(action, report.getAction());
                assertEquals(handleNote, report.getHandleNote());
                found = true;
                break;
            }
        }
        assertTrue(found, "未找到处理后的举报");
    }
}
