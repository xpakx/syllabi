import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AdmissionDetails } from '../entity/admission-details';
import { AdmissionForm } from '../entity/admission-form';
import { AdmissionFormDetails } from '../entity/admission-form-details';
import { AdmissionFormRequest } from '../entity/admission-form-request';
import { Page } from '../entity/page';
import { ServiceWithGetAllChildren } from './service-with-get-all-children';
import { ServiceWithGetById } from './service-with-get-by-id';
import { AdmissionFormReviewRequest } from '../entity/admission-form-review-request';
import { CloseAdmissionRequest } from '../entity/close-admission-request';
import { Admission } from '../entity/admission';
import { StudentProgram } from '../entity/student-program';
import { StudentProgramRequest } from '../entity/student-program-request';

@Injectable({
  providedIn: 'root'
})
export class AdmissionFormService implements ServiceWithGetAllChildren<AdmissionForm, AdmissionDetails>,
ServiceWithGetById<AdmissionFormDetails> {
  private url = environment.apiServerUrl;

  constructor(private http: HttpClient) { }

  getById(id: number): Observable<AdmissionFormDetails> {
    return this.http.get<AdmissionFormDetails>(`${this.url}/students/admissions/${id}`);
  }

  getAllByParentId(id: number): Observable<Page<AdmissionForm>> {
    return this.http.get<Page<AdmissionForm>>(`${this.url}/admissions/${id}/forms`);
  }

  getAllByParentIdForPage(id: number, page: number): Observable<Page<AdmissionForm>> {
    return this.http.get<Page<AdmissionForm>>(`${this.url}/admissions/${id}/forms?page=${page}`);
  }

  getAllVerifiedByParentId(id: number): Observable<Page<AdmissionForm>> {
    return this.http.get<Page<AdmissionForm>>(`${this.url}/admissions/${id}/forms/veridied`);
  }

  getAllVerifiedByParentIdForPage(id: number, page: number): Observable<Page<AdmissionForm>> {
    return this.http.get<Page<AdmissionForm>>(`${this.url}/admissions/${id}/forms/verified?page=${page}`);
  }

  getParentById(id: number): Observable<AdmissionDetails> {
    return this.http.get<AdmissionDetails>(`${this.url}/admissions/${id}`);
  }

  delete(id: number): Observable<any> {
    throw new Error('Method not implemented.');
  }

  addNew(admissionId: number, form: AdmissionFormRequest): Observable<AdmissionForm> {
    let userId = localStorage.getItem("user_id")
    return this.http.post<AdmissionForm>(`${this.url}/admissions/${admissionId}/apply/${userId}`, form);
  }

  review(id: number, form: AdmissionFormReviewRequest): Observable<AdmissionFormDetails> {
    return this.http.put<AdmissionFormDetails>(`${this.url}/students/admissions/${id}`, form);
  }

  getAllResultsByParentId(id: number): Observable<Page<AdmissionForm>> {
    return this.http.get<Page<AdmissionForm>>(`${this.url}/admissions/${id}/results`);
  }

  getAllResultsByParentIdForPage(id: number, page: number): Observable<Page<AdmissionForm>> {
    return this.http.get<Page<AdmissionForm>>(`${this.url}/admissions/${id}/results?page=${page}`);
  }

  close(admissionId: number, students: CloseAdmissionRequest): Observable<Admission> {
    return this.http.post<Admission>(`${this.url}/admissions/${admissionId}/close`, students);
  }

  changeLimit(admissionId: number, limit: number): Observable<Admission>  {
    return this.http.put<Admission>(`${this.url}/admissions/${admissionId}/limit`, {studentLimit: limit});
  }

  recruit(userId: number, request: StudentProgramRequest): Observable<StudentProgram> {
    return this.http.post<StudentProgram>(`${this.url}/users/${userId}/student/programs`, request);
  }

  getAllForUser(id: number): Observable<Page<AdmissionForm>> {
    return this.http.get<Page<AdmissionForm>>(`${this.url}/users/${id}/admissions`);
  }

  getAllForUserForPage(id: number, page: number): Observable<Page<AdmissionForm>> {
    return this.http.get<Page<AdmissionForm>>(`${this.url}/users/${id}/admissions?page=${page}`);
  }
}
