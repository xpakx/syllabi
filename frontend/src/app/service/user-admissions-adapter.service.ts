import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Admission } from "../entity/admission";
import { AdmissionForm } from "../entity/admission-form";
import { Page } from "../entity/page";
import { Program } from "../entity/program";
import { User } from "../entity/user";
import { AdmissionFormService } from "./admission-form.service";
import { AdmissionService } from "./admission.service";
import { ProgramService } from "./program.service";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";
import { UserService } from "./user.service";

@Injectable({
    providedIn: 'root'
})
export class UserAdmissionsAdapterService
implements ServiceWithGetAllChildren<AdmissionForm, User> {

    constructor(protected http: HttpClient, private service: UserService,
        private admissionService: AdmissionFormService) {  }

    public getAllByParentId(id: number): Observable<Page<AdmissionForm>> {
        return this.admissionService.getAllForUser(id);
    }
    
    public getAllByParentIdForPage(id: number, page: number): Observable<Page<AdmissionForm>> {
        return this.admissionService.getAllForUserForPage(id, page);
    }

    public getParentById(id: number): Observable<User> {
        return this.service.getById(id);
    }

    public delete(id: number): Observable<any> {
        return this.admissionService.delete(id);
    }
}