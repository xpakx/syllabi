import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalCourseTypeChoiceComponent } from './modal-course-type-choice.component';

describe('ModalCourseTypeChoiceComponent', () => {
  let component: ModalCourseTypeChoiceComponent;
  let fixture: ComponentFixture<ModalCourseTypeChoiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalCourseTypeChoiceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalCourseTypeChoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
