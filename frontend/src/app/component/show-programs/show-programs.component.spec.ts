import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowProgramsComponent } from './show-programs.component';

describe('ShowProgramsComponent', () => {
  let component: ShowProgramsComponent;
  let fixture: ComponentFixture<ShowProgramsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowProgramsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowProgramsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
