import { AdmissionWeight } from "./admission-weight";
import { ProgramSummary } from "./program-summary";

export interface AdmissionDetails {
    id: number;
    name: string;
    closed: boolean;
    studentLimit: number;
    startDate: Date;
    endDate: Date;
    program: ProgramSummary;
    weights: AdmissionWeight[];
}