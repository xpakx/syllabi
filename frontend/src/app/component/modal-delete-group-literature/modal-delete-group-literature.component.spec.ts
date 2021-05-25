import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalDeleteGroupLiteratureComponent } from './modal-delete-group-literature.component';

describe('ModalDeleteGroupLiteratureComponent', () => {
  let component: ModalDeleteGroupLiteratureComponent;
  let fixture: ComponentFixture<ModalDeleteGroupLiteratureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalDeleteGroupLiteratureComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalDeleteGroupLiteratureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
