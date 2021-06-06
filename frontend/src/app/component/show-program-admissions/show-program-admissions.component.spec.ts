import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowProgramAdmissionsComponent } from './show-program-admissions.component';

describe('ShowProgramAdmissionsComponent', () => {
  let component: ShowProgramAdmissionsComponent;
  let fixture: ComponentFixture<ShowProgramAdmissionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowProgramAdmissionsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowProgramAdmissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
