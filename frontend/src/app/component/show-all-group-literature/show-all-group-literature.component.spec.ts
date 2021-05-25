import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowAllGroupLiteratureComponent } from './show-all-group-literature.component';

describe('ShowAllGroupLiteratureComponent', () => {
  let component: ShowAllGroupLiteratureComponent;
  let fixture: ComponentFixture<ShowAllGroupLiteratureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowAllGroupLiteratureComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShowAllGroupLiteratureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
