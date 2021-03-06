import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { StudyGroup } from 'src/app/entity/study-group';
import { StudyGroupService } from 'src/app/service/study-group.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-study-group',
  templateUrl: './show-study-group.component.html',
  styleUrls: ['./show-study-group.component.css']
})
export class ShowStudyGroupComponent extends ShowComponent<StudyGroup> implements OnInit {

  constructor(protected groupService: StudyGroupService, protected userService: UserService,
    protected route: ActivatedRoute, 
    protected dialog: MatDialog, protected router: Router) { 
      super(groupService, userService, router, route, dialog);
      this.elemTypeName = "study group";
    }

  ngOnInit(): void {
    this.getElem();
  }

  afterDeleteSuccess() {
    this.router.navigate(["years/"+this.elem?.year.id+"/groups"]);
  }
}
