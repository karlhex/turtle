export interface Department {
    id?: number;
    name: string;
    description?: string;
    code: string;
    isActive?: boolean;
    parentId?: number;
    managerId?: number;
}
