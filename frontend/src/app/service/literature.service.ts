import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Literature } from '../entity/literature';
import { LiteratureForPage } from '../entity/literature-for-page';
import { LiteratureRequest } from '../entity/literature-request';
import { Page } from '../entity/page';
import { ServiceWithDelete } from './service-with-delete';

@Injectable({
  providedIn: 'root'
})
export class LiteratureService {
  protected url = environment.apiServerUrl;

  constructor(protected http: HttpClient) { }

  public getAllCourseLiterature(id: number): Observable<Page<LiteratureForPage>> {
    return this.http.get<Page<LiteratureForPage>>(`${this.url}/courses/${id}/literature`);
  }

  public getAllCourseLiteratureForPage(id: number, page: number): Observable<Page<LiteratureForPage>> {
    return this.http.get<Page<LiteratureForPage>>(`${this.url}/courses/${id}/literature?page=${page}`);
  }

  public getCourseLiteratureById(id: number): Observable<LiteratureForPage> {
    return this.http.get<LiteratureForPage>(`${this.url}/courses/literature/${id}`);
  }

  public addNewCourseLiterature(id: number, literature: LiteratureRequest): Observable<Literature> {
    return this.http.post<Literature>(`${this.url}/courses/${id}/literature`, literature);
  }

  public editCourseLiterature(id: number, literature: LiteratureRequest): Observable<Literature> {
    return this.http.put<Literature>(`${this.url}/courses/literature/${id}`, literature);
  }

  public getAllGroupLiterature(id: number): Observable<Page<LiteratureForPage>> {
    return this.http.get<Page<LiteratureForPage>>(`${this.url}/groups/${id}/literature`);
  }

  public getAllGroupLiteratureForPage(id: number, page: number): Observable<Page<LiteratureForPage>> {
    return this.http.get<Page<LiteratureForPage>>(`${this.url}/groups/${id}/literature?page=${page}`);
  }

  public deleteGroupLiterature(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/groups/literature/${id}`);
  }

  public getGroupLiteratureById(id: number): Observable<LiteratureForPage> {
    return this.http.get<LiteratureForPage>(`${this.url}/groups/literature/${id}`);
  }

  public addNewGroupLiterature(id: number, literature: LiteratureRequest): Observable<Literature> {
    return this.http.post<Literature>(`${this.url}/groups/${id}/literature`, literature);
  }

  public editGroupLiterature(id: number, literature: LiteratureRequest): Observable<Literature> {
    return this.http.put<Literature>(`${this.url}/groups/literature/${id}`, literature);
  }
}
