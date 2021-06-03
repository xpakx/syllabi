import { TestBed } from '@angular/core/testing';

import { AdmissionFormService } from './admission-form.service';

describe('AdmissionFormService', () => {
  let service: AdmissionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdmissionFormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
