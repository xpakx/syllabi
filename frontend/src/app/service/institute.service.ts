import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CourseForPage } from '../entity/course-for-page';
import { Institute } from '../entity/institute';
import { InstituteForPage } from '../entity/institute-for-page';
import { InstituteRequest } from '../entity/institute-request';
import { Page } from '../entity/page';
import { ProgramSummary } from '../entity/program-summary';
import { CrudService } from './crud.service';

@Injectable({
  providedIn: 'root'
})
export class InstituteService 
implements CrudService<InstituteForPage, Institute, InstituteRequest, InstituteRequest, Institute> {
  private url = environment.apiServerUrl + "/institutes";

  constructor(private http: HttpClient) { }

  public addNew(institute: InstituteRequest): Observable<Institute> {
    return this.http.post<Institute>(`${this.url}`, institute);
  }

  public getAll(): Observable<Page<InstituteForPage>> {
    return this.http.get<Page<InstituteForPage>>(`${this.url}`);
  }

  public getAllForPage(page: number): Observable<Page<InstituteForPage>> {
    return this.http.get<Page<InstituteForPage>>(`${this.url}?page=${page}`);
  }

  public getById(id: number): Observable<Institute> {
    return this.http.get<Institute>(`${this.url}/${id}`);
  }

  public edit(id: number, institute: InstituteRequest): Observable<Institute> {
    return this.http.put<Institute>(`${this.url}/${id}`, institute);
  }

  public delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/${id}`);
  }

  public getAllCourses(id: number): Observable<Page<CourseForPage>> {
    return this.http.get<Page<CourseForPage>>(`${this.url}/${id}/courses`);
  }

  public getAllCoursesForPage(id: number, page: number): Observable<Page<CourseForPage>> {
    return this.http.get<Page<CourseForPage>>(`${this.url}/${id}/courses?page=${page}`);
  }

  public getAllPrograms(id: number): Observable<Page<ProgramSummary>> {
    return this.http.get<Page<ProgramSummary>>(`${this.url}/${id}/programs`);
  }

  public getAllProgramsForPage(id: number, page: number): Observable<Page<ProgramSummary>> {
    return this.http.get<Page<ProgramSummary>>(`${this.url}/${id}/programs?page=${page}`);
  }

  public getAllChildren(id: number): Observable<Page<InstituteForPage>> {
    return this.http.get<Page<ProgramSummary>>(`${this.url}/${id}/children`);
  }

  public getAllChildrenForPage(id: number, page: number): Observable<Page<InstituteForPage>> {
    return this.http.get<Page<ProgramSummary>>(`${this.url}/${id}/children?page=${page}`);
  }
}
