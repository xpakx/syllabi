import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalDeleteStudyGroupComponent } from './modal-delete-study-group.component';

describe('ModalDeleteStudyGroupComponent', () => {
  let component: ModalDeleteStudyGroupComponent;
  let fixture: ComponentFixture<ModalDeleteStudyGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalDeleteStudyGroupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalDeleteStudyGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
