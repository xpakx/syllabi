import { CourseSummary } from "./course-summary";

export interface CourseYear {
    id: number;
    parent: CourseSummary;
    description: string;
    startDate: Date;
    endDate: Date;
}