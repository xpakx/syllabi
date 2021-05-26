import { Observable } from "rxjs";
import { Page } from "../entity/page";

export interface ServiceWithGetAll<T> {
    getAll(): Observable<Page<T>> 
    getAllForPage(page: number): Observable<Page<T>> 
}