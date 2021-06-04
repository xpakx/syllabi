import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAdmissionFormComponent } from './show-admission-form.component';

describe('ShowAdmissionFormComponent', () => {
  let component: ShowAdmissionFormComponent;
  let fixture: ComponentFixture<ShowAdmissionFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowAdmissionFormComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowAdmissionFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
