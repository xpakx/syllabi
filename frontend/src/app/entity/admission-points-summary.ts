import { AdmissionWeightSummary } from "./admission-weight-summary";

export interface AdmissionPointsSummary {
    id: number;
    points: number;
    weight: AdmissionWeightSummary;
}