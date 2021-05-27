import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Program } from 'src/app/entity/program';
import { ProgramSummary } from 'src/app/entity/program-summary';
import { ProgramService } from 'src/app/service/program.service';
import { ModalDeleteStudyGroupComponent } from '../modal-delete-study-group/modal-delete-study-group.component';
import { ModalProgramDeleteComponent } from '../modal-program-delete/modal-program-delete.component';

@Component({
  selector: 'app-show-program',
  templateUrl: './show-program.component.html',
  styleUrls: ['./show-program.component.css']
})
export class ShowProgramComponent implements OnInit {
  program: Program | undefined;
  message: string = '';

  constructor(private programService: ProgramService, private route: ActivatedRoute, 
    private dialog: MatDialog, private router: Router) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.programService.getById(id).subscribe(
      (result: Program) => {
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

  loadCourse(id: number): void {
    this.programService.getById(id).subscribe(
      (result: Program) => {
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

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name};
    const dialogRef = this.dialog.open(ModalProgramDeleteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          //redir
      }
    );
  }
}
