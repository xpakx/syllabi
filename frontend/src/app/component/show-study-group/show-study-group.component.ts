import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { StudyGroup } from 'src/app/entity/study-group';
import { StudyGroupService } from 'src/app/service/study-group.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteStudyGroupComponent } from '../modal-delete-study-group/modal-delete-study-group.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-study-group',
  templateUrl: './show-study-group.component.html',
  styleUrls: ['./show-study-group.component.css']
})
export class ShowStudyGroupComponent extends ShowComponent<StudyGroup> implements OnInit {

  constructor(protected groupService: StudyGroupService, protected userService: UserService,
    protected route: ActivatedRoute, 
    private dialog: MatDialog, protected router: Router) { 
      super(groupService, userService, router, route)
    }

  ngOnInit(): void {
    this.getElem();
  }

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name};
    const dialogRef = this.dialog.open(ModalDeleteStudyGroupComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data: boolean) => {
          this.router.navigate(["years/"+this.elem?.year.id+"/groups"]);
      }
    );
  }

}
