import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalProgramDeleteComponent } from './modal-program-delete.component';

describe('ModalProgramDeleteComponent', () => {
  let component: ModalProgramDeleteComponent;
  let fixture: ComponentFixture<ModalProgramDeleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalProgramDeleteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalProgramDeleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
