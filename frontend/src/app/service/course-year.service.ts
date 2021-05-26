import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { CourseYear } from '../entity/course-year';
import { CourseYearDetails } from '../entity/course-year-details';
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
  private uri = environment.apiServerUrl + "/years";

  constructor(private http: HttpClient) { }

  public getCourseYearById(id: number): Observable<CourseYearDetails> {
    return this.http.get<CourseYearDetails>(`${this.uri}/${id}`);
  }

  public delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.uri}/${id}`);
  }

  public editCourseYear(id: number, year: CourseYearRequest): Observable<CourseYear> {
    return this.http.put<CourseYear>(`${this.uri}/${id}`, year);
  }

  public getAllGroupsForYear(id: number): Observable<Page<StudyGroupForPage>> {
    return this.http.get<Page<StudyGroupForPage>>(`${this.uri}/${id}/groups`);
  }

  public getAllGroupsForYearForPage(id: number, page: number): Observable<Page<StudyGroupForPage>> {
    return this.http.get<Page<StudyGroupForPage>>(`${this.uri}/${id}/groups?page=${page}`);
  }

  public addNewStudyGroup(id: number, group: StudyGroupRequest): Observable<StudyGroup> {
    return this.http.post<StudyGroup>(`${this.uri}/${id}/groups`, group);
  }
}
