import { Observable } from "rxjs";
import { Page } from "../entity/page";
import { ServiceWithDelete } from "./service-with-delete";

export interface ServiceWithGetAll<T> extends ServiceWithDelete {
    getAll(): Observable<Page<T>> 
    getAllForPage(page: number): Observable<Page<T>> 
}