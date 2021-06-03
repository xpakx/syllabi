import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Admission } from '../entity/admission';
import { Page } from '../entity/page';
import { ServiceWithGetAll } from './service-with-get-all';
import { ServiceWithGetById } from './service-with-get-by-id';

@Injectable({
  providedIn: 'root'
})
export class AdmissionService implements ServiceWithGetAll<Admission>, ServiceWithGetById<Admission> {
  private url = environment.apiServerUrl;

  constructor(private http: HttpClient) { }

  getById(id: number): Observable<Admission> {
    return this.http.get<Admission>(`${this.url}/admissions/${id}`);
  }

  getAll(): Observable<Page<Admission>> {
    return this.http.get<Page<Admission>>(`${this.url}/admissions`);
  }

  getAllForPage(page: number): Observable<Page<Admission>> {
    return this.http.get<Page<Admission>>(`${this.url}/admissions?page=${page}`);
  }

  delete(id: number): Observable<any> {
    throw new Error('Method not implemented.');
  }


}
