import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Page } from "../entity/page";
import { StudyGroupForPage } from "../entity/study-group-for-page";
import { CourseYearService } from "./course-year.service";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";

@Injectable({
    providedIn: 'root'
})
export class YearGroupsService extends CourseYearService
implements ServiceWithGetAllChildren<StudyGroupForPage> {

    constructor(protected http: HttpClient) { 
        super(http);
    }

    public getAllChildren(id: number): Observable<Page<StudyGroupForPage>> {
        return this.getAllGroupsForYear(id);
    }
    
    public getAllChildrenForPage(id: number, page: number): Observable<Page<StudyGroupForPage>> {
        return this.getAllGroupsForYearForPage(id, page);
    }
}