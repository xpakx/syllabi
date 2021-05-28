import { CourseSummary } from "./course-summary";
import { InstituteForPage } from "./institute-for-page";
import { SemesterSummary } from "./semester-summary";

export interface CourseDetails {
    id: number;
    courseCode: string;
    iscedCode: string;
    erasmusCode: string;
    name: string;
    ects: number;
    language: string;
    facultative: boolean;
    stationary: boolean;

    semesters: SemesterSummary[];
    prerequisites: CourseSummary[];
    organizer: InstituteForPage;

    shortDescription: string;
    description: string;
    assessmentRules: string;
    effects: string;
    requirements: string;
}
