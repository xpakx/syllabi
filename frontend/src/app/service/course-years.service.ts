import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { CourseYearForPage } from "../entity/course-year-for-page";
import { LiteratureForPage } from "../entity/literature-for-page";
import { Page } from "../entity/page";
import { CourseYearService } from "./course-year.service";
import { CourseService } from "./course.service";
import { LiteratureService } from "./literature.service";
import { ServiceWithDelete } from "./service-with-delete";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";

@Injectable({
    providedIn: 'root'
})
export class CourseYearsService extends CourseYearService
implements ServiceWithGetAllChildren<CourseYearForPage> {

    constructor(protected http: HttpClient) { 
        super(http);
    }

    public getAllByParentId(id: number): Observable<Page<CourseYearForPage>> {
        return this.getAllActiveYearsForCourse(id);
    }
    
    public getAllByParentIdForPage(id: number, page: number): Observable<Page<CourseYearForPage>> {
        return this.getAllActiveYearsForCourseForPage(id, page);
    }
}