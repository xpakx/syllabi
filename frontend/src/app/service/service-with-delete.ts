import { Observable } from "rxjs";

export interface ServiceWithDelete {
    delete(id: number): Observable<any>;
}