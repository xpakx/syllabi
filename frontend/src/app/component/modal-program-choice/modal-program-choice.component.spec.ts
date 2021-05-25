import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalProgramChoiceComponent } from './modal-program-choice.component';

describe('ModalProgramChoiceComponent', () => {
  let component: ModalProgramChoiceComponent;
  let fixture: ComponentFixture<ModalProgramChoiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalProgramChoiceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalProgramChoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
