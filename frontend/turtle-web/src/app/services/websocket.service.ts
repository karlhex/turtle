import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { TokenStorageService } from './token-storage.service';

export interface ApplicationUpdateMessage {
  applicationId: number;
  status: string;
  updatedAt: string;
  message?: string;
}

export interface WebSocketMessage {
  type: 'APPLICATION_STATUS_UPDATE' | 'APPROVAL_NOTIFICATION' | 'SYSTEM_NOTIFICATION' | 'AUTH' | 'SUBSCRIBE_APPLICATION' | 'UNSUBSCRIBE_APPLICATION';
  data: any;
  timestamp: string;
}

/**
 * WebSocket服务
 * 用于实时接收员工申请状态更新和通知
 */
@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  private ws?: WebSocket;
  private reconnectInterval = 5000; // 5秒重连间隔
  private maxReconnectAttempts = 10;
  private reconnectAttempts = 0;
  private isConnecting = false;

  // 连接状态主题
  private connectionStatusSubject = new BehaviorSubject<boolean>(false);
  public connectionStatus$ = this.connectionStatusSubject.asObservable();

  // 消息主题
  private messageSubject = new BehaviorSubject<WebSocketMessage | null>(null);
  public messages$ = this.messageSubject.asObservable();

  // 申请状态更新主题
  private applicationUpdateSubject = new BehaviorSubject<ApplicationUpdateMessage | null>(null);
  public applicationUpdates$ = this.applicationUpdateSubject.asObservable();

  constructor(private tokenStorage: TokenStorageService) {
    this.initializeConnection();
  }

  /**
   * 初始化WebSocket连接
   */
  private initializeConnection(): void {
    if (this.isConnecting || this.ws?.readyState === WebSocket.OPEN) {
      return;
    }

    const token = this.tokenStorage.getToken();
    if (!token) {
      console.log('No auth token available, skipping WebSocket connection');
      return;
    }

    this.connect();
  }

  /**
   * 建立WebSocket连接
   */
  private connect(): void {
    if (this.isConnecting) {
      return;
    }

    this.isConnecting = true;
    const token = this.tokenStorage.getToken();
    const wsUrl = this.getWebSocketUrl();

    console.log('Connecting to WebSocket:', wsUrl);

    try {
      this.ws = new WebSocket(wsUrl);

      this.ws.onopen = (event) => {
        console.log('WebSocket connected');
        this.isConnecting = false;
        this.reconnectAttempts = 0;
        this.connectionStatusSubject.next(true);

        // 发送认证信息
        if (token) {
          this.send({
            type: 'AUTH',
            data: { token },
            timestamp: new Date().toISOString()
          });
        }
      };

      this.ws.onmessage = (event) => {
        try {
          const message: WebSocketMessage = JSON.parse(event.data);
          console.log('WebSocket message received:', message);

          this.messageSubject.next(message);

          // 处理特定类型的消息
          if (message.type === 'APPLICATION_STATUS_UPDATE') {
            this.applicationUpdateSubject.next(message.data as ApplicationUpdateMessage);
          }
        } catch (error) {
          console.error('Failed to parse WebSocket message:', error);
        }
      };

      this.ws.onclose = (event) => {
        console.log('WebSocket disconnected:', event.code, event.reason);
        this.isConnecting = false;
        this.connectionStatusSubject.next(false);

        // 尝试重连
        this.handleReconnect();
      };

      this.ws.onerror = (error) => {
        console.error('WebSocket error:', error);
        this.isConnecting = false;
        this.connectionStatusSubject.next(false);
      };

    } catch (error) {
      console.error('Failed to create WebSocket connection:', error);
      this.isConnecting = false;
      this.handleReconnect();
    }
  }

  /**
   * 处理重连逻辑
   */
  private handleReconnect(): void {
    if (this.reconnectAttempts >= this.maxReconnectAttempts) {
      console.error('Max reconnect attempts reached');
      return;
    }

    this.reconnectAttempts++;
    console.log(`Attempting to reconnect... (${this.reconnectAttempts}/${this.maxReconnectAttempts})`);

    setTimeout(() => {
      this.connect();
    }, this.reconnectInterval * this.reconnectAttempts); // 逐渐增加重连间隔
  }

  /**
   * 发送消息
   */
  private send(message: WebSocketMessage): void {
    if (this.ws?.readyState === WebSocket.OPEN) {
      this.ws.send(JSON.stringify(message));
    } else {
      console.warn('WebSocket is not connected, cannot send message');
    }
  }

  /**
   * 获取WebSocket URL
   */
  private getWebSocketUrl(): string {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const host = window.location.hostname;
    const port = '8080'; // 后端WebSocket端口
    return `${protocol}//${host}:${port}/ws/notifications`;
  }

  /**
   * 手动连接
   */
  public connectManually(): void {
    if (this.ws?.readyState === WebSocket.OPEN) {
      return;
    }
    this.connect();
  }

  /**
   * 断开连接
   */
  public disconnect(): void {
    if (this.ws) {
      this.ws.close();
      this.ws = undefined;
    }
    this.connectionStatusSubject.next(false);
  }

  /**
   * 重置连接
   */
  public resetConnection(): void {
    this.disconnect();
    this.reconnectAttempts = 0;
    setTimeout(() => {
      this.initializeConnection();
    }, 1000);
  }

  /**
   * 订阅特定申请的状态更新
   */
  public subscribeToApplication(applicationId: number): void {
    this.send({
      type: 'SUBSCRIBE_APPLICATION',
      data: { applicationId },
      timestamp: new Date().toISOString()
    });
  }

  /**
   * 取消订阅特定申请的状态更新
   */
  public unsubscribeFromApplication(applicationId: number): void {
    this.send({
      type: 'UNSUBSCRIBE_APPLICATION',
      data: { applicationId },
      timestamp: new Date().toISOString()
    });
  }

  /**
   * 获取当前连接状态
   */
  public isConnected(): boolean {
    return this.ws?.readyState === WebSocket.OPEN;
  }

  /**
   * 服务销毁时清理资源
   */
  public ngOnDestroy(): void {
    this.disconnect();
  }
}