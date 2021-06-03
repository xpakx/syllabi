import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAdmissionsComponent } from './show-admissions.component';

describe('ShowAdmissionsComponent', () => {
  let component: ShowAdmissionsComponent;
  let fixture: ComponentFixture<ShowAdmissionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowAdmissionsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowAdmissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
