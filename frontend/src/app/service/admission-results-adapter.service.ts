import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Admission } from "../entity/admission";
import { AdmissionDetails } from "../entity/admission-details";
import { AdmissionForm } from "../entity/admission-form";
import { CloseAdmissionRequest } from "../entity/close-admission-request";
import { Page } from "../entity/page";
import { AdmissionFormService } from "./admission-form.service";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";

@Injectable({
    providedIn: 'root'
})
export class AdmissionResultsAdapterService
implements ServiceWithGetAllChildren<AdmissionForm, AdmissionDetails> {

    constructor(protected http: HttpClient, private service: AdmissionFormService) {  }

    public getAllByParentId(id: number): Observable<Page<AdmissionForm>> {
        return this.service.getAllResultsByParentId(id);
    }
    
    public getAllByParentIdForPage(id: number, page: number): Observable<Page<AdmissionForm>> {
        return this.service.getAllResultsByParentIdForPage(id, page);
    }

    public getParentById(id: number): Observable<AdmissionDetails> {
        return this.service.getParentById(id);
    }

    public delete(id: number): Observable<any> {
        return this.service.delete(id);
    }

    public close(id: number, students: CloseAdmissionRequest): Observable<Admission> {
        return this.service.close(id, students);
    }
}