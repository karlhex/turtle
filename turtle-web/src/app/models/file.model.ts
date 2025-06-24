import { ClientType } from '../types/client-type.enum';
import { FileType } from '../types/file-type.enum';

export interface FileMetadata {
  id: number;
  clientType: ClientType;
  clientId: number;
  fileType: FileType;
  originalName: string;
  storageName: string;
  fileSize: number;
  mimeType: string;
  uploadTime: string;
  createdAt: string;
  updatedAt: string;
}
