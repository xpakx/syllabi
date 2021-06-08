import { Observable } from "rxjs";

export interface ServiceWithGetById<T> {
    getById(id: number): Observable<T>
    delete(id: number): Observable<any>
}