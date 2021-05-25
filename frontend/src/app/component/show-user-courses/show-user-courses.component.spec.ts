import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowUserCoursesComponent } from './show-user-courses.component';

describe('ShowUserCoursesComponent', () => {
  let component: ShowUserCoursesComponent;
  let fixture: ComponentFixture<ShowUserCoursesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowUserCoursesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowUserCoursesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
