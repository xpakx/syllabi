import { Job } from "./job";
import { UserWithOnlyId } from "./user-with-only-id";

export interface Teacher {
    id: number;
    name: string;
    surname: string;
    title: string;
    phone: string;
    email: string;
    pbnId: string;
    user: UserWithOnlyId;
    job: Job;
}