import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowInstituteCoursesComponent } from './show-institute-courses.component';

describe('ShowInstituteCoursesComponent', () => {
  let component: ShowInstituteCoursesComponent;
  let fixture: ComponentFixture<ShowInstituteCoursesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowInstituteCoursesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowInstituteCoursesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
