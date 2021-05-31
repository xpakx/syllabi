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
    private dialog: MatDialog, protected router: Router) {
      super(programService, userService, router, route);
     }

  ngOnInit(): void {
    this.getElem();
  }

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {
      title: "Delete program", 
      question: "Do you want to remove program " + name + "?"
    };
    const dialogRef = this.dialog.open(ModalDeleteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data: boolean) => {
          if(data) {
            this.deleteElem(id);
          }
      }
    );
  }

  deleteElem(id: number) {
    this.programService.delete(id).subscribe(
      (response) => {
        //redir
      },
      (error: HttpErrorResponse) => {
        //show error
      }
    );
  }
}
