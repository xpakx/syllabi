import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowSemesterComponent } from './show-semester.component';

describe('ShowSemesterComponent', () => {
  let component: ShowSemesterComponent;
  let fixture: ComponentFixture<ShowSemesterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowSemesterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowSemesterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
