import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Literature } from '../entity/literature';
import { LiteratureForPage } from '../entity/literature-for-page';
import { LiteratureRequest } from '../entity/literature-request';
import { Page } from '../entity/page';
import { ServiceWithDelete } from './service-with-delete';

@Injectable({
  providedIn: 'root'
})
export class LiteratureService {
  protected url = environment.apiServerUrl;

  constructor(protected http: HttpClient) { }
}
