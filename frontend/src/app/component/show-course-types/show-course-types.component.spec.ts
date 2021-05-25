import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowCourseTypesComponent } from './show-course-types.component';

describe('ShowCourseTypesComponent', () => {
  let component: ShowCourseTypesComponent;
  let fixture: ComponentFixture<ShowCourseTypesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowCourseTypesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowCourseTypesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
