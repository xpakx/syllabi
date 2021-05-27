import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CourseTypeRequest } from '../entity/course-type-request';
import { CourseYear } from '../entity/course-year';
import { CourseYearDetails } from '../entity/course-year-details';
import { CourseYearForPage } from '../entity/course-year-for-page';
import { CourseYearRequest } from '../entity/course-year-request';
import { Page } from '../entity/page';
import { StudyGroup } from '../entity/study-group';
import { StudyGroupForPage } from '../entity/study-group-for-page';
import { StudyGroupRequest } from '../entity/study-group-request';
import { CrudWithParentService } from './crud-with-parent.service';
import { ServiceWithDelete } from './service-with-delete';

@Injectable({
  providedIn: 'root'
})
export class CourseYearService 
implements CrudWithParentService<CourseYearForPage, CourseYearDetails, CourseYearRequest, CourseYearRequest, CourseYear> { 
  private url = environment.apiServerUrl + "/years";
  private urlCourses = environment.apiServerUrl + "/courses";

  constructor(protected http: HttpClient) { }

  public addNew(courseId: number, year: CourseYearRequest): Observable<CourseYear> {
    return this.http.post<CourseYear>(`${this.urlCourses}/${courseId}/years`, year);
  }

  public getAllByParentId(id: number): Observable<Page<CourseYearForPage>> {
    return this.http.get<Page<CourseYearForPage>>(`${this.urlCourses}/${id}/years`);
  }

  public getAllByParentIdForPage(id: number, page: number): Observable<Page<CourseYearForPage>> {
    return this.http.get<Page<CourseYearForPage>>(`${this.urlCourses}/${id}/years?page=${page}`);
  }

  public getAllActiveByParentId(id: number): Observable<Page<CourseYearForPage>> {
    return this.http.get<Page<CourseYearForPage>>(`${this.urlCourses}/${id}/years/active`);
  }

  public getAllActiveByParentIdForPage(id: number, page: number): Observable<Page<CourseYearForPage>> {
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
