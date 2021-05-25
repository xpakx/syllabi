import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditCourseLiteratureComponent } from './edit-course-literature.component';

describe('EditCourseLiteratureComponent', () => {
  let component: EditCourseLiteratureComponent;
  let fixture: ComponentFixture<EditCourseLiteratureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditCourseLiteratureComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditCourseLiteratureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
