import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowProgramCoursesComponent } from './show-program-courses.component';

describe('ShowProgramCoursesComponent', () => {
  let component: ShowProgramCoursesComponent;
  let fixture: ComponentFixture<ShowProgramCoursesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowProgramCoursesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowProgramCoursesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
