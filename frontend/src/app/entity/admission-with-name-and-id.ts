import { ProgramWithOnlyId } from "./program-with-only-id";

export interface AdmissionWithNameAndId {
    id: number;
    name: string;
    program: ProgramWithOnlyId;
}