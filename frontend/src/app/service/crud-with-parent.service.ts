import { Observable } from "rxjs";
import { ServiceWithDelete } from "./service-with-delete";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";

export interface CrudWithParentService<ForPageType, DetailsType, CreateType, EditType, ResponseType, ParentType> 
extends ServiceWithGetAllChildren<ForPageType>, ServiceWithDelete {
    getById(id: number): Observable<DetailsType>
    addNew(parentId: number, course: CreateType): Observable<ResponseType>
    edit(id: number, course: EditType): Observable<ResponseType>
    getParentById(id: number): Observable<ParentType>
}