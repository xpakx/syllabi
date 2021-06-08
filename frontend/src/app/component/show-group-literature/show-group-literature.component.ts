import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Literature } from 'src/app/entity/literature';
import { StudyGroupSummary } from 'src/app/entity/study-group-summary';
import { GroupLiteratureService } from 'src/app/service/group-literature.service';
import { UserService } from 'src/app/service/user.service';
import { ModalDeleteComponent } from '../modal-delete/modal-delete.component';
import { ShowComponent } from '../show/show-component.component';

@Component({
  selector: 'app-show-group-literature',
  templateUrl: './show-group-literature.component.html',
  styleUrls: ['./show-group-literature.component.css']
})
export class ShowGroupLiteratureComponent extends ShowComponent<Literature> implements OnInit {
  group: StudyGroupSummary | undefined;

  constructor(protected literatureService: GroupLiteratureService, protected userService: UserService,
    protected route: ActivatedRoute, 
    protected dialog: MatDialog, protected router: Router) {
      super(literatureService, userService, router, route, dialog);
      this.elemTypeName = "literature";
      this.parentTypeName = "group";
     }

  ngOnInit(): void {
    this.getElem();


    this.literatureService.getParentById(this.id).subscribe(
      (result: StudyGroupSummary) => {
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

  afterDeleteSuccess() {
    this.router.navigate(['groups/'+this.group?.id+'/literature']);
  }
}
