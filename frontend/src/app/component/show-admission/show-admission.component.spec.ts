import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAdmissionComponent } from './show-admission.component';

describe('ShowAdmissionComponent', () => {
  let component: ShowAdmissionComponent;
  let fixture: ComponentFixture<ShowAdmissionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowAdmissionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowAdmissionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
