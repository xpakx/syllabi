import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Course } from '../entity/course';
import { CourseResponse } from '../entity/course-response';
import { CourseDetails } from '../entity/course-details';
import { CourseForPage } from '../entity/course-for-page';
import { Page } from '../entity/page';
import { CourseEdit } from '../entity/course-edit';
import { CourseSummary } from '../entity/course-summary';
import { environment } from 'src/environments/environment';
import { CrudService } from './crud.service';

@Injectable({
  providedIn: 'root'
})
export class CourseService 
implements CrudService<CourseForPage, CourseDetails, Course, CourseEdit, CourseResponse> {
  private url = environment.apiServerUrl + "/courses";

  constructor(protected http: HttpClient) { }

  public addNew(course: Course): Observable<CourseResponse> {
    return this.http.post<CourseResponse>(`${this.url}`, course);
  }

  public getAll(): Observable<Page<CourseForPage>> {
    return this.http.get<Page<CourseForPage>>(`${this.url}`);
  }

  public getAllForPage(page: number): Observable<Page<CourseForPage>> {
    return this.http.get<Page<CourseForPage>>(`${this.url}?page=${page}`);
  }

  public getById(id: number): Observable<CourseDetails> {
    return this.http.get<CourseDetails>(`${this.url}/${id}`);
  }

  public getByIdMin(id: number): Observable<CourseSummary> {
    return this.http.get<CourseSummary>(`${this.url}/${id}/min`);
  }

  public edit(id: number, course: CourseEdit): Observable<CourseResponse> {
    return this.http.put<CourseResponse>(`${this.url}/${id}`, course);
  }

  public delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/${id}`);
  }
}
