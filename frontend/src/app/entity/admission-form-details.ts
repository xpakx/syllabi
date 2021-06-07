import { AdmissionWithNameAndId } from "./admission-with-name-and-id";
import { UserWithOnlyId } from "./user-with-only-id";

export interface AdmissionFormDetails {
    id: number;
    name: string;
    surname: string;
    documentId: string;
    verified: boolean;
    accepted: boolean;
    discarded: boolean;
    user: UserWithOnlyId;
    admission: AdmissionWithNameAndId;
}