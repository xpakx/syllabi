import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../entity/page';
import { SemesterSummary } from '../entity/semester-summary';
import { Semester } from '../entity/semester';
import { CrudWithParentService } from './crud-with-parent.service';
import { SemesterRequest } from '../entity/semester-request';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SemesterService implements CrudWithParentService<Semester, SemesterSummary, SemesterRequest, SemesterRequest, Semester> {
  private programUrl = environment.apiServerUrl + "/programs";
  private url = environment.apiServerUrl + "/semesters";

  constructor(private http: HttpClient) { }

  getById(id: number): Observable<SemesterSummary> {
    return this.http.get<SemesterSummary>(`${this.url}/${id}`);
  }

  addNew(parentId: number, semester: SemesterRequest): Observable<Semester> {
    return this.http.post<Semester>(`${this.programUrl}/${parentId}/semesters`, semester);
  }

  edit(id: number, semester: SemesterRequest): Observable<Semester> {
    return this.http.put<Semester>(`${this.url}/${id}`, semester);
  }

  getAllByParentId(id: number): Observable<Page<Semester>> {
    return this.http.get<Page<Semester>>(`${this.programUrl}/${id}/semesters`);
  }

  getAllByParentIdForPage(id: number, page: number): Observable<Page<Semester>> {
    return this.http.get<Page<Semester>>(`${this.programUrl}/${id}/semesters?page=${page}`);
  }

  delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/${id}`);
  }
}
