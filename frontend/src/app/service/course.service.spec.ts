import { TestBed } from '@angular/core/testing';
import { Course } from '../entity/course';

import { CourseService } from './course.service';
import { of, defer } from 'rxjs';
import { CourseResponse } from '../entity/course-response';

describe('CourseService', () => {
  let service: CourseService;
  let httpClientSpy: { post: jasmine.Spy };

  beforeEach(() => {
    httpClientSpy = jasmine.createSpyObj('HttpClient', ['post']);
    service = new CourseService(httpClientSpy as any);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('#addNewCourse should return added course', () => {
    const token: CourseResponse = {id: 0,
      courseCode: 'code',
      iscedCode: 'code',
      erasmusCode: 'code',
      name: 'Dummy Course',
      ects: 12,
      language: 'pl',
      facultative: true,
      stationary: true,
  
      shortDescription: '',
      description: '',
      assessmentRules: '',
      effects: '',
      requirements: '',
  
      organizerId: undefined};

    httpClientSpy.post.and.returnValue(of(token));

    service.addNew(token).subscribe(
      response => expect(response).toEqual(token), fail
    );
    expect(httpClientSpy.post.calls.count()).toBe(1, 'one call');
  });





});
