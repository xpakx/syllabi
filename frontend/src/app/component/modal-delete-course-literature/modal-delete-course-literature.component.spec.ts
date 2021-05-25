import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalDeleteCourseLiteratureComponent } from './modal-delete-course-literature.component';

describe('ModalDeleteCourseLiteratureComponent', () => {
  let component: ModalDeleteCourseLiteratureComponent;
  let fixture: ComponentFixture<ModalDeleteCourseLiteratureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModalDeleteCourseLiteratureComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModalDeleteCourseLiteratureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
