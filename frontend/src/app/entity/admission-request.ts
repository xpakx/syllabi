import { AdmissionWeight } from "./admission-weight";

export interface AdmissionRequest {
    name: string;
    studentLimit: number;
    startDate: Date;
    endDate: Date;
    weights: AdmissionWeight[];
}