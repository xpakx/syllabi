export interface Admission {
    id: number;
    name: string;
    closed: boolean;
    studentLimit: number;
    startDate: Date;
    endDate: Date;
}