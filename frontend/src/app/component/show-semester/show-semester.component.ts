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
    private dialog: MatDialog, protected router: Router) {
      super(service, userService, router, route);
  }

  ngOnInit(): void {
    this.getElem();
  }

  delete(id: number, name: string, programName: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {
      title: "Delete semester for program " + programName, 
      question: "Do you want to remove semester " + name + "?"
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
    this.service.delete(id).subscribe(
      (response) => {
        this.router.navigate(['programs/'+this.elem?.program.id+"/semesters"]);
      },
      (error: HttpErrorResponse) => {
        //show error
      }
    );
  }
}
