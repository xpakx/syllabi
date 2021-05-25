import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditCourseYearComponent } from './edit-course-year.component';

describe('EditCourseYearComponent', () => {
  let component: EditCourseYearComponent;
  let fixture: ComponentFixture<EditCourseYearComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditCourseYearComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditCourseYearComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
