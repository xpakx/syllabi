import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { SemesterSummary } from 'src/app/entity/semester-summary';
import { SemesterService } from 'src/app/service/semester.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-semester',
  templateUrl: './show-semester.component.html',
  styleUrls: ['./show-semester.component.css']
})
export class ShowSemesterComponent extends ShowComponent<SemesterSummary> implements OnInit {

  constructor(protected service: SemesterService, protected userService: UserService,
    protected route: ActivatedRoute, 
    protected dialog: MatDialog, protected router: Router) {
      super(service, userService, router, route, dialog);
      this.elemTypeName = "semester";
      this.parentTypeName = "program";
  }

  ngOnInit(): void {
    this.getElem();
  }

  afterDeleteSuccess() {
    this.router.navigate(['programs/'+this.elem?.program.id+"/semesters"]);
  }
}
