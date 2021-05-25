import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowProgramComponent } from './show-program.component';

describe('ShowProgramComponent', () => {
  let component: ShowProgramComponent;
  let fixture: ComponentFixture<ShowProgramComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowProgramComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowProgramComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
