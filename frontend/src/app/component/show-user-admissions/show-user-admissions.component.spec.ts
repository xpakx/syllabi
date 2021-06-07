import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowUserAdmissionsComponent } from './show-user-admissions.component';

describe('ShowUserAdmissionsComponent', () => {
  let component: ShowUserAdmissionsComponent;
  let fixture: ComponentFixture<ShowUserAdmissionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowUserAdmissionsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowUserAdmissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
