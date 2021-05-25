import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalCoordinatorsChoiceComponent } from './modal-coordinators-choice.component';

describe('ModalCoordinatorsChoiceComponent', () => {
  let component: ModalCoordinatorsChoiceComponent;
  let fixture: ComponentFixture<ModalCoordinatorsChoiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalCoordinatorsChoiceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalCoordinatorsChoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
