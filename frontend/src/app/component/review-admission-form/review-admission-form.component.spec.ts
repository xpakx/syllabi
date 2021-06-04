import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewAdmissionFormComponent } from './review-admission-form.component';

describe('ReviewAdmissionFormComponent', () => {
  let component: ReviewAdmissionFormComponent;
  let fixture: ComponentFixture<ReviewAdmissionFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewAdmissionFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReviewAdmissionFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
