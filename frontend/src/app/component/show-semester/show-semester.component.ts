import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { SemesterSummary } from 'src/app/entity/semester-summary';
import { SemesterService } from 'src/app/service/semester.service';
import { ModalDeleteSemesterComponent } from '../modal-delete-semester/modal-delete-semester.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-semester',
  templateUrl: './show-semester.component.html',
  styleUrls: ['./show-semester.component.css']
})
export class ShowSemesterComponent extends ShowComponent<SemesterSummary> implements OnInit {

  constructor(protected service: SemesterService, protected route: ActivatedRoute, 
    private dialog: MatDialog, protected router: Router) {
      super(service, router, route);
  }

  ngOnInit(): void {
    this.getElem();
  }

  delete(id: number, name: string, programName: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name, parentName: programName};
    const dialogRef = this.dialog.open(ModalDeleteSemesterComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data: boolean) => {
        if(data) {
          this.router.navigate(['programs/'+this.elem?.program.id+"/semesters"]);
        }
      }
    );
  }
}
