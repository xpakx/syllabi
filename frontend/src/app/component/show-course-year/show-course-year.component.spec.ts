import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowCourseYearComponent } from './show-course-year.component';

describe('ShowCourseYearComponent', () => {
  let component: ShowCourseYearComponent;
  let fixture: ComponentFixture<ShowCourseYearComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowCourseYearComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowCourseYearComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
