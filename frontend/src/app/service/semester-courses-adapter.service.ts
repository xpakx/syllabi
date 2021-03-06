import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { CourseForPage } from "../entity/course-for-page";
import { Page } from "../entity/page";
import { SemesterSummary } from "../entity/semester-summary";
import { CourseService } from "./course.service";
import { SemesterService } from "./semester.service";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";

@Injectable({
    providedIn: 'root'
})
export class SemesterCoursesAdapterService
implements ServiceWithGetAllChildren<CourseForPage, SemesterSummary> {

    constructor(protected http: HttpClient, private service: SemesterService,
        private courseService: CourseService) {  }

    public getAllByParentId(id: number): Observable<Page<CourseForPage>> {
        return this.service.getAllCourses(id);
    }
    
    public getAllByParentIdForPage(id: number, page: number): Observable<Page<CourseForPage>> {
        return this.service.getAllCoursesForPage(id, page);
    }

    public getParentById(id: number): Observable<SemesterSummary> {
        return this.service.getById(id);
    }

    public delete(id: number): Observable<any> {
        return this.courseService.delete(id);
    }
}