import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowCourseLiteratureComponent } from './show-course-literature.component';

describe('ShowCourseLiteratureComponent', () => {
  let component: ShowCourseLiteratureComponent;
  let fixture: ComponentFixture<ShowCourseLiteratureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowCourseLiteratureComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowCourseLiteratureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
