import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { CourseYearDetails } from "../entity/course-year-details";
import { Page } from "../entity/page";
import { StudentWithUserId } from "../entity/student-with-user-id";
import { CourseYearService } from "./course-year.service";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";
import { StudentService } from "./student.service";

@Injectable({
    providedIn: 'root'
})
export class CourseYearStudentsAdapterService
implements ServiceWithGetAllChildren<StudentWithUserId> {

    constructor(protected http: HttpClient, private service: StudentService, 
        private parentService: CourseYearService) {  }

    public getAllByParentId(id: number): Observable<Page<StudentWithUserId>> {
        return this.service.getAllStudentsForCourseYear(id);
    }
    
    public getAllByParentIdForPage(id: number, page: number): Observable<Page<StudentWithUserId>> {
        return this.service.getAllStudentsForCourseYearForPage(id, page);
    }

    public getParentById(id: number): Observable<CourseYearDetails> {
        return this.parentService.getById(id);
    }
}