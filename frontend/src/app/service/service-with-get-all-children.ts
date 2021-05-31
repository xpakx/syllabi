import { Observable } from "rxjs";
import { Page } from "../entity/page";
import { ServiceWithDelete } from "./service-with-delete";

export interface ServiceWithGetAllChildren<T, U> extends ServiceWithDelete {
    getAllByParentId(id: number): Observable<Page<T>> 
    getAllByParentIdForPage(id: number, page: number): Observable<Page<T>> 
    getParentById(id: number): Observable<U>
}