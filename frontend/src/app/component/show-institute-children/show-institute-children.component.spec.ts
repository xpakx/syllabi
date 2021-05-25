import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowInstituteChildrenComponent } from './show-institute-children.component';

describe('ShowInstituteChildrenComponent', () => {
  let component: ShowInstituteChildrenComponent;
  let fixture: ComponentFixture<ShowInstituteChildrenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowInstituteChildrenComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowInstituteChildrenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
