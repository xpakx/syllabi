import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { CourseForPage } from "../entity/course-for-page";
import { Page } from "../entity/page";
import { StudentWithUserId } from "../entity/student-with-user-id";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";
import { StudentService } from "./student.service";
import { UserService } from "./user.service";

@Injectable({
    providedIn: 'root'
})
export class UserCoursesAdapterService
implements ServiceWithGetAllChildren<CourseForPage, StudentWithUserId> {

    constructor(protected http: HttpClient, private service: UserService, 
        private parentService: StudentService) {  }

    public getAllByParentId(id: number): Observable<Page<CourseForPage>> {
        return this.service.getAllCoursesForUser(id);
    }
    
    public getAllByParentIdForPage(id: number, page: number): Observable<Page<CourseForPage>> {
        return this.service.getAllCoursesForUserForPage(id, page);
    }

    public getParentById(id: number): Observable<StudentWithUserId> {
        return this.parentService.getById(id);
    }
}