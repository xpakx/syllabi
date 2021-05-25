import { CourseType } from "./course-type";
import { CourseYearForPage } from "./course-year-for-page";

export interface StudyGroupForPage {
    id: number;
    name: string;
    description: string;
    studentLimit: number;
    ongoing: boolean;
    type: CourseType;
}