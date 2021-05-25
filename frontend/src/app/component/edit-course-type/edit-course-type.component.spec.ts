import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditCourseTypeComponent } from './edit-course-type.component';

describe('EditCourseTypeComponent', () => {
  let component: EditCourseTypeComponent;
  let fixture: ComponentFixture<EditCourseTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditCourseTypeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditCourseTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
