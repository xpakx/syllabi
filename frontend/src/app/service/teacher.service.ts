import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Job } from '../entity/job';
import { JobRequest } from '../entity/job-request';
import { Page } from '../entity/page';
import { Teacher } from '../entity/teacher';
import { TeacherCreateRequest } from '../entity/teacher-create-request';
import { TeacherSummary } from '../entity/teacher-summary';
import { TeacherUpdateRequest } from '../entity/teacher-update-request';
import { ServiceWithDelete } from './service-with-delete';
import { ServiceWithGetAll } from './service-with-get-all';
import { ServiceWithGetById } from './service-with-get-by-id';

@Injectable({
  providedIn: 'root'
})
export class TeacherService implements ServiceWithDelete, ServiceWithGetAll<TeacherSummary>, ServiceWithGetById<Teacher> {
  private url = environment.apiServerUrl;

  constructor(private http: HttpClient) { }

  public getAll(): Observable<Page<TeacherSummary>> {
    return this.http.get<Page<TeacherSummary>>(`${this.url}/teachers`);
  }

  public getAllForPage(page: number): Observable<Page<TeacherSummary>> {
    return this.http.get<Page<TeacherSummary>>(`${this.url}/teachers?page=${page}`);
  }

  public addNewTeacher(id: number, teacher: TeacherCreateRequest): Observable<Teacher> {
    return this.http.post<Teacher>(`${this.url}/users/${id}/teacher`, teacher);
  }

  public getById(id: number): Observable<Teacher> {
    return this.http.get<Teacher>(`${this.url}/users/${id}/teacher`);
  }

  public delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/users/${id}/teacher`);
  }

  public editTeacher(id: number, teacher: TeacherUpdateRequest): Observable<Teacher> {
    return this.http.put<Teacher>(`${this.url}/users/${id}/teacher`, teacher);
  }

  public editTeacherJob(id: number, job: JobRequest): Observable<Job> {
    return this.http.put<Job>(`${this.url}/users/${id}/teacher/job`, job);
  }

  public getTeacherJob(id: number): Observable<Job> {
    return this.http.get<Job>(`${this.url}/users/${id}/teacher/job`);
  }
}
