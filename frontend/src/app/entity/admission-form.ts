export interface AdmissionForm {
    id: number;
    name: string;
    surname: string;
    documentId: string;
    verified: boolean;
    accepted: boolean;
    discarded: boolean;
}