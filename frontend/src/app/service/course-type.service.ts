import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CourseType } from '../entity/course-type';
import { CourseTypeRequest } from '../entity/course-type-request';
import { Page } from '../entity/page';

@Injectable({
  providedIn: 'root'
})
export class CourseTypeService {
  private url = environment.apiServerUrl + "/types";

  constructor(private http: HttpClient) { }

  public getAllCourseTypes(): Observable<Page<CourseType>> {
    return this.http.get<Page<CourseType>>(`${this.url}`);
  }

  public getAllCourseTypesForPage(page: number): Observable<Page<CourseType>> {
    return this.http.get<Page<CourseType>>(`${this.url}?page=${page}`);
  }

  public addNewCourseType(type: CourseTypeRequest): Observable<CourseType> {
    return this.http.post<CourseType>(`${this.url}`, type);
  }

  public getCourseTypeById(id: number): Observable<CourseType> {
    return this.http.get<CourseType>(`${this.url}/${id}`);
  }

  public deleteCourseType(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/${id}`);
  }

  public editCourseType(id: number, type: CourseTypeRequest): Observable<CourseType> {
    return this.http.put<CourseType>(`${this.url}/${id}`, type);
  }

}
