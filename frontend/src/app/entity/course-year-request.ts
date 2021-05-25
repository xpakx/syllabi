export interface CourseYearRequest {
    description: string;
    startDate: Date;
    endDate: Date;
    assessmentRules: string;
    commentary: string;
    coordinators: number[] | undefined;
}
