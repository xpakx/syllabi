export interface CourseEdit {
    courseCode: string;
    iscedCode: string;
    erasmusCode: string;
    name: string;
    ects: number;
    language: string;
    facultative: boolean;
    stationary: boolean;

    shortDescription: string;
    description: string;
    assessmentRules: string;
    effects: string;
    requirements: string;

    organizerId: number | undefined;
    prerequisites: number[] | undefined;
    semesters: number[] | undefined;
}
