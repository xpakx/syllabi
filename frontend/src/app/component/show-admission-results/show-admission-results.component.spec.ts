import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAdmissionResultsComponent } from './show-admission-results.component';

describe('ShowAdmissionResultsComponent', () => {
  let component: ShowAdmissionResultsComponent;
  let fixture: ComponentFixture<ShowAdmissionResultsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowAdmissionResultsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowAdmissionResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
