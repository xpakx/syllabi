import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Page } from '../entity/page';
import { Student } from '../entity/student';
import { StudentCreateRequest } from '../entity/student-create-request';
import { StudentUpdateRequest } from '../entity/student-update-request';
import { StudentWithUserId } from '../entity/student-with-user-id';
import { ServiceWithDelete } from './service-with-delete';
import { ServiceWithGetById } from './service-with-get-by-id';

@Injectable({
  providedIn: 'root'
})
export class StudentService implements ServiceWithDelete, ServiceWithGetById<StudentUpdateRequest> {
  private url = environment.apiServerUrl;

  constructor(protected http: HttpClient) { }

  public addNew(id: number, student: StudentCreateRequest): Observable<Student> {
    return this.http.post<Student>(`${this.url}/users/${id}/student`, student);
  }

  public getById(id: number): Observable<StudentWithUserId> {
    return this.http.get<StudentWithUserId>(`${this.url}/users/${id}/student`);
  }

  public delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/users/${id}/student`);
  }

  public edit(id: number, student: StudentUpdateRequest): Observable<Student> {
    return this.http.put<Student>(`${this.url}/users/${id}/student`, student);
  }

  public getAllStudentsForStudyGroup(id: number): Observable<Page<StudentWithUserId>> {
    return this.http.get<Page<StudentWithUserId>>(`${this.url}/groups/${id}/students`);
  }

  public getAllStudentsForStudyGroupForPage(id: number, page: number): Observable<Page<StudentWithUserId>> {
    return this.http.get<Page<StudentWithUserId>>(`${this.url}/groups/${id}/students?page=${page}`);
  }

  public getAllStudentsForCourseYear(id: number): Observable<Page<StudentWithUserId>> {
    return this.http.get<Page<StudentWithUserId>>(`${this.url}/years/${id}/students`);
  }

  public getAllStudentsForCourseYearForPage(id: number, page: number): Observable<Page<StudentWithUserId>> {
    return this.http.get<Page<StudentWithUserId>>(`${this.url}/years/${id}/students?page=${page}`);
  }
}
