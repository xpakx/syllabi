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

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  private apiServerUrl = 'http://192.168.1.204:8080';

  constructor(private http: HttpClient) { }

  public addNewCourse(course: Course): Observable<CourseResponse> {
    return this.http.post<CourseResponse>(`${this.apiServerUrl}/courses`, course);
  }

  public getAllCourses(): Observable<Page<CourseForPage>> {
    return this.http.get<Page<CourseForPage>>(`${this.apiServerUrl}/courses/all`);
  }

  public getAllCoursesForPage(page: number): Observable<Page<CourseForPage>> {
    return this.http.get<Page<CourseForPage>>(`${this.apiServerUrl}/courses/all?page=${page}`);
  }

  public getCourseById(id: number): Observable<CourseDetails> {
    return this.http.get<CourseDetails>(`${this.apiServerUrl}/courses/${id}`);
  }

  public getCourseByIdMin(id: number): Observable<CourseSummary> {
    return this.http.get<CourseSummary>(`${this.apiServerUrl}/courses/${id}/min`);
  }

  public editCourse(id: number, course: CourseEdit): Observable<CourseResponse> {
    return this.http.put<CourseResponse>(`${this.apiServerUrl}/courses/${id}`, course);
  }

  public deleteCourse(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiServerUrl}/courses/${id}`);
  }

  public getAllYearsForCourse(id: number): Observable<Page<CourseYearForPage>> {
    return this.http.get<Page<CourseYearForPage>>(`${this.apiServerUrl}/courses/${id}/years`);
  }

  public getAllYearsForCourseForPage(id: number, page: number): Observable<Page<CourseYearForPage>> {
    return this.http.get<Page<CourseYearForPage>>(`${this.apiServerUrl}/courses/${id}/years?page=${page}`);
  }

  public getAllActiveYearsForCourse(id: number): Observable<Page<CourseYearForPage>> {
    return this.http.get<Page<CourseYearForPage>>(`${this.apiServerUrl}/courses/${id}/years/active`);
  }

  public getAllActiveYearsForCourseForPage(id: number, page: number): Observable<Page<CourseYearForPage>> {
    return this.http.get<Page<CourseYearForPage>>(`${this.apiServerUrl}/courses/${id}/years/active?page=${page}`);
  }

  public addNewCourseYear(id: number, year: CourseYearRequest): Observable<CourseYear> {
    return this.http.post<CourseYear>(`${this.apiServerUrl}/courses/${id}/years`, year);
  }
  

  

}
