import { CourseSummary } from "./course-summary";

export interface CourseYearForPage {
    id: number;
    parent: CourseSummary;
    description: string;
    startDate: Date;
    endDate: Date;
}