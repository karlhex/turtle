export enum ActionType {
  CREATED = 'CREATED',
  SUBMITTED = 'SUBMITTED',
  REVIEWED = 'REVIEWED',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  UPDATED = 'UPDATED',
  CONVERTED = 'CONVERTED',
  WORKFLOW_STARTED = 'WORKFLOW_STARTED',
  WORKFLOW_COMPLETED = 'WORKFLOW_COMPLETED',
  STATUS_CHANGED = 'STATUS_CHANGED',
  COMMENT_ADDED = 'COMMENT_ADDED',
  DOCUMENT_UPLOADED = 'DOCUMENT_UPLOADED',
  DOCUMENT_DELETED = 'DOCUMENT_DELETED',
  NOTIFICATION_SENT = 'NOTIFICATION_SENT',
  TIMEOUT_REMINDER = 'TIMEOUT_REMINDER',
  SYSTEM_ACTION = 'SYSTEM_ACTION'
}

export interface ApplicationHistory {
  id: number;
  applicationId: number;
  actionType: ActionType;
  actionTypeDescription: string;
  fromStatus?: string;
  fromStatusDescription?: string;
  toStatus?: string;
  toStatusDescription?: string;
  operatorId?: number;
  operatorName?: string;
  description: string;
  details?: string;
  workflowTaskId?: string;
  createdAt: string;
  clientIp?: string;
  userAgent?: string;
}

export interface ApplicationTimelineItem {
  id: number;
  title: string;
  description: string;
  timestamp: string;
  actionType: ActionType;
  operatorName?: string;
  icon: string;
  color: string;
  details?: any;
}