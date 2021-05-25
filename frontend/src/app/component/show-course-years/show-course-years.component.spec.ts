import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowCourseYearsComponent } from './show-course-years.component';

describe('ShowCourseYearsComponent', () => {
  let component: ShowCourseYearsComponent;
  let fixture: ComponentFixture<ShowCourseYearsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowCourseYearsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowCourseYearsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
