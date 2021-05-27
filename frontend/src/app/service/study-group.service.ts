import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Page } from '../entity/page';
import { StudyGroup } from '../entity/study-group';
import { StudyGroupForPage } from '../entity/study-group-for-page';
import { StudyGroupRequest } from '../entity/study-group-request';
import { StudyGroupSummary } from '../entity/study-group-summary';
import { ServiceWithDelete } from './service-with-delete';

@Injectable({
  providedIn: 'root'
})
export class StudyGroupService implements ServiceWithDelete {
  private url = environment.apiServerUrl + "/groups";
  private parentUrl = environment.apiServerUrl + "/years";

  constructor(protected http: HttpClient) { }

  public addNew(yearId: number, group: StudyGroupRequest): Observable<StudyGroup> {
    return this.http.post<StudyGroup>(`${this.parentUrl}/${yearId}/groups`, group);
  }

  public getAll(yearId: number): Observable<Page<StudyGroupForPage>> {
    return this.http.get<Page<StudyGroupForPage>>(`${this.parentUrl}/${yearId}/groups`);
  }

  public getAllForPage(yearId: number, page: number): Observable<Page<StudyGroupForPage>> {
    return this.http.get<Page<StudyGroupForPage>>(`${this.parentUrl}/${yearId}/groups?page=${page}`);
  }

  public getById(id: number): Observable<StudyGroup> {
    return this.http.get<StudyGroup>(`${this.url}/${id}`);
  }

  public getByIdMin(id: number): Observable<StudyGroupSummary> {
    return this.http.get<StudyGroupSummary>(`${this.url}/${id}`);
  }

  public delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/${id}`);
  }

  public edit(id: number, group: StudyGroupRequest): Observable<StudyGroup> {
    return this.http.put<StudyGroup>(`${this.url}/${id}`, group);
  }
}
