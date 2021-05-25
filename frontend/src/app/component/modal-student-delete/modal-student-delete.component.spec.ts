import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalStudentDeleteComponent } from './modal-student-delete.component';

describe('ModalStudentDeleteComponent', () => {
  let component: ModalStudentDeleteComponent;
  let fixture: ComponentFixture<ModalStudentDeleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalStudentDeleteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalStudentDeleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
