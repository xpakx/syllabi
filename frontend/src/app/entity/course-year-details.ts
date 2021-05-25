import { CourseSummary } from "./course-summary";
import { TeacherSummary } from "./teacher-summary";

export interface CourseYearDetails {
    id: number;
    parent: CourseSummary;
    assessmentRules: string;
    description: string;
    commentary: string;
    startDate: Date;
    endDate: Date;
    coordinatedBy: TeacherSummary[];
}