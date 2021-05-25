import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalDeleteCourseComponent } from './modal-delete-course.component';

describe('ModalDeleteCourseComponent', () => {
  let component: ModalDeleteCourseComponent;
  let fixture: ComponentFixture<ModalDeleteCourseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalDeleteCourseComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalDeleteCourseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
