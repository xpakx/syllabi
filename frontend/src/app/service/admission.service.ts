import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Admission } from '../entity/admission';
import { AdmissionDetails } from '../entity/admission-details';
import { AdmissionRequest } from '../entity/admission-request';
import { Page } from '../entity/page';
import { ServiceWithGetAll } from './service-with-get-all';
import { ServiceWithGetById } from './service-with-get-by-id';

@Injectable({
  providedIn: 'root'
})
export class AdmissionService implements ServiceWithGetAll<Admission>, ServiceWithGetById<AdmissionDetails> {
  private url = environment.apiServerUrl;

  constructor(private http: HttpClient) { }

  getById(id: number): Observable<AdmissionDetails> {
    return this.http.get<AdmissionDetails>(`${this.url}/admissions/${id}`);
  }

  getAll(): Observable<Page<Admission>> {
    return this.http.get<Page<Admission>>(`${this.url}/admissions`);
  }

  getAllForPage(page: number): Observable<Page<Admission>> {
    return this.http.get<Page<Admission>>(`${this.url}/admissions?page=${page}`);
  }

  delete(id: number): Observable<any> {
    return this.http.delete<any>(`${this.url}/admissions/${id}`);
  }

  addNew(id: number, admission: AdmissionRequest): Observable<Admission> {
    return this.http.post<Admission>(`${this.url}/programs/${id}/admissions`, admission);
  }
}
