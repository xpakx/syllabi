import { UserWithOnlyId } from "./user-with-only-id";

export interface TeacherSummary {
    id: number;
    title: string;
    name: string;
    surname: string;
    user: UserWithOnlyId;
}