import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CourseForPage } from '../entity/course-for-page';
import { Page } from '../entity/page';
import { Program } from '../entity/program';
import { ProgramForPage } from '../entity/program-for-page';
import { ProgramRequest } from '../entity/program-request';
import { ServiceWithDelete } from './service-with-delete';

@Injectable({
  providedIn: 'root'
})
export class ProgramService implements ServiceWithDelete {
  private url = environment.apiServerUrl + "/programs";

  constructor(private http: HttpClient) { }

  public getAllPrograms(): Observable<Page<ProgramForPage>> {
    return this.http.get<Page<ProgramForPage>>(`${this.url}`);
  }

  public getAllProgramsForPage(page: number): Observable<Page<ProgramForPage>> {
    return this.http.get<Page<ProgramForPage>>(`${this.url}?page=${page}`);
  }

  public addNewProgram(program: ProgramRequest): Observable<Program> {
    return this.http.post<Program>(`${this.url}`, program);
  }

  public delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/${id}`);
  }

  public getProgramById(id: number): Observable<Program> {
    return this.http.get<Program>(`${this.url}/${id}`);
  }

  public editProgram(id: number, program: ProgramRequest): Observable<Program> {
    return this.http.put<Program>(`${this.url}/${id}`, program);
  }

  public getAllCoursesForProgram(id: number): Observable<Page<CourseForPage>> {
    return this.http.get<Page<CourseForPage>>(`${this.url}/${id}/courses`);
  }

  public getAllCoursesForProgramForPage(id: number, page: number): Observable<Page<CourseForPage>> {
    return this.http.get<Page<CourseForPage>>(`${this.url}/${id}/courses?page=${page}`);
  }


}
