import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { LiteratureForPage } from "../entity/literature-for-page";
import { Page } from "../entity/page";
import { LiteratureService } from "./literature.service";
import { ServiceWithDelete } from "./service-with-delete";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";

@Injectable({
    providedIn: 'root'
})
export class GroupLiteratureService extends LiteratureService 
implements ServiceWithDelete, ServiceWithGetAllChildren<LiteratureForPage>  {

    constructor(protected http: HttpClient) { 
        super(http);
    }

    public delete(id: number): Observable<any> {
        return this.deleteGroupLiterature(id);
    }

    public getAllByParentId(id: number): Observable<Page<LiteratureForPage>> {
        return this.getAllGroupLiterature(id);
    }
    
    public getAllByParentIdForPage(id: number, page: number): Observable<Page<LiteratureForPage>> {
        return this.getAllGroupLiteratureForPage(id, page);
    }
}