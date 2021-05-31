import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { CourseForPage } from "../entity/course-for-page";
import { Page } from "../entity/page";
import { Program } from "../entity/program";
import { CourseService } from "./course.service";
import { ProgramService } from "./program.service";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";

@Injectable({
    providedIn: 'root'
})
export class ProgramCoursesAdapterService
implements ServiceWithGetAllChildren<CourseForPage, Program> {

    constructor(protected http: HttpClient, private service: ProgramService,
        private courseService: CourseService) {  }

    public getAllByParentId(id: number): Observable<Page<CourseForPage>> {
        return this.service.getAllCoursesForProgram(id);
    }
    
    public getAllByParentIdForPage(id: number, page: number): Observable<Page<CourseForPage>> {
        return this.service.getAllCoursesForProgramForPage(id, page);
    }

    public getParentById(id: number): Observable<Program> {
        return this.service.getById(id);
    }

    public delete(id: number): Observable<any> {
        return this.courseService.delete(id);
    }
}