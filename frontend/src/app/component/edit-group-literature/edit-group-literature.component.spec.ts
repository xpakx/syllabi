import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditGroupLiteratureComponent } from './edit-group-literature.component';

describe('EditGroupLiteratureComponent', () => {
  let component: EditGroupLiteratureComponent;
  let fixture: ComponentFixture<EditGroupLiteratureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditGroupLiteratureComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EditGroupLiteratureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
