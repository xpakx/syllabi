import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { Literature } from "../entity/literature";
import { LiteratureForPage } from "../entity/literature-for-page";
import { LiteratureRequest } from "../entity/literature-request";
import { Page } from "../entity/page";
import { ServiceWithDelete } from "./service-with-delete";
import { ServiceWithGetAllChildren } from "./service-with-get-all-children";

@Injectable({
    providedIn: 'root'
})
export class GroupLiteratureService
implements ServiceWithDelete, ServiceWithGetAllChildren<LiteratureForPage>  {
    private url = environment.apiServerUrl;

    constructor(protected http: HttpClient) { }

    public getAllByParentId(id: number): Observable<Page<LiteratureForPage>> {
        return this.http.get<Page<LiteratureForPage>>(`${this.url}/groups/${id}/literature`);
    }

    public getAllByParentIdForPage(id: number, page: number): Observable<Page<LiteratureForPage>> {
        return this.http.get<Page<LiteratureForPage>>(`${this.url}/groups/${id}/literature?page=${page}`);
    }

    public delete(id: number): Observable<any> {
      return this.http.delete<any>(`${this.url}/groups/literature/${id}`);
    }

    public getById(id: number): Observable<LiteratureForPage> {
        return this.http.get<LiteratureForPage>(`${this.url}/groups/literature/${id}`);
    }

    public addNew(id: number, literature: LiteratureRequest): Observable<Literature> {
        return this.http.post<Literature>(`${this.url}/groups/${id}/literature`, literature);
    }

    public edit(id: number, literature: LiteratureRequest): Observable<Literature> {
        return this.http.put<Literature>(`${this.url}/groups/literature/${id}`, literature);
    }
}