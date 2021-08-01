import { AdmissionPointsReview } from "./admission-points-review";

export interface AdmissionFormReviewRequest {
    name: string;
    surname: string;
    documentId: string;
    verify: boolean;
    points: AdmissionPointsReview[];
}