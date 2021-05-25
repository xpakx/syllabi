import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalPrerequisiteChoiceComponent } from './modal-prerequisite-choice.component';

describe('ModalPrerequisiteChoiceComponent', () => {
  let component: ModalPrerequisiteChoiceComponent;
  let fixture: ComponentFixture<ModalPrerequisiteChoiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalPrerequisiteChoiceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalPrerequisiteChoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
