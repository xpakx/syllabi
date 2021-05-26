import { Observable } from "rxjs";
import { Page } from "../entity/page";

export interface ServiceWithGetAllChildren<T> {
    getAllChildren(id: number): Observable<Page<T>> 
    getAllChildrenForPage(id: number, page: number): Observable<Page<T>> 
}