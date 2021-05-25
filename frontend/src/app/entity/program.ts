import { CourseSummary } from "./course-summary";
import { Institute } from "./institute";

export interface Program {
    id: number;
    name: string;
    description: string;
    organizer: Institute;
}