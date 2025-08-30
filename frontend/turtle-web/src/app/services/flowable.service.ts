import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export interface FlowableTask {
  id: string;
  name: string;
  assignee: string;
  candidateGroups: string[];
  createTime: string;
  processInstanceId: string;
  processDefinitionKey: string;
  taskDefinitionKey: string;
  description?: string;
  priority?: number;
  dueDate?: string;
}

export interface FlowableProcessInstance {
  id: string;
  processDefinitionKey: string;
  businessKey: string;
  startTime: string;
  startUserId: string;
  suspended: boolean;
  ended: boolean;
}

export interface FlowableProcessDefinition {
  id: string;
  key: string;
  name: string;
  version: number;
  deploymentId: string;
  suspended: boolean;
}

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  code?: string;
}

@Injectable({
  providedIn: 'root'
})
export class FlowableService {
  private readonly baseUrl = `${environment.apiUrl}/flowable`;

  constructor(private http: HttpClient) {}

  /**
   * 获取用户任务列表 - 基于用户ID（数字）
   * @param userId 用户ID（数字，也支持用户名作为后备）
   */
  getTasks(userId: string): Observable<FlowableTask[]> {
    const params = new HttpParams().set('userId', userId);

    return this.http.get<ApiResponse<FlowableTask[]>>(`${this.baseUrl}/tasks`, { params })
      .pipe(map(response => response.data));
  }

  /**
   * 获取调试任务信息
   * @param userId 用户ID（数字，也支持用户名作为后备）
   */
  getDebugTasks(userId?: string): Observable<any> {
    let params = new HttpParams();
    if (userId) {
      params = params.set('userId', userId);
    }

    return this.http.get<ApiResponse<any>>(`${this.baseUrl}/debug-tasks`, { params })
      .pipe(map(response => response.data));
  }

  /**
   * 完成任务
   */
  completeTask(taskId: string, variables?: any): Observable<string> {
    return this.http.post<ApiResponse<string>>(`${this.baseUrl}/tasks/${taskId}/complete`, variables)
      .pipe(map(response => response.data));
  }

  /**
   * 认领任务
   */
  claimTask(taskId: string, userId: string): Observable<string> {
    return this.http.post<ApiResponse<string>>(`${this.baseUrl}/tasks/${taskId}/claim`, null, {
      params: { userId }
    }).pipe(map(response => response.data));
  }

  /**
   * 获取流程实例列表
   */
  getProcessInstances(businessKey?: string, processDefinitionKey?: string): Observable<FlowableProcessInstance[]> {
    let params = new HttpParams();
    if (businessKey) {
      params = params.set('businessKey', businessKey);
    }
    if (processDefinitionKey) {
      params = params.set('processDefinitionKey', processDefinitionKey);
    }

    return this.http.get<ApiResponse<FlowableProcessInstance[]>>(`${this.baseUrl}/process-instances`, { params })
      .pipe(map(response => response.data));
  }

  /**
   * 启动流程实例
   */
  startProcessInstance(processDefinitionKey: string, businessKey?: string, variables?: any): Observable<any> {
    let params = new HttpParams().set('processDefinitionKey', processDefinitionKey);
    
    if (businessKey) {
      params = params.set('businessKey', businessKey);
    }

    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/process-instances`, variables, { params })
      .pipe(map(response => response.data));
  }

  /**
   * 获取流程定义列表
   */
  getProcessDefinitions(): Observable<any> {
    return this.http.get<ApiResponse<any>>(`${this.baseUrl}/process-definitions`)
      .pipe(map(response => response.data));
  }

  /**
   * 获取流程历史
   */
  getHistoricProcessInstances(processInstanceId?: string, businessKey?: string): Observable<any> {
    let params = new HttpParams();
    if (processInstanceId) {
      params = params.set('processInstanceId', processInstanceId);
    }
    if (businessKey) {
      params = params.set('businessKey', businessKey);
    }

    return this.http.get<ApiResponse<any>>(`${this.baseUrl}/history/process-instances`, { params })
      .pipe(map(response => response.data));
  }

  /**
   * 获取任务历史
   */
  getHistoricTaskInstances(processInstanceId?: string, taskAssignee?: string): Observable<any> {
    let params = new HttpParams();
    if (processInstanceId) {
      params = params.set('processInstanceId', processInstanceId);
    }
    if (taskAssignee) {
      params = params.set('taskAssignee', taskAssignee);
    }

    return this.http.get<ApiResponse<any>>(`${this.baseUrl}/history/task-instances`, { params })
      .pipe(map(response => response.data));
  }

  /**
   * 删除流程实例
   */
  deleteProcessInstance(processInstanceId: string, deleteReason?: string): Observable<any> {
    let params = new HttpParams();
    if (deleteReason) {
      params = params.set('deleteReason', deleteReason);
    }
    
    return this.http.delete<ApiResponse<any>>(`${this.baseUrl}/process-instances/${processInstanceId}`, { params })
      .pipe(map(response => response.data));
  }

  /**
   * 挂起/激活流程实例
   */
  suspendProcessInstance(processInstanceId: string, suspend: boolean): Observable<any> {
    const endpoint = suspend ? 'suspend' : 'activate';
    return this.http.put<ApiResponse<any>>(`${this.baseUrl}/process-instances/${processInstanceId}/${endpoint}`, {})
      .pipe(map(response => response.data));
  }

  /**
   * 获取流程图
   */
  getProcessDiagram(processInstanceId: string): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/process-instances/${processInstanceId}/diagram`, {
      responseType: 'blob'
    });
  }

  /**
   * 部署流程定义
   */
  deployProcessDefinition(name: string, bpmnXml: string): Observable<any> {
    const formData = new FormData();
    const blob = new Blob([bpmnXml], { type: 'application/xml' });
    formData.append('deployment', blob, `${name}.bpmn20.xml`);
    
    return this.http.post<ApiResponse<any>>(`${this.baseUrl}/deployments`, formData)
      .pipe(map(response => response.data));
  }
}