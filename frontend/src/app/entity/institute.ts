import { InstituteForPage } from "./institute-for-page";

export interface Institute {
    id: number;
    name: string;
    code: string;
    url: string;
    phone: string;
    address: string;
    parent: InstituteForPage;
}