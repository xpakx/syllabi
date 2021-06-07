import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Admission } from "../entity/admission";
import { Page } from "../entity/page";
import { Program } from "../entity/program";
import { AdmissionService } from "./admission.service";
import { ProgramService } from "./program.service";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";

@Injectable({
    providedIn: 'root'
})
export class ProgramAdmissionsAdapterService
implements ServiceWithGetAllChildren<Admission, Program> {

    constructor(protected http: HttpClient, private service: ProgramService,
        private admissionService: AdmissionService) {  }

    public getAllByParentId(id: number): Observable<Page<Admission>> {
        return this.admissionService.getAllForProgram(id);
    }
    
    public getAllByParentIdForPage(id: number, page: number): Observable<Page<Admission>> {
        return this.admissionService.getAllForProgramForPage(id, page);
    }

    public getParentById(id: number): Observable<Program> {
        return this.service.getById(id);
    }

    public delete(id: number): Observable<any> {
        return this.admissionService.delete(id);
    }
}