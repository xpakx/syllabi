import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgramSemestersComponent } from './program-semesters.component';

describe('ProgramSemestersComponent', () => {
  let component: ProgramSemestersComponent;
  let fixture: ComponentFixture<ProgramSemestersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProgramSemestersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProgramSemestersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
