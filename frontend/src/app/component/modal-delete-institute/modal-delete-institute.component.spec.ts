import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalDeleteInstituteComponent } from './modal-delete-institute.component';

describe('ModalDeleteInstituteComponent', () => {
  let component: ModalDeleteInstituteComponent;
  let fixture: ComponentFixture<ModalDeleteInstituteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalDeleteInstituteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalDeleteInstituteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
