import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddStudyGroupComponent } from './add-study-group.component';

describe('AddStudyGroupComponent', () => {
  let component: AddStudyGroupComponent;
  let fixture: ComponentFixture<AddStudyGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddStudyGroupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddStudyGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
