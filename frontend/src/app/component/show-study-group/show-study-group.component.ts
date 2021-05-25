import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { StudyGroup } from 'src/app/entity/study-group';
import { StudyGroupService } from 'src/app/service/study-group.service';
import { ModalDeleteStudyGroupComponent } from '../modal-delete-study-group/modal-delete-study-group.component';

@Component({
  selector: 'app-show-study-group',
  templateUrl: './show-study-group.component.html',
  styleUrls: ['./show-study-group.component.css']
})
export class ShowStudyGroupComponent implements OnInit {
  group: StudyGroup | undefined;
  message: string = '';
  id: number;

  constructor(private groupService: StudyGroupService, private route: ActivatedRoute, 
    private dialog: MatDialog, private router: Router) { 
      this.id = Number(this.route.snapshot.paramMap.get('id'));
    }

  ngOnInit(): void {
    this.groupService.getStudyGroupById(this.id).subscribe(
      (result: StudyGroup) => {
        this.group = result;
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
    const dialogRef = this.dialog.open(ModalDeleteStudyGroupComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data: boolean) => {
          this.router.navigate(["years/"+this.group?.year.id+"/groups"]);
      }
    );
  }

}
