import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Institute } from 'src/app/entity/institute';
import { InstituteService } from 'src/app/service/institute.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-institute',
  templateUrl: './show-institute.component.html',
  styleUrls: ['./show-institute.component.css']
})
export class ShowInstituteComponent extends ShowComponent<Institute> implements OnInit {
  institute: Institute | undefined;

  constructor(protected instituteService: InstituteService, protected userService: UserService,
    protected route: ActivatedRoute, 
    protected dialog: MatDialog, protected router: Router) { 
      super(instituteService, userService, router, route, dialog);
      this.elemTypeName = "institute";
    }

  ngOnInit(): void {
    this.getElem();
  }

  afterDeleteSuccess() {
    this.router.navigate(['institutes']);
  }
}
