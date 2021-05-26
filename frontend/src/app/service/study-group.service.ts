import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { StudyGroup } from '../entity/study-group';
import { StudyGroupRequest } from '../entity/study-group-request';
import { StudyGroupSummary } from '../entity/study-group-summary';
import { ServiceWithDelete } from './service-with-delete';

@Injectable({
  providedIn: 'root'
})
export class StudyGroupService implements ServiceWithDelete {
  private url = environment.apiServerUrl + "/groups";

  constructor(private http: HttpClient) { }

  public getStudyGroupById(id: number): Observable<StudyGroup> {
    return this.http.get<StudyGroup>(`${this.url}/${id}`);
  }

  public getStudyGroupByIdMin(id: number): Observable<StudyGroupSummary> {
    return this.http.get<StudyGroupSummary>(`${this.url}/${id}`);
  }

  public delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/${id}`);
  }

  public editStudyGroup(id: number, group: StudyGroupRequest): Observable<StudyGroup> {
    return this.http.put<StudyGroup>(`${this.url}/${id}`, group);
  }
}
