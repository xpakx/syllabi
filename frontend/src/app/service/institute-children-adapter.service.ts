import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Institute } from "../entity/institute";
import { InstituteForPage } from "../entity/institute-for-page";
import { Page } from "../entity/page";
import { InstituteService } from "./institute.service";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";

@Injectable({
    providedIn: 'root'
})
export class InstituteChildrenAdapterService
implements ServiceWithGetAllChildren<InstituteForPage, Institute> {

    constructor(protected http: HttpClient, private service: InstituteService) {  }

    public getAllByParentId(id: number): Observable<Page<InstituteForPage>> {
        return this.service.getAllChildren(id);
    }
    
    public getAllByParentIdForPage(id: number, page: number): Observable<Page<InstituteForPage>> {
        return this.service.getAllChildrenForPage(id, page);
    }

    public getParentById(id: number): Observable<Institute> {
        return this.service.getById(id);
    }
}