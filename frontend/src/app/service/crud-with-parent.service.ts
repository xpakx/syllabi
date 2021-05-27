import { Observable } from "rxjs";
import { ServiceWithDelete } from "./service-with-delete";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";

export interface CrudWithParentService<ForPageType, DetailsType, CreateType, EditType, ResponseType> 
extends ServiceWithGetAllChildren<ForPageType>, ServiceWithDelete {
    getById(id: number): Observable<DetailsType>
    addNew(course: CreateType): Observable<ResponseType>
    edit(id: number, course: EditType): Observable<ResponseType>
}