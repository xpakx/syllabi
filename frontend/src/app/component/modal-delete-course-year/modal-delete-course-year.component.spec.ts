import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalDeleteCourseYearComponent } from './modal-delete-course-year.component';

describe('ModalDeleteCourseYearComponent', () => {
  let component: ModalDeleteCourseYearComponent;
  let fixture: ComponentFixture<ModalDeleteCourseYearComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalDeleteCourseYearComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalDeleteCourseYearComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
