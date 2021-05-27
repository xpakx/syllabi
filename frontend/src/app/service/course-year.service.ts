import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CourseYear } from '../entity/course-year';
import { CourseYearDetails } from '../entity/course-year-details';
import { CourseYearForPage } from '../entity/course-year-for-page';
import { CourseYearRequest } from '../entity/course-year-request';
import { Page } from '../entity/page';
import { StudyGroup } from '../entity/study-group';
import { StudyGroupForPage } from '../entity/study-group-for-page';
import { StudyGroupRequest } from '../entity/study-group-request';
import { ServiceWithDelete } from './service-with-delete';

@Injectable({
  providedIn: 'root'
})
export class CourseYearService implements ServiceWithDelete {
  private url = environment.apiServerUrl + "/years";
  private urlCourses = environment.apiServerUrl + "/courses";

  constructor(protected http: HttpClient) { }

  public addNew(courseId: number, year: CourseYearRequest): Observable<CourseYear> {
    return this.http.post<CourseYear>(`${this.urlCourses}/${courseId}/years`, year);
  }

  public getAllYearsForCourse(id: number): Observable<Page<CourseYearForPage>> {
    return this.http.get<Page<CourseYearForPage>>(`${this.urlCourses}/${id}/years`);
  }

  public getAllYearsForCourseForPage(id: number, page: number): Observable<Page<CourseYearForPage>> {
    return this.http.get<Page<CourseYearForPage>>(`${this.urlCourses}/${id}/years?page=${page}`);
  }

  public getAllActiveYearsForCourse(id: number): Observable<Page<CourseYearForPage>> {
    return this.http.get<Page<CourseYearForPage>>(`${this.urlCourses}/${id}/years/active`);
  }

  public getAllActiveYearsForCourseForPage(id: number, page: number): Observable<Page<CourseYearForPage>> {
    return this.http.get<Page<CourseYearForPage>>(`${this.urlCourses}/${id}/years/active?page=${page}`);
  }

  public getById(id: number): Observable<CourseYearDetails> {
    return this.http.get<CourseYearDetails>(`${this.url}/${id}`);
  }

  public edit(id: number, year: CourseYearRequest): Observable<CourseYear> {
    return this.http.put<CourseYear>(`${this.url}/${id}`, year);
  }

  public delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/${id}`);
  }
}
