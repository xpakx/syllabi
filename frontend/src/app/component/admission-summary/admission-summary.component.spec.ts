import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdmissionSummaryComponent } from './admission-summary.component';

describe('AdmissionSummaryComponent', () => {
  let component: AdmissionSummaryComponent;
  let fixture: ComponentFixture<AdmissionSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdmissionSummaryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdmissionSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
