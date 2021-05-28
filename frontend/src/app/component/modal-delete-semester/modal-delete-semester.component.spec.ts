import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalDeleteSemesterComponent } from './modal-delete-semester.component';

describe('ModalDeleteSemesterComponent', () => {
  let component: ModalDeleteSemesterComponent;
  let fixture: ComponentFixture<ModalDeleteSemesterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalDeleteSemesterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalDeleteSemesterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
