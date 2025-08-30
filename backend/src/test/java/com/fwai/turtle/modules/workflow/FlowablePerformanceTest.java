package com.fwai.turtle.modules.workflow;

import com.fwai.turtle.modules.workflow.dto.WorkflowSubmissionRequest;
import com.fwai.turtle.modules.workflow.dto.WorkflowSubmissionResponse;
import com.fwai.turtle.modules.workflow.service.FlowableWorkflowService;
import com.fwai.turtle.modules.workflow.service.UnifiedWorkflowService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Flowable工作流性能测试
 * 
 * @author Claude
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "workflow.engine=flowable",
    "spring.datasource.url=jdbc:h2:mem:perfdb",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "flowable.database-schema-update=drop-create"
})
@Disabled("Performance tests - run manually")
public class FlowablePerformanceTest {

    @Autowired
    private UnifiedWorkflowService unifiedWorkflowService;

    @Autowired
    private FlowableWorkflowService flowableWorkflowService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Test
    @DisplayName("测试批量工作流创建性能")
    void testBatchWorkflowCreationPerformance() {
        int batchSize = 100;
        StopWatch stopWatch = new StopWatch();
        
        stopWatch.start("Create " + batchSize + " workflows");
        
        List<WorkflowSubmissionResponse> responses = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            WorkflowSubmissionRequest request = createTestRequest("PERF-BATCH-" + i, 500.0 + i);
            WorkflowSubmissionResponse response = unifiedWorkflowService.submitForApproval(request);
            responses.add(response);
        }
        
        stopWatch.stop();
        
        // 验证所有工作流都成功创建
        assertEquals(batchSize, responses.size());
        
        // 性能断言
        long totalTime = stopWatch.getLastTaskTimeMillis();
        double avgTimePerWorkflow = (double) totalTime / batchSize;
        
        System.out.println("=== Batch Workflow Creation Performance ===");
        System.out.println("Total workflows: " + batchSize);
        System.out.println("Total time: " + totalTime + "ms");
        System.out.println("Average time per workflow: " + avgTimePerWorkflow + "ms");
        
