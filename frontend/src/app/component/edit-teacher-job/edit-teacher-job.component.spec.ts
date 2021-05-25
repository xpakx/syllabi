import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditTeacherJobComponent } from './edit-teacher-job.component';

describe('EditTeacherJobComponent', () => {
  let component: EditTeacherJobComponent;
  let fixture: ComponentFixture<EditTeacherJobComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditTeacherJobComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditTeacherJobComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
