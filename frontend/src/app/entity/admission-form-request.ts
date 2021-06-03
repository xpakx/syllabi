import { AdmissionPoints } from "./admission-points";

export interface AdmissionFormRequest {
    name: string;
    surname: string;
    documentId: string;
    points: AdmissionPoints[];
}