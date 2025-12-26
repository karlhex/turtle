import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { ApplicationHistory, ApplicationTimelineItem, ActionType } from '../models/application-history.model';

@Injectable({
  providedIn: 'root'
})
export class ApplicationHistoryService {

  private apiUrl = `${environment.apiUrl}/employee-applications`;

  constructor(private http: HttpClient) {}

  /**
   * 获取申请的操作历史记录
   */
  getApplicationHistory(applicationId: number): Observable<ApplicationHistory[]> {
    return this.http.get<ApplicationHistory[]>(`${this.apiUrl}/${applicationId}/history`);
  }

  /**
   * 获取申请的状态变更历史
   */
  getApplicationStatusHistory(applicationId: number): Observable<ApplicationHistory[]> {
    return this.http.get<ApplicationHistory[]>(`${this.apiUrl}/${applicationId}/status-history`);
  }

  /**
   * 将历史记录转换为时间线项目
   */
  convertToTimelineItems(histories: ApplicationHistory[]): ApplicationTimelineItem[] {
    return histories.map(history => this.mapToTimelineItem(history))
                   .sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime());
  }

  /**
   * 记录新的历史操作（系统调用）
   */
  recordHistory(applicationId: number, actionType: ActionType, description: string, details?: any): Observable<ApplicationHistory> {
    return this.http.post<ApplicationHistory>(`${this.apiUrl}/${applicationId}/history`, {
      actionType,
      description,
      details: details ? JSON.stringify(details) : null
    });
  }

  /**
   * 获取操作统计信息
   */
  getOperationStatistics(startDate: string, endDate: string): Observable<{ [key: string]: number }> {
    return this.http.get<{ [key: string]: number }>(`${this.apiUrl}/history/statistics`, {
      params: { startDate, endDate }
    });
  }

  /**
   * 将历史记录映射为时间线项目
   */
  private mapToTimelineItem(history: ApplicationHistory): ApplicationTimelineItem {
    const config = this.getActionConfig(history.actionType);

    return {
      id: history.id,
      title: this.getActionTitle(history),
      description: history.description,
      timestamp: history.createdAt,
      actionType: history.actionType,
      operatorName: history.operatorName,
      icon: config.icon,
      color: config.color,
      details: history.details ? JSON.parse(history.details) : null
    };
  }

  /**
   * 获取操作标题
   */
  private getActionTitle(history: ApplicationHistory): string {
    const operator = history.operatorName || '系统';

    switch (history.actionType) {
      case ActionType.CREATED:
        return `${operator} 创建了申请`;
      case ActionType.SUBMITTED:
        return `${operator} 提交了申请`;
      case ActionType.REVIEWED:
        return `${operator} 审核了申请`;
      case ActionType.APPROVED:
        return `${operator} 通过了申请`;
      case ActionType.REJECTED:
        return `${operator} 拒绝了申请`;
      case ActionType.UPDATED:
        return `${operator} 更新了申请`;
      case ActionType.CONVERTED:
        return `${operator} 将申请转换为员工记录`;
      case ActionType.WORKFLOW_STARTED:
        return `系统启动了工作流`;
      case ActionType.WORKFLOW_COMPLETED:
        return `工作流已完成`;
      case ActionType.STATUS_CHANGED:
        return `申请状态已变更`;
      case ActionType.COMMENT_ADDED:
        return `${operator} 添加了评论`;
      case ActionType.DOCUMENT_UPLOADED:
        return `${operator} 上传了文档`;
      case ActionType.DOCUMENT_DELETED:
        return `${operator} 删除了文档`;
      case ActionType.NOTIFICATION_SENT:
        return `系统发送了通知`;
      case ActionType.TIMEOUT_REMINDER:
        return `系统发送了超时提醒`;
      case ActionType.SYSTEM_ACTION:
        return `系统执行了操作`;
      default:
        return `${operator} 执行了 ${history.actionTypeDescription || history.actionType}`;
    }
  }

  /**
   * 获取操作配置（图标和颜色）
   */
  private getActionConfig(actionType: ActionType): { icon: string; color: string } {
    switch (actionType) {
      case ActionType.CREATED:
        return { icon: 'add_circle', color: '#4caf50' };
      case ActionType.SUBMITTED:
        return { icon: 'send', color: '#2196f3' };
      case ActionType.REVIEWED:
        return { icon: 'rate_review', color: '#ff9800' };
      case ActionType.APPROVED:
        return { icon: 'check_circle', color: '#4caf50' };
      case ActionType.REJECTED:
        return { icon: 'cancel', color: '#f44336' };
      case ActionType.UPDATED:
        return { icon: 'edit', color: '#2196f3' };
      case ActionType.CONVERTED:
        return { icon: 'person_add', color: '#9c27b0' };
      case ActionType.WORKFLOW_STARTED:
        return { icon: 'play_circle', color: '#607d8b' };
      case ActionType.WORKFLOW_COMPLETED:
        return { icon: 'check_circle_outline', color: '#4caf50' };
      case ActionType.STATUS_CHANGED:
        return { icon: 'swap_horiz', color: '#ff9800' };
      case ActionType.COMMENT_ADDED:
        return { icon: 'comment', color: '#2196f3' };
      case ActionType.DOCUMENT_UPLOADED:
        return { icon: 'cloud_upload', color: '#4caf50' };
      case ActionType.DOCUMENT_DELETED:
        return { icon: 'delete', color: '#f44336' };
      case ActionType.NOTIFICATION_SENT:
        return { icon: 'notifications', color: '#ff9800' };
      case ActionType.TIMEOUT_REMINDER:
        return { icon: 'schedule', color: '#f44336' };
      case ActionType.SYSTEM_ACTION:
        return { icon: 'settings', color: '#607d8b' };
      default:
        return { icon: 'info', color: '#607d8b' };
    }
  }
}