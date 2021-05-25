export interface StudyGroupRequest {
    description: string;
    name: string;
    studentLimit: number;
    ongoing: boolean;
    courseTypeId: number;
    teachers: number[];
}