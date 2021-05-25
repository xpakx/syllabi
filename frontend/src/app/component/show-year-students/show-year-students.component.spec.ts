import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowYearStudentsComponent } from './show-year-students.component';

describe('ShowYearStudentsComponent', () => {
  let component: ShowYearStudentsComponent;
  let fixture: ComponentFixture<ShowYearStudentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowYearStudentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowYearStudentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
