import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Program } from 'src/app/entity/program';
import { ProgramService } from 'src/app/service/program.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-program',
  templateUrl: './show-program.component.html',
  styleUrls: ['./show-program.component.css']
})
export class ShowProgramComponent extends ShowComponent<Program> implements OnInit {

  constructor(protected programService: ProgramService, protected userService: UserService,
    protected route: ActivatedRoute, 
    protected dialog: MatDialog, protected router: Router) {
      super(programService, userService, router, route, dialog);
      this.elemTypeName = "program";
     }

  ngOnInit(): void {
    this.getElem();
  }

  afterDeleteSuccess() {
    this.router.navigate(['programs']);
  }
}
