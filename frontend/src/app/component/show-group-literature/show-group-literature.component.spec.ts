import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowGroupLiteratureComponent } from './show-group-literature.component';

describe('ShowGroupLiteratureComponent', () => {
  let component: ShowGroupLiteratureComponent;
  let fixture: ComponentFixture<ShowGroupLiteratureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowGroupLiteratureComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowGroupLiteratureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
