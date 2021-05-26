import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Page } from "../entity/page";
import { StudentWithUserId } from "../entity/student-with-user-id";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";
import { StudentService } from "./student.service";

@Injectable({
    providedIn: 'root'
})
export class CourseYearStudentsService extends StudentService
implements ServiceWithGetAllChildren<StudentWithUserId> {

    constructor(protected http: HttpClient) { 
        super(http);
    }

    public getAll(id: number): Observable<Page<StudentWithUserId>> {
        return this.getAllStudentsForCourseYear(id);
    }
    
    public getAllForPage(id: number, page: number): Observable<Page<StudentWithUserId>> {
        return this.getAllStudentsForCourseYearForPage(id, page);
    }
}