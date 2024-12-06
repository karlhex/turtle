export enum ContractStatus {
    PENDING_SIGNATURE = 'PENDING_SIGNATURE',  // 等待签署
    IN_PROGRESS = 'IN_PROGRESS',             // 执行中
    COMPLETED = 'COMPLETED',                 // 已结束
    CANCELLED = 'CANCELLED',                 // 已取消
    SUSPENDED = 'SUSPENDED'                  // 已挂起
}
