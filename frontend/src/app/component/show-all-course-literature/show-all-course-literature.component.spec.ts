import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAllCourseLiteratureComponent } from './show-all-course-literature.component';

describe('ShowAllCourseLiteratureComponent', () => {
  let component: ShowAllCourseLiteratureComponent;
  let fixture: ComponentFixture<ShowAllCourseLiteratureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowAllCourseLiteratureComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowAllCourseLiteratureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
