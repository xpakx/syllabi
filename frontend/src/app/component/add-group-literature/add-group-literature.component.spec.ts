import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddGroupLiteratureComponent } from './add-group-literature.component';

describe('AddGroupLiteratureComponent', () => {
  let component: AddGroupLiteratureComponent;
  let fixture: ComponentFixture<AddGroupLiteratureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddGroupLiteratureComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddGroupLiteratureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
