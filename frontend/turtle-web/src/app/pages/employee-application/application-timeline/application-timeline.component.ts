import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { takeUntil, finalize } from 'rxjs/operators';
import { ApplicationHistory, ApplicationTimelineItem } from '../../../models/application-history.model';
import { ApplicationHistoryService } from '../../../services/application-history.service';
import { WebSocketService, ApplicationUpdateMessage } from '../../../services/websocket.service';

@Component({
  selector: 'app-application-timeline',
  templateUrl: './application-timeline.component.html',
  styleUrls: ['./application-timeline.component.scss']
})
export class ApplicationTimelineComponent implements OnInit, OnDestroy {

  @Input() applicationId!: number;
  @Input() showFullHistory = true; // 是否显示完整历史（false时只显示状态变更）

  timelineItems: ApplicationTimelineItem[] = [];
  loading = false;
  error: string | null = null;

  private destroy$ = new Subject<void>();

  constructor(
    private historyService: ApplicationHistoryService,
    private webSocketService: WebSocketService
  ) {}

  ngOnInit(): void {
    if (this.applicationId) {
      this.loadTimeline();
      this.subscribeToRealTimeUpdates();
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();

    if (this.applicationId) {
      this.webSocketService.unsubscribeFromApplication(this.applicationId);
    }
  }

  /**
   * 加载时间线数据
   */
  loadTimeline(): void {
    this.loading = true;
    this.error = null;

    const historyObservable = this.showFullHistory
      ? this.historyService.getApplicationHistory(this.applicationId)
      : this.historyService.getApplicationStatusHistory(this.applicationId);

    historyObservable
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.loading = false)
      )
      .subscribe({
        next: (histories: ApplicationHistory[]) => {
          this.timelineItems = this.historyService.convertToTimelineItems(histories);
        },
        error: (error) => {
          console.error('Failed to load application timeline:', error);
          this.error = '加载时间线失败，请稍后重试';
        }
      });
  }

  /**
   * 订阅实时更新
   */
  private subscribeToRealTimeUpdates(): void {
    // 订阅该申请的状态更新
    this.webSocketService.subscribeToApplication(this.applicationId);

    // 监听状态更新消息
    this.webSocketService.applicationUpdates$
      .pipe(takeUntil(this.destroy$))
      .subscribe((update: ApplicationUpdateMessage | null) => {
        if (update && update.applicationId === this.applicationId) {
          // 有新的状态更新，重新加载时间线
          this.loadTimeline();
        }
      });
  }

  /**
   * 刷新时间线
   */
  refresh(): void {
    this.loadTimeline();
  }

  /**
   * 格式化时间显示
   */
  formatTime(timestamp: string): string {
    const date = new Date(timestamp);
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffMins = Math.floor(diffMs / (1000 * 60));
    const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
    const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));

    if (diffMins < 1) {
      return '刚刚';
    } else if (diffMins < 60) {
      return `${diffMins}分钟前`;
    } else if (diffHours < 24) {
      return `${diffHours}小时前`;
    } else if (diffDays < 7) {
      return `${diffDays}天前`;
    } else {
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    }
  }

  /**
   * 格式化详细时间
   */
  formatDetailTime(timestamp: string): string {
    const date = new Date(timestamp);
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    });
  }

  /**
   * 获取时间线项目的样式类
   */
  getTimelineItemClass(item: ApplicationTimelineItem): string {
    const baseClass = 'timeline-item';

    switch (item.actionType) {
      case 'APPROVED':
        return `${baseClass} success`;
      case 'REJECTED':
        return `${baseClass} error`;
      case 'CREATED':
      case 'SUBMITTED':
        return `${baseClass} info`;
      case 'CONVERTED':
        return `${baseClass} primary`;
      default:
        return `${baseClass} default`;
    }
  }

  /**
   * 检查是否有详细信息
   */
  hasDetails(item: ApplicationTimelineItem): boolean {
    return !!(item.details && Object.keys(item.details).length > 0);
  }

  /**
   * 格式化详细信息显示
   */
  formatDetails(details: any): string {
    if (!details) return '';

    if (typeof details === 'string') {
      return details;
    }

    return Object.entries(details)
      .map(([key, value]) => `${key}: ${value}`)
      .join(', ');
  }
}