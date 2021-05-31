import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CourseType } from '../entity/course-type';
import { CourseTypeRequest } from '../entity/course-type-request';
import { Page } from '../entity/page';
import { CrudService } from './crud.service';

@Injectable({
  providedIn: 'root'
})
export class CourseTypeService 
implements CrudService<CourseType, CourseType, CourseTypeRequest, CourseTypeRequest, CourseType> {
  private url = environment.apiServerUrl + "/types";

  constructor(private http: HttpClient) { }

  public addNew(type: CourseTypeRequest): Observable<CourseType> {
    return this.http.post<CourseType>(`${this.url}`, type);
  }

  public getAll(): Observable<Page<CourseType>> {
    return this.http.get<Page<CourseType>>(`${this.url}`);
  }

  public getAllForPage(page: number): Observable<Page<CourseType>> {
    return this.http.get<Page<CourseType>>(`${this.url}?page=${page}`);
  }

  public getById(id: number): Observable<CourseType> {
    return this.http.get<CourseType>(`${this.url}/${id}`);
  }

  public edit(id: number, type: CourseTypeRequest): Observable<CourseType> {
    return this.http.put<CourseType>(`${this.url}/${id}`, type);
  }

  public delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/${id}`);
  }
}
