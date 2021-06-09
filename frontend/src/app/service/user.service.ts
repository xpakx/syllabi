import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ChangePasswordRequest } from '../entity/change-password-request';
import { CourseForPage } from '../entity/course-for-page';
import { Page } from '../entity/page';
import { RoleRequest } from '../entity/role-request';
import { User } from '../entity/user';
import { ServiceWithDelete } from './service-with-delete';
import { ServiceWithGetAll } from './service-with-get-all';
import { ServiceWithGetById } from './service-with-get-by-id';

@Injectable({
  providedIn: 'root'
})
export class UserService implements ServiceWithDelete, ServiceWithGetAll<User>, ServiceWithGetById<User> {
  private url = environment.apiServerUrl + "/users";

  constructor(private http: HttpClient) { }

  public addRole(userId: number, role: RoleRequest): Observable<User> {
    return this.http.post<User>(`${this.url}/${userId}/roles`, role);
  }
  
  public getAll(): Observable<Page<User>> {
    return this.http.get<Page<User>>(`${this.url}`);
  }

  public getAllForPage(page: number): Observable<Page<User>> {
    return this.http.get<Page<User>>(`${this.url}?page=${page}`);
  }

  public delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/${id}`);
  }

  public getById(id: number): Observable<User> {
    return this.http.get<User>(`${this.url}/${id}`);
  }

  public changePassword(id: string, request: ChangePasswordRequest): Observable<User> {
    return this.http.put<User>(`${this.url}/${id}`, request);
  }

  public getAllCoursesForUser(id: number): Observable<Page<CourseForPage>> {
    return this.http.get<Page<CourseForPage>>(`${this.url}/${id}/courses`);
  }

  public getAllCoursesForUserForPage(id: number, page: number): Observable<Page<CourseForPage>> {
    return this.http.get<Page<CourseForPage>>(`${this.url}/${id}/courses?page=${page}`);
  }
}
