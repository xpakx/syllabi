import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdmissionFormSummaryComponent } from './admission-form-summary.component';

describe('AdmissionFormSummaryComponent', () => {
  let component: AdmissionFormSummaryComponent;
  let fixture: ComponentFixture<AdmissionFormSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdmissionFormSummaryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdmissionFormSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
