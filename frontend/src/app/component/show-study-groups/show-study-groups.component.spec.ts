import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowStudyGroupsComponent } from './show-study-groups.component';

describe('ShowStudyGroupsComponent', () => {
  let component: ShowStudyGroupsComponent;
  let fixture: ComponentFixture<ShowStudyGroupsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowStudyGroupsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowStudyGroupsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
