import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Page } from '../entity/page';
import { SemesterSummary } from '../entity/semester-summary';
import { Semester } from '../entity/semester';
import { CrudWithParentService } from './crud-with-parent.service';
import { SemesterRequest } from '../entity/semester-request';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { ProgramSummary } from '../entity/program-summary';
import { CourseForPage } from '../entity/course-for-page';

@Injectable({
  providedIn: 'root'
})
export class SemesterService implements CrudWithParentService<Semester, SemesterSummary, SemesterRequest, SemesterRequest, Semester, ProgramSummary> {
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

  getParentById(id: number): Observable<ProgramSummary> {
    return this.http.get<ProgramSummary>(`${this.programUrl}/${id}`);
  }

  getAllCourses(id: number): Observable<Page<CourseForPage>> {
    return this.http.get<Page<CourseForPage>>(`${this.url}/${id}/courses`);
  }

  getAllCoursesForPage(id: number, page: number): Observable<Page<CourseForPage>> {
    return this.http.get<Page<CourseForPage>>(`${this.programUrl}/${id}/courses?page=${page}`);
  }
}
