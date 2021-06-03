import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalAddWeightComponent } from './modal-add-weight.component';

describe('ModalAddWeightComponent', () => {
  let component: ModalAddWeightComponent;
  let fixture: ComponentFixture<ModalAddWeightComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalAddWeightComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalAddWeightComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
