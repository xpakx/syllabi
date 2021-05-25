import { CourseSummary } from "./course-summary";
import { CourseType } from "./course-type";
import { CourseYearForPage } from "./course-year-for-page";
import { TeacherSummary } from "./teacher-summary";

export interface StudyGroup {
    id: number;
    name: string;
    description: string;
    studentLimit: number;
    ongoing: boolean;
    type: CourseType;
    year: CourseYearForPage;
    teachers: TeacherSummary[];
}