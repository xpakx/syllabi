import { CourseYearForPage } from "./course-year-for-page";

export interface StudyGroupSummary {
    id: number;
    name: string;
    year: CourseYearForPage;
}