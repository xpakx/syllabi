import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowInstituteComponent } from './show-institute.component';

describe('ShowInstituteComponent', () => {
  let component: ShowInstituteComponent;
  let fixture: ComponentFixture<ShowInstituteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowInstituteComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowInstituteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
