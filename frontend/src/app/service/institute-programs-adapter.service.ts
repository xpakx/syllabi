import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Institute } from "../entity/institute";
import { Page } from "../entity/page";
import { ProgramForPage } from "../entity/program-for-page";
import { InstituteService } from "./institute.service";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";

@Injectable({
    providedIn: 'root'
})
export class InstituteProgramsAdapterService
implements ServiceWithGetAllChildren<ProgramForPage, Institute> {

    constructor(protected http: HttpClient, private service: InstituteService) {  }

    public getAllByParentId(id: number): Observable<Page<ProgramForPage>> {
        return this.service.getAllPrograms(id);
    }
    
    public getAllByParentIdForPage(id: number, page: number): Observable<Page<ProgramForPage>> {
        return this.service.getAllProgramsForPage(id, page);
    }

    public getParentById(id: number): Observable<Institute> {
        return this.service.getById(id);
    }
}