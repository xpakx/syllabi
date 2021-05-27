import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Institute } from 'src/app/entity/institute';
import { InstituteService } from 'src/app/service/institute.service';
import { ModalDeleteInstituteComponent } from '../modal-delete-institute/modal-delete-institute.component';

@Component({
  selector: 'app-show-institute',
  templateUrl: './show-institute.component.html',
  styleUrls: ['./show-institute.component.css']
})
export class ShowInstituteComponent implements OnInit {
  institute: Institute | undefined;
  message: string = '';

  constructor(private instituteService: InstituteService, private route: ActivatedRoute, 
    private dialog: MatDialog, private router: Router) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.instituteService.getById(id).subscribe(
      (result: Institute) => {
        this.institute = result;
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
    this.instituteService.getById(id).subscribe(
      (result: Institute) => {
        this.institute = result;
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
    const dialogRef = this.dialog.open(ModalDeleteInstituteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
          //redir
      }
    );
  }

}
