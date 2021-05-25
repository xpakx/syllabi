import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCourseLiteratureComponent } from './add-course-literature.component';

describe('AddCourseLiteratureComponent', () => {
  let component: AddCourseLiteratureComponent;
  let fixture: ComponentFixture<AddCourseLiteratureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddCourseLiteratureComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddCourseLiteratureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
