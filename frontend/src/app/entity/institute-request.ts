export interface InstituteRequest {
    name: string;
    code: string;
    url: string;
    phone: string;
    address: string;
    parentId: number | undefined;
}