import { InstituteForPage } from "./institute-for-page";

export interface Job {
    id: number;
    name: string;
    institute: InstituteForPage;
}