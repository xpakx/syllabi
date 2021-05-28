import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowSemesterCoursesComponent } from './show-semester-courses.component';

describe('ShowSemesterCoursesComponent', () => {
  let component: ShowSemesterCoursesComponent;
  let fixture: ComponentFixture<ShowSemesterCoursesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowSemesterCoursesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowSemesterCoursesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
