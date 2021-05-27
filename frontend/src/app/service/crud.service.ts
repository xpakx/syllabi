import { Observable } from "rxjs";
import { ServiceWithDelete } from "./service-with-delete";
import { ServiceWithGetAll } from "./service-with-get-all";
import { ServiceWithGetById } from "./service-with-get-by-id";

export interface CrudService<ForPageType, DetailsType, CreateType, EditType, ResponseType> 
extends ServiceWithGetAll<ForPageType>, ServiceWithDelete, ServiceWithGetById<DetailsType> {
    addNew(course: CreateType): Observable<ResponseType>
    edit(id: number, course: EditType): Observable<ResponseType>
}