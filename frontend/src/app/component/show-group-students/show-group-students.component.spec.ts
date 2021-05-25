import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowGroupStudentsComponent } from './show-group-students.component';

describe('ShowGroupStudentsComponent', () => {
  let component: ShowGroupStudentsComponent;
  let fixture: ComponentFixture<ShowGroupStudentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowGroupStudentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowGroupStudentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
