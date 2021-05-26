import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Course } from '../entity/course';
import { CourseResponse } from '../entity/course-response';
import { CourseDetails } from '../entity/course-details';
import { CourseForPage } from '../entity/course-for-page';
import { Page } from '../entity/page';
import { CourseYearForPage } from '../entity/course-year-for-page';
import { CourseEdit } from '../entity/course-edit';
import { CourseYear } from '../entity/course-year';
import { CourseYearRequest } from '../entity/course-year-request';
import { CourseSummary } from '../entity/course-summary';
import { environment } from 'src/environments/environment';
import { ServiceWithDelete } from './service-with-delete';
import { ServiceWithGetAll } from './service-with-get-all';

@Injectable({
  providedIn: 'root'
})
export class CourseService implements ServiceWithDelete, ServiceWithGetAll<CourseForPage> {
  private url = environment.apiServerUrl + "/courses";

  constructor(private http: HttpClient) { }

  public addNewCourse(course: Course): Observable<CourseResponse> {
    return this.http.post<CourseResponse>(`${this.url}`, course);
  }

  public getAll(): Observable<Page<CourseForPage>> {
    return this.http.get<Page<CourseForPage>>(`${this.url}`);
  }

  public getAllForPage(page: number): Observable<Page<CourseForPage>> {
    return this.http.get<Page<CourseForPage>>(`${this.url}?page=${page}`);
  }

  public getCourseById(id: number): Observable<CourseDetails> {
    return this.http.get<CourseDetails>(`${this.url}/${id}`);
  }

  public getCourseByIdMin(id: number): Observable<CourseSummary> {
    return this.http.get<CourseSummary>(`${this.url}/${id}/min`);
  }

  public editCourse(id: number, course: CourseEdit): Observable<CourseResponse> {
    return this.http.put<CourseResponse>(`${this.url}/${id}`, course);
  }

  public delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/${id}`);
  }

  public getAllYearsForCourse(id: number): Observable<Page<CourseYearForPage>> {
    return this.http.get<Page<CourseYearForPage>>(`${this.url}/${id}/years`);
  }

  public getAllYearsForCourseForPage(id: number, page: number): Observable<Page<CourseYearForPage>> {
    return this.http.get<Page<CourseYearForPage>>(`${this.url}/${id}/years?page=${page}`);
  }

  public getAllActiveYearsForCourse(id: number): Observable<Page<CourseYearForPage>> {
    return this.http.get<Page<CourseYearForPage>>(`${this.url}/${id}/years/active`);
  }

  public getAllActiveYearsForCourseForPage(id: number, page: number): Observable<Page<CourseYearForPage>> {
    return this.http.get<Page<CourseYearForPage>>(`${this.url}/${id}/years/active?page=${page}`);
  }

  public addNewCourseYear(id: number, year: CourseYearRequest): Observable<CourseYear> {
    return this.http.post<CourseYear>(`${this.url}/${id}/years`, year);
  }
}
