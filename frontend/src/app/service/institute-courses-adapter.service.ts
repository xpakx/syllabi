import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { CourseForPage } from "../entity/course-for-page";
import { Institute } from "../entity/institute";
import { Page } from "../entity/page";
import { CourseService } from "./course.service";
import { InstituteService } from "./institute.service";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";

@Injectable({
    providedIn: 'root'
})
export class InstituteCoursesAdapterService
implements ServiceWithGetAllChildren<CourseForPage, Institute> {

    constructor(protected http: HttpClient, private service: InstituteService,
        private courseService: CourseService) {  }

    public getAllByParentId(id: number): Observable<Page<CourseForPage>> {
        return this.service.getAllCourses(id);
    }
    
    public getAllByParentIdForPage(id: number, page: number): Observable<Page<CourseForPage>> {
        return this.service.getAllCoursesForPage(id, page);
    }

    public getParentById(id: number): Observable<Institute> {
        return this.service.getById(id);
    }

    public delete(id: number): Observable<any> {
        return this.courseService.delete(id);
    }
}