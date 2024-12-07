import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { ClientType } from '@app/types/client-type.enum';
import { FileType } from '@app/types/file-type.enum';
import { FileMetadata } from '@app/models/file.model';
import { FileService } from '@app/services/file.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.scss']
})
export class FileUploadComponent implements OnInit {
  @Input() clientType!: ClientType;
  @Input() clientId!: number;
  @Input() files: FileMetadata[] = [];
  @Output() fileUploaded = new EventEmitter<FileMetadata>();
  @Output() fileDeleted = new EventEmitter<number>();

  uploading = false;

  constructor(
    private fileService: FileService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadFiles();
  }

  private loadFiles(): void {
    if (this.clientType && this.clientId) {
      this.fileService.findByClient(this.clientType, this.clientId)
        .subscribe({
          next: (response) => {
            this.files = response.data;
          },
          error: (error) => {
            this.snackBar.open('加载文件列表失败', '关闭', { duration: 3000 });
            console.error('Error loading files:', error);
          }
        });
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      this.uploadFile(input.files[0]);
    }
  }

  private uploadFile(file: File): void {
    this.uploading = true;
    const fileMetadata: FileMetadata = {
      id: 0, // Temporary id, will be assigned by backend
      clientType: this.clientType,
      clientId: this.clientId,
      fileType: FileType.UNKNOWN, // You may want to determine this based on file extension or type
      originalName: file.name,
      storageName: '', // Backend will generate this
      fileSize: file.size,
      mimeType: file.type,
      uploadTime: new Date().toISOString(),
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };

    this.fileService.upload(file, fileMetadata, this.clientType, this.clientId)
      .subscribe({
        next: (response) => {
          this.files.push(response.data);
          this.fileUploaded.emit(response.data);
          this.snackBar.open('文件上传成功', '关闭', { duration: 3000 });
        },
        error: (error) => {
          this.snackBar.open('文件上传失败', '关闭', { duration: 3000 });
          console.error('Error uploading file:', error);
        },
        complete: () => {
          this.uploading = false;
        }
      });
  }

  downloadFile(fileId: number): void {
    this.fileService.download(fileId)
      .subscribe({
        next: (blob) => {
          const file = this.files.find(f => f.id === fileId);
          if (file) {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = file.originalName;
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            window.URL.revokeObjectURL(url);
          }
        },
        error: (error) => {
          this.snackBar.open('文件下载失败', '关闭', { duration: 3000 });
          console.error('Error downloading file:', error);
        }
      });
  }

  deleteFile(fileId: number): void {
    if (confirm('确定要删除这个文件吗？')) {
      this.fileService.delete(fileId)
        .subscribe({
          next: () => {
            this.files = this.files.filter(f => f.id !== fileId);
            this.fileDeleted.emit(fileId);
            this.snackBar.open('文件删除成功', '关闭', { duration: 3000 });
          },
          error: (error) => {
            this.snackBar.open('文件删除失败', '关闭', { duration: 3000 });
            console.error('Error deleting file:', error);
          }
        });
    }
  }
}