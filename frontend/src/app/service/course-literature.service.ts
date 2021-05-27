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
export class CourseLiteratureService extends LiteratureService 
implements ServiceWithDelete, ServiceWithGetAllChildren<LiteratureForPage> {

    constructor(protected http: HttpClient) { 
        super(http);
    }

    public delete(id: number): Observable<any> {
        return this.http.delete<any>(`${this.url}/courses/literature/${id}`);
    }

    public getAllByParentId(id: number): Observable<Page<LiteratureForPage>> {
        return this.getAllCourseLiterature(id);
    }
    
    public getAllByParentIdForPage(id: number, page: number): Observable<Page<LiteratureForPage>> {
        return this.getAllCourseLiteratureForPage(id, page);
    }
}