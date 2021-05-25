import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowStudyGroupComponent } from './show-study-group.component';

describe('ShowStudyGroupComponent', () => {
  let component: ShowStudyGroupComponent;
  let fixture: ComponentFixture<ShowStudyGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowStudyGroupComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowStudyGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
