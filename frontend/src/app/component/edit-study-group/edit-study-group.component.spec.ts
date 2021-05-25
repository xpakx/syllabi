import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditStudyGroupComponent } from './edit-study-group.component';

describe('EditStudyGroupComponent', () => {
  let component: EditStudyGroupComponent;
  let fixture: ComponentFixture<EditStudyGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditStudyGroupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditStudyGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
