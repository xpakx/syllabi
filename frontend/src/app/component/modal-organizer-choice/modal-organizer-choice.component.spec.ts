import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalOrganizerChoiceComponent } from './modal-organizer-choice.component';

describe('ModalOrganizerChoiceComponent', () => {
  let component: ModalOrganizerChoiceComponent;
  let fixture: ComponentFixture<ModalOrganizerChoiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalOrganizerChoiceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalOrganizerChoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
