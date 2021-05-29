import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Page } from "../entity/page";
import { StudentWithUserId } from "../entity/student-with-user-id";
import { StudyGroupSummary } from "../entity/study-group-summary";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";
import { StudentService } from "./student.service";
import { StudyGroupService } from "./study-group.service";

@Injectable({
    providedIn: 'root'
})
export class StudyGroupStudentsAdapterService
implements ServiceWithGetAllChildren<StudentWithUserId, StudyGroupSummary> {

    constructor(protected http: HttpClient, private service: StudentService, 
        private parentService: StudyGroupService) {  }

    public getAllByParentId(id: number): Observable<Page<StudentWithUserId>> {
        return this.service.getAllStudentsForStudyGroup(id);
    }
    
    public getAllByParentIdForPage(id: number, page: number): Observable<Page<StudentWithUserId>> {
        return this.service.getAllStudentsForStudyGroupForPage(id, page);
    }

    public getParentById(id: number): Observable<StudyGroupSummary> {
        return this.parentService.getByIdMin(id);
    }
}