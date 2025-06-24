import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FileMetadata } from '@app/models/file.model';
import { ClientType } from '@app/types/client-type.enum';
import { ApiResponse } from '@app/models/api.model';

@Injectable({
  providedIn: 'root',
})
export class FileService {
  private readonly apiUrl = '/api/files';

  constructor(private http: HttpClient) {}

  upload(
    file: File,
    fileMetadata: FileMetadata,
    clientType: ClientType,
    clientId: number
  ): Observable<ApiResponse<FileMetadata>> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('clientType', clientType.toString());
    formData.append('clientId', clientId.toString());
    formData.append('fileType', fileMetadata.fileType.toString());
    formData.append('originalName', fileMetadata.originalName);
    formData.append('fileSize', fileMetadata.fileSize.toString());
    formData.append('mimeType', fileMetadata.mimeType);

    return this.http.post<ApiResponse<FileMetadata>>(`${this.apiUrl}/upload`, formData);
  }

  findByClient(clientType: ClientType, clientId: number): Observable<ApiResponse<FileMetadata[]>> {
    const params = new HttpParams()
      .set('clientType', clientType)
      .set('clientId', clientId.toString());

    return this.http.get<ApiResponse<FileMetadata[]>>(this.apiUrl, { params });
  }

  download(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/download`, {
      responseType: 'blob',
    });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