        // 期望每个工作流创建时间不超过100ms
        assertTrue(avgTimePerWorkflow < 100, 
            "Average workflow creation time should be less than 100ms, actual: " + avgTimePerWorkflow + "ms");
    }

    @Test
    @DisplayName("测试并发工作流处理性能")
    void testConcurrentWorkflowPerformance() throws InterruptedException, ExecutionException {
        int threadCount = 10;
        int workflowsPerThread = 20;
        int totalWorkflows = threadCount * workflowsPerThread;
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<List<WorkflowSubmissionResponse>>> futures = new ArrayList<>();
        
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("Concurrent workflow creation");
        
        // 并发创建工作流
        for (int t = 0; t < threadCount; t++) {
            final int threadIndex = t;
            Future<List<WorkflowSubmissionResponse>> future = executor.submit(() -> {
                List<WorkflowSubmissionResponse> threadResponses = new ArrayList<>();
                for (int i = 0; i < workflowsPerThread; i++) {
                    String businessNo = "PERF-THREAD-" + threadIndex + "-" + i;
                    WorkflowSubmissionRequest request = createTestRequest(businessNo, 600.0 + i);
                    WorkflowSubmissionResponse response = unifiedWorkflowService.submitForApproval(request);
                    threadResponses.add(response);
                }
                return threadResponses;
            });
            futures.add(future);
        }
        
        // 等待所有线程完成
        List<WorkflowSubmissionResponse> allResponses = new ArrayList<>();
        for (Future<List<WorkflowSubmissionResponse>> future : futures) {
            allResponses.addAll(future.get());
        }
        
        stopWatch.stop();
        executor.shutdown();
        
        // 验证结果
        assertEquals(totalWorkflows, allResponses.size());
        
        // 性能报告
        long totalTime = stopWatch.getLastTaskTimeMillis();
        double avgTimePerWorkflow = (double) totalTime / totalWorkflows;
        double throughput = (double) totalWorkflows / totalTime * 1000; // workflows per second
        
        System.out.println("=== Concurrent Workflow Performance ===");
        System.out.println("Threads: " + threadCount);
        System.out.println("Workflows per thread: " + workflowsPerThread);
        System.out.println("Total workflows: " + totalWorkflows);
        System.out.println("Total time: " + totalTime + "ms");
        System.out.println("Average time per workflow: " + avgTimePerWorkflow + "ms");
        System.out.println("Throughput: " + throughput + " workflows/second");
        
        // 性能期望
        assertTrue(throughput > 10, "Throughput should be more than 10 workflows/second");
        assertTrue(avgTimePerWorkflow < 200, "Average time per workflow should be less than 200ms");
    }

    @Test
    @DisplayName("测试任务完成性能")
    void testTaskCompletionPerformance() {
        int workflowCount = 50;
        
        // 1. 创建工作流
        List<WorkflowSubmissionResponse> workflows = new ArrayList<>();
        for (int i = 0; i < workflowCount; i++) {
            WorkflowSubmissionRequest request = createTestRequest("PERF-TASK-" + i, 500.0);
            WorkflowSubmissionResponse response = unifiedWorkflowService.submitForApproval(request);
            workflows.add(response);
        }
        
        // 2. 获取所有任务
        List<Task> tasks = taskService.createTaskQuery().active().list();
        assertEquals(workflowCount, tasks.size());
        
        // 3. 批量完成任务
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("Complete " + workflowCount + " tasks");
        
        Map<String, Object> completionVars = new HashMap<>();
        completionVars.put("approved", true);
        completionVars.put("comments", "Performance test approval");
        
        for (Task task : tasks) {
            flowableWorkflowService.completeTask(task.getId(), completionVars);
        }
        
        stopWatch.stop();
        
        // 4. 验证任务完成
        long remainingTasks = taskService.createTaskQuery().active().count();
        assertTrue(remainingTasks <= workflowCount); // 某些可能进入下一步
        
        // 性能报告
        long totalTime = stopWatch.getLastTaskTimeMillis();
        double avgTimePerTask = (double) totalTime / workflowCount;
        
        System.out.println("=== Task Completion Performance ===");
        System.out.println("Total tasks completed: " + workflowCount);
        System.out.println("Total time: " + totalTime + "ms");
        System.out.println("Average time per task: " + avgTimePerTask + "ms");
        
        assertTrue(avgTimePerTask < 50, "Average task completion time should be less than 50ms");
    }

    @Test
    @DisplayName("测试查询性能")
    void testQueryPerformance() {
        // 准备测试数据
        int dataSize = 200;
        List<String> processInstanceIds = new ArrayList<>();
        
        // 创建测试工作流
        for (int i = 0; i < dataSize; i++) {
            WorkflowSubmissionRequest request = createTestRequest("PERF-QUERY-" + i, 700.0 + i);
            WorkflowSubmissionResponse response = unifiedWorkflowService.submitForApproval(request);
            processInstanceIds.add(response.getWorkflowId().toString());
        }
        
        // 测试各种查询性能
        StopWatch stopWatch = new StopWatch();
        
        // 1. 测试流程实例查询
        stopWatch.start("Query process instances");
        List<org.flowable.engine.runtime.ProcessInstance> processInstances = 
            runtimeService.createProcessInstanceQuery().list();
        stopWatch.stop();
        
        assertTrue(processInstances.size() >= dataSize);
        
        // 2. 测试任务查询
        stopWatch.start("Query active tasks");
        List<Task> tasks = taskService.createTaskQuery().active().list();
        stopWatch.stop();
        
        assertTrue(tasks.size() >= dataSize);
        
        // 3. 测试历史查询
        stopWatch.start("Query historic process instances");
        List<org.flowable.engine.history.HistoricProcessInstance> historicInstances = 
            flowableWorkflowService.getHistoricProcessInstances(null, null).getData();
        stopWatch.stop();
        
        // 4. 测试过滤查询
        stopWatch.start("Query filtered tasks");
        List<Task> filteredTasks = taskService.createTaskQuery()
            .taskDefinitionKey("deptApproval")
            .active()
            .list();
        stopWatch.stop();
        
        // 性能报告
        System.out.println("=== Query Performance (Data size: " + dataSize + ") ===");
        stopWatch.prettyPrint();
        
        // 所有查询应在合理时间内完成
        for (StopWatch.TaskInfo taskInfo : stopWatch.getTaskInfo()) {
            assertTrue(taskInfo.getTimeMillis() < 1000, 
                "Query '" + taskInfo.getTaskName() + "' should complete in less than 1000ms");
        }
    }

    @Test
    @DisplayName("测试内存使用情况")
    void testMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        
        // 记录初始内存使用
        runtime.gc(); // 强制垃圾回收
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();
        
        // 创建大量工作流
        int workflowCount = 500;
        List<WorkflowSubmissionResponse> responses = new ArrayList<>();
        
        for (int i = 0; i < workflowCount; i++) {
            WorkflowSubmissionRequest request = createTestRequest("MEM-TEST-" + i, 800.0);
            WorkflowSubmissionResponse response = unifiedWorkflowService.submitForApproval(request);
            responses.add(response);
            
            // 每100个工作流检查一次内存
            if (i % 100 == 0) {
                runtime.gc();
                long currentMemory = runtime.totalMemory() - runtime.freeMemory();
                long memoryIncrease = currentMemory - initialMemory;
                System.out.println("After " + (i + 1) + " workflows: " + 
                    memoryIncrease / 1024 / 1024 + " MB memory increase");
            }
        }
        
        // 最终内存检查
        runtime.gc();
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();
        long totalMemoryIncrease = finalMemory - initialMemory;
        double memoryPerWorkflow = (double) totalMemoryIncrease / workflowCount;
        
        System.out.println("=== Memory Usage ===");
        System.out.println("Total workflows: " + workflowCount);
        System.out.println("Initial memory: " + initialMemory / 1024 / 1024 + " MB");
        System.out.println("Final memory: " + finalMemory / 1024 / 1024 + " MB");
        System.out.println("Total memory increase: " + totalMemoryIncrease / 1024 / 1024 + " MB");
        System.out.println("Memory per workflow: " + memoryPerWorkflow / 1024 + " KB");
        
        // 内存使用不应过多
        assertTrue(memoryPerWorkflow < 50 * 1024, // 每个工作流不应超过50KB
            "Memory usage per workflow should be less than 50KB");
    }

    private WorkflowSubmissionRequest createTestRequest(String businessNo, double amount) {
        WorkflowSubmissionRequest request = new WorkflowSubmissionRequest();
        request.setBusinessType("REIMBURSEMENT");
        request.setBusinessId(System.currentTimeMillis() + businessNo.hashCode());
        request.setBusinessNo(businessNo);
        request.setTitle("Performance Test - " + businessNo);
        request.setDescription("Performance test workflow");
        request.setAmount(BigDecimal.valueOf(amount));
        request.setSubmitterId(1L);
        return request;
    }
}