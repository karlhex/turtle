export interface User {
    id?: number;
    username: string;
    email: string;
    roles?: Set<string>;
    employee?: number;  // Reference to Employee ID
    createdAt?: Date;
    updatedAt?: Date;
}
