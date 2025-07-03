/**
 * API Response interface
 */
export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

/**
 * Page Response interface
 */
export interface PageResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  isLast: boolean;
  isFirst: boolean;
}

/**
 * Sort Parameters interface for table sorting
 */
export interface SortParams {
  sortBy: string;
  direction: 'ASC' | 'DESC';
}
