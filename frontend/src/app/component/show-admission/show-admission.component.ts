import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { AdmissionDetails } from 'src/app/entity/admission-details';
import { AdmissionService } from 'src/app/service/admission.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-admission',
  templateUrl: './show-admission.component.html',
  styleUrls: ['./show-admission.component.css']
})
export class ShowAdmissionComponent extends ShowComponent<AdmissionDetails> implements OnInit {

  constructor(protected service: AdmissionService, protected userService: UserService,
     protected route: ActivatedRoute, 
    private dialog: MatDialog, protected router: Router) {  
      super(service, userService, router, route);
      this.redir = 'admissions/';
     }

  ngOnInit(): void {
    this.getElem();
    this.checkAuthority("ROLE_ADMISSION_ADMIN");
  }

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {
      title: "Delete admission", 
      question: "Do you want to remove admission " + name + "?"
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
        this.router.navigate(['admissions']);
      },
      (error: HttpErrorResponse) => {
        //show error
      }
    );
  }

}
