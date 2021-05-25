import { TestBed } from '@angular/core/testing';

import { CourseYearService } from './course-year.service';

describe('CourseYearService', () => {
  let service: CourseYearService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CourseYearService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
