import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Institute } from 'src/app/entity/institute';
import { ProgramForPage } from 'src/app/entity/program-for-page';
import { InstituteProgramsAdapterService } from 'src/app/service/institute-programs-adapter.service';
import { UserService } from 'src/app/service/user.service';
import { PageableGetAllChildrenComponent } from '../pageable/pageable-get-all-children.component';

@Component({
  selector: 'app-show-institute-programs',
  templateUrl: './show-institute-programs.component.html',
  styleUrls: ['./show-institute-programs.component.css']
})
export class ShowInstituteProgramsComponent extends PageableGetAllChildrenComponent<ProgramForPage, Institute> implements OnInit {
  
  constructor(protected service: InstituteProgramsAdapterService, protected userService: UserService,
    protected dialog: MatDialog,
    protected route: ActivatedRoute, protected router: Router) {  
      super(service, userService, router, route, dialog);
      this.elemTypeName = "program";
    }

  ngOnInit(): void {
    this.getFirstPage();
    this.getParent();
    this.checkAuthority("ROLE_COURSE_ADMIN");
  }
}
