import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowInstituteProgramsComponent } from './show-institute-programs.component';

describe('ShowInstituteProgramsComponent', () => {
  let component: ShowInstituteProgramsComponent;
  let fixture: ComponentFixture<ShowInstituteProgramsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowInstituteProgramsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowInstituteProgramsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
