import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalDeleteCourseTypeComponent } from './modal-delete-course-type.component';

describe('ModalDeleteCourseTypeComponent', () => {
  let component: ModalDeleteCourseTypeComponent;
  let fixture: ComponentFixture<ModalDeleteCourseTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalDeleteCourseTypeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalDeleteCourseTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
