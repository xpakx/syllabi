import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAdmissionFormsComponent } from './show-admission-forms.component';

describe('ShowAdmissionFormsComponent', () => {
  let component: ShowAdmissionFormsComponent;
  let fixture: ComponentFixture<ShowAdmissionFormsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowAdmissionFormsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowAdmissionFormsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
