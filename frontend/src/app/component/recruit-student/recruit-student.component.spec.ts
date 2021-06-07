import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecruitStudentComponent } from './recruit-student.component';

describe('RecruitStudentComponent', () => {
  let component: RecruitStudentComponent;
  let fixture: ComponentFixture<RecruitStudentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RecruitStudentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RecruitStudentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
