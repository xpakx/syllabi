import { Observable } from "rxjs";
import { ServiceWithDelete } from "./service-with-delete";
import { ServiceWithGetAll } from "./service-with-get-all";

export interface CrudService<ForPageType, DetailsType, CreateType, EditType, ResponseType> 
extends ServiceWithGetAll<ForPageType>, ServiceWithDelete {
    getById(id: number): Observable<DetailsType>
    addNew(course: CreateType): Observable<ResponseType>
    edit(id: number, course: EditType): Observable<ResponseType>
}