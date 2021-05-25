import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalTeacherDeleteComponent } from './modal-teacher-delete.component';

describe('ModalTeacherDeleteComponent', () => {
  let component: ModalTeacherDeleteComponent;
  let fixture: ComponentFixture<ModalTeacherDeleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalTeacherDeleteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalTeacherDeleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
