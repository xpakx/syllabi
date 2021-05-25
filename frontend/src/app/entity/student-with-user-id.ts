import { UserWithOnlyId } from "./user-with-only-id";

export interface StudentWithUserId {
    id: number;
    name: string;
    surname: string;
    user: UserWithOnlyId;
}