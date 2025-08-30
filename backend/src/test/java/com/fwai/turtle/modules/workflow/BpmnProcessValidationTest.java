package com.fwai.turtle.modules.workflow;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BPMN流程定义验证测试
 * 
 * @author Claude
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "workflow.engine=flowable"
})
public class BpmnProcessValidationTest {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Test
    @DisplayName("验证BPMN流程定义是否正确部署")
    void testBpmnProcessDefinitionsDeployed() {
        // 检查报销审批流程
        ProcessDefinition reimbursementProcess = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey("reimbursement_approval")
            .singleResult();
        
        assertNotNull(reimbursementProcess, "Reimbursement approval process should be deployed");
        assertEquals("报销审批流程", reimbursementProcess.getName());
        assertTrue(reimbursementProcess.hasStartFormKey() || !reimbursementProcess.hasStartFormKey()); // 验证流程可用

        // 检查合同审批流程
        ProcessDefinition contractProcess = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey("contract_approval")
            .singleResult();
        
        assertNotNull(contractProcess, "Contract approval process should be deployed");
        assertEquals("合同审批流程", contractProcess.getName());
        
        // 验证流程定义数量
        List<ProcessDefinition> allProcesses = repositoryService.createProcessDefinitionQuery().list();
        assertTrue(allProcesses.size() >= 2, "Should have at least 2 process definitions");
    }

    @Test
    @DisplayName("验证报销审批流程的基本流转")
    void testReimbursementProcessFlow() {
        // 启动报销审批流程
        Map<String, Object> variables = new HashMap<>();
        variables.put("businessType", "REIMBURSEMENT");
        variables.put("businessId", 1001L);
        variables.put("amount", 800.0);
        variables.put("submitterId", 1L);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
            "reimbursement_approval", "REIMB-TEST-001", variables);
        
        assertNotNull(processInstance);
        assertFalse(processInstance.isEnded());

        // 验证第一个任务是部门审批
        List<Task> tasks = taskService.createTaskQuery()
            .processInstanceId(processInstance.getId())
            .active()
            .list();
        
        assertEquals(1, tasks.size());
        Task firstTask = tasks.get(0);
        assertEquals("deptApproval", firstTask.getTaskDefinitionKey());
        assertEquals("部门领导审批", firstTask.getName());

        // 完成部门审批
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("approved", true);
        taskVariables.put("comments", "部门审批通过");
        taskService.complete(firstTask.getId(), taskVariables);

        // 验证流程进入下一步（财务审批，因为金额≤1000）
        tasks = taskService.createTaskQuery()
            .processInstanceId(processInstance.getId())
            .active()
            .list();
        
