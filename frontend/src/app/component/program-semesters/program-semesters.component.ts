import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { ProgramSummary } from 'src/app/entity/program-summary';
import { Semester } from 'src/app/entity/semester';
import { SemesterService } from 'src/app/service/semester.service';
import { ModalDeleteCourseLiteratureComponent } from '../modal-delete-course-literature/modal-delete-course-literature.component';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-program-semesters',
  templateUrl: './program-semesters.component.html',
  styleUrls: ['./program-semesters.component.css']
})
export class ProgramSemestersComponent extends PageableGetAllChildrenComponent<Semester> implements OnInit {
  program: ProgramSummary | undefined;

  constructor(protected service: SemesterService,
    private dialog: MatDialog, protected route: ActivatedRoute, 
    protected router: Router) {
      super(service, router, route);
  }

  ngOnInit(): void {
      this.getFirstPage();
  
      this.service.getParentById(this.id).subscribe(
        (result: ProgramSummary) => {
          this.program = result;
        },
        (error: HttpErrorResponse) => {
          if(error.status === 401) {
            localStorage.removeItem("token");
            this.router.navigate(['login']);
          }
          this.message = error.error.message;
        }
      );
  }

  delete(id: number, name: string, programName: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name, parentName: programName};
    const dialogRef = this.dialog.open(ModalDeleteCourseLiteratureComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          this.getPage(this.page);
      }
    );
  }
}
