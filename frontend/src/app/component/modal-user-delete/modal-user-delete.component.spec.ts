import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalUserDeleteComponent } from './modal-user-delete.component';

describe('ModalUserDeleteComponent', () => {
  let component: ModalUserDeleteComponent;
  let fixture: ComponentFixture<ModalUserDeleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalUserDeleteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalUserDeleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
