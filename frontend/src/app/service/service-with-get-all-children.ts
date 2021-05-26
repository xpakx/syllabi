import { Observable } from "rxjs";
import { Page } from "../entity/page";

export interface ServiceWithGetAllChildren<T> {
    getAll(id: number): Observable<Page<T>> 
    getAllForPage(id: number, page: number): Observable<Page<T>> 
}