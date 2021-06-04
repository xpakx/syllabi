import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { AdmissionFormDetails } from 'src/app/entity/admission-form-details';
import { AdmissionFormService } from 'src/app/service/admission-form.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-admission-form',
  templateUrl: './show-admission-form.component.html',
  styleUrls: ['./show-admission-form.component.css']
})
export class ShowAdmissionFormComponent extends ShowComponent<AdmissionFormDetails> implements OnInit {
  constructor(protected service: AdmissionFormService, protected userService: UserService,
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
     title: "Delete admission form", 
     question: "Do you want to remove admission form " + name + "?"
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
