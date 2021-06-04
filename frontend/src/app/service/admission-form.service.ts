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
    throw new Error('Method not implemented.');
  }

  getAllByParentIdForPage(id: number, page: number): Observable<Page<AdmissionForm>> {
    throw new Error('Method not implemented.');
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
}
