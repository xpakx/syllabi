import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCourseYearComponent } from './add-course-year.component';

describe('AddCourseYearComponent', () => {
  let component: AddCourseYearComponent;
  let fixture: ComponentFixture<AddCourseYearComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddCourseYearComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddCourseYearComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
