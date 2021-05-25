import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowInstitutesComponent } from './show-institutes.component';

describe('ShowInstitutesComponent', () => {
  let component: ShowInstitutesComponent;
  let fixture: ComponentFixture<ShowInstitutesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowInstitutesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowInstitutesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