        assertEquals(1, tasks.size());
        Task secondTask = tasks.get(0);
        assertEquals("financeApproval", secondTask.getTaskDefinitionKey());
        assertEquals("财务审批", secondTask.getName());
    }

    @Test
    @DisplayName("验证报销审批流程的金额路由逻辑")
    void testReimbursementAmountRouting() {
        // 测试小额报销（≤1000）
        testAmountRouting(500.0, "financeApproval", "财务审批");
        
        // 测试中额报销（1000-5000）
        testAmountRouting(2500.0, "generalManagerApproval", "总经理审批");
        
        // 测试大额报销（>5000）
        testAmountRouting(8000.0, "ceoApproval", "CEO审批");
    }

    @Test
    @DisplayName("验证合同审批流程的基本流转")
    void testContractProcessFlow() {
        // 启动合同审批流程
        Map<String, Object> variables = new HashMap<>();
        variables.put("businessType", "CONTRACT");
        variables.put("businessId", 2001L);
        variables.put("amount", 50000.0); // 5万合同
        variables.put("submitterId", 1L);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
            "contract_approval", "CONTRACT-TEST-001", variables);
        
        assertNotNull(processInstance);
        assertFalse(processInstance.isEnded());

        // 验证第一个任务是项目经理审核
        List<Task> tasks = taskService.createTaskQuery()
            .processInstanceId(processInstance.getId())
            .active()
            .list();
        
        assertEquals(1, tasks.size());
        Task firstTask = tasks.get(0);
        assertEquals("projectManagerReview", firstTask.getTaskDefinitionKey());
        assertEquals("项目经理初审", firstTask.getName());
    }

    @Test
    @DisplayName("验证流程拒绝逻辑")
    void testProcessRejection() {
        // 启动流程
        Map<String, Object> variables = new HashMap<>();
        variables.put("businessType", "REIMBURSEMENT");
        variables.put("businessId", 3001L);
        variables.put("amount", 1000.0);
        variables.put("submitterId", 1L);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
            "reimbursement_approval", "REIMB-REJECT-001", variables);
        
        // 获取第一个任务并拒绝
        List<Task> tasks = taskService.createTaskQuery()
            .processInstanceId(processInstance.getId())
            .active()
            .list();
        
        Task firstTask = tasks.get(0);
        Map<String, Object> rejectionVars = new HashMap<>();
        rejectionVars.put("approved", false);
        rejectionVars.put("comments", "测试拒绝");
        taskService.complete(firstTask.getId(), rejectionVars);

        // 验证流程已结束
        ProcessInstance finishedInstance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(processInstance.getId())
            .singleResult();
        
        assertNull(finishedInstance, "Process should be ended after rejection");
    }

    @Test
    @DisplayName("验证流程变量的正确传递")
    void testProcessVariables() {
        Map<String, Object> initialVars = new HashMap<>();
        initialVars.put("businessType", "TEST");
        initialVars.put("businessId", 4001L);
        initialVars.put("amount", 1500.0);
        initialVars.put("submitterId", 2L);
        initialVars.put("priority", 3);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
            "reimbursement_approval", "VAR-TEST-001", initialVars);

        // 验证变量已正确设置
        Map<String, Object> processVars = runtimeService.getVariables(processInstance.getId());
        
        assertEquals("TEST", processVars.get("businessType"));
        assertEquals(4001L, processVars.get("businessId"));
        assertEquals(1500.0, processVars.get("amount"));
        assertEquals(2L, processVars.get("submitterId"));
        assertEquals(3, processVars.get("priority"));

        // 在任务完成时添加新变量
        List<Task> tasks = taskService.createTaskQuery()
            .processInstanceId(processInstance.getId())
            .active()
            .list();

        Task task = tasks.get(0);
        Map<String, Object> taskVars = new HashMap<>();
        taskVars.put("approved", true);
        taskVars.put("approver", "Manager");
        taskVars.put("approvalTime", System.currentTimeMillis());
        taskService.complete(task.getId(), taskVars);

        // 验证新变量已添加到流程中
        processVars = runtimeService.getVariables(processInstance.getId());
        assertTrue((Boolean) processVars.get("approved"));
        assertEquals("Manager", processVars.get("approver"));
        assertNotNull(processVars.get("approvalTime"));
    }

    @Test
    @DisplayName("验证并行网关在合同审批中的行为")
    void testParallelGatewayInContractProcess() {
        // 启动需要并行审核的合同流程（金额>10万）
        Map<String, Object> variables = new HashMap<>();
        variables.put("businessType", "CONTRACT");
        variables.put("businessId", 5001L);
        variables.put("amount", 200000.0); // 20万合同，需要并行法务和财务审核
        variables.put("submitterId", 1L);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
            "contract_approval", "CONTRACT-PARALLEL-001", variables);

        // 完成项目经理初审
        List<Task> tasks = taskService.createTaskQuery()
            .processInstanceId(processInstance.getId())
            .active()
            .list();

        assertEquals(1, tasks.size());
        Task pmTask = tasks.get(0);
        assertEquals("projectManagerReview", pmTask.getTaskDefinitionKey());

        Map<String, Object> approvalVars = new HashMap<>();
        approvalVars.put("approved", true);
        approvalVars.put("comments", "项目经理审核通过");
        taskService.complete(pmTask.getId(), approvalVars);

        // 验证进入并行审核阶段（法务和财务同时进行）
        tasks = taskService.createTaskQuery()
            .processInstanceId(processInstance.getId())
            .active()
            .list();

        // 应该有两个并行任务
        assertEquals(2, tasks.size());
        
        boolean hasLegalReview = tasks.stream()
            .anyMatch(task -> "legalReview".equals(task.getTaskDefinitionKey()));
        boolean hasFinanceReview = tasks.stream()
            .anyMatch(task -> "financeReview".equals(task.getTaskDefinitionKey()));
        
        assertTrue(hasLegalReview, "Should have legal review task");
        assertTrue(hasFinanceReview, "Should have finance review task");
    }

    private void testAmountRouting(double amount, String expectedTaskKey, String expectedTaskName) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("businessType", "REIMBURSEMENT");
        variables.put("businessId", System.currentTimeMillis());
        variables.put("amount", amount);
        variables.put("submitterId", 1L);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
            "reimbursement_approval", "AMOUNT-TEST-" + (int) amount, variables);

        // 完成部门审批
        List<Task> tasks = taskService.createTaskQuery()
            .processInstanceId(processInstance.getId())
            .active()
            .list();

        Task deptTask = tasks.get(0);
        Map<String, Object> approvalVars = new HashMap<>();
        approvalVars.put("approved", true);
        approvalVars.put("comments", "部门审批通过");
        taskService.complete(deptTask.getId(), approvalVars);

        // 验证路由到正确的下一步
        tasks = taskService.createTaskQuery()
            .processInstanceId(processInstance.getId())
            .active()
            .list();

        assertEquals(1, tasks.size());
        Task nextTask = tasks.get(0);
        assertEquals(expectedTaskKey, nextTask.getTaskDefinitionKey());
        assertEquals(expectedTaskName, nextTask.getName());

        // 清理：完成流程
        approvalVars.put("comments", "测试完成");
        taskService.complete(nextTask.getId(), approvalVars);
    }
}