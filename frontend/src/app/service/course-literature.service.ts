import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { CourseSummary } from "../entity/course-summary";
import { Literature } from "../entity/literature";
import { LiteratureForPage } from "../entity/literature-for-page";
import { LiteratureRequest } from "../entity/literature-request";
import { Page } from "../entity/page";
import { CrudWithParentService } from "./crud-with-parent.service";

@Injectable({
    providedIn: 'root'
})
export class CourseLiteratureService
implements CrudWithParentService<LiteratureForPage, Literature, LiteratureRequest, LiteratureRequest, Literature, CourseSummary> {
    private url = environment.apiServerUrl;

    constructor(protected http: HttpClient) { }

    public delete(id: number): Observable<any> {
        return this.http.delete<any>(`${this.url}/courses/literature/${id}`);
    }

    public getAllByParentId(id: number): Observable<Page<LiteratureForPage>> {
        return this.http.get<Page<LiteratureForPage>>(`${this.url}/courses/${id}/literature`);
    }

    public getAllByParentIdForPage(id: number, page: number): Observable<Page<LiteratureForPage>> {
        return this.http.get<Page<LiteratureForPage>>(`${this.url}/courses/${id}/literature?page=${page}`);
    }

    public getById(id: number): Observable<LiteratureForPage> {
        return this.http.get<LiteratureForPage>(`${this.url}/courses/literature/${id}`);
    }

    public addNew(id: number, literature: LiteratureRequest): Observable<Literature> {
        return this.http.post<Literature>(`${this.url}/courses/${id}/literature`, literature);
    }

    public edit(id: number, literature: LiteratureRequest): Observable<Literature> {
        return this.http.put<Literature>(`${this.url}/courses/literature/${id}`, literature);
    }

    public getParentById(id: number): Observable<CourseSummary> {
        return this.http.get<CourseSummary>(`${this.url}/courses/${id}/min`);
    }
}