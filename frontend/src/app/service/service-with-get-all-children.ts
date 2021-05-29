import { Observable } from "rxjs";
import { Page } from "../entity/page";

export interface ServiceWithGetAllChildren<T, U> {
    getAllByParentId(id: number): Observable<Page<T>> 
    getAllByParentIdForPage(id: number, page: number): Observable<Page<T>> 
    getParentById(id: number): Observable<U>
}