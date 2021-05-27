import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Page } from "../entity/page";
import { StudyGroupForPage } from "../entity/study-group-for-page";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";
import { StudyGroupService } from "./study-group.service";

@Injectable({
    providedIn: 'root'
})
export class YearGroupsService extends StudyGroupService
implements ServiceWithGetAllChildren<StudyGroupForPage> {

    constructor(protected http: HttpClient) { 
        super(http);
    }

    public getAllChildren(id: number): Observable<Page<StudyGroupForPage>> {
        return this.getAll(id);
    }
    
    public getAllChildrenForPage(id: number, page: number): Observable<Page<StudyGroupForPage>> {
        return this.getAllForPage(id, page);
    }
}