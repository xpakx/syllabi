import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { InstituteForPage } from 'src/app/entity/institute-for-page';
import { Page } from 'src/app/entity/page';
import { InstituteService } from 'src/app/service/institute.service';
import { ModalDeleteInstituteComponent } from '../modal-delete-institute/modal-delete-institute.component';
import { PageableComponent } from '../pageable/pageable.component';

@Component({
  selector: 'app-show-institute-children',
  templateUrl: './show-institute-children.component.html',
  styleUrls: ['./show-institute-children.component.css']
})
export class ShowInstituteChildrenComponent extends PageableComponent<InstituteForPage> implements OnInit {
  
  constructor(private instituteService: InstituteService, private dialog: MatDialog,
    private router: Router, private route: ActivatedRoute) { 
      super();
    }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.instituteService.getAllChildren(id).subscribe(
      (response: Page<InstituteForPage>) => {
        this.printPage(response);
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

  getPage(page: number): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.instituteService.getAllChildrenForPage(id, page).subscribe(
      (response: Page<InstituteForPage>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        if(error.status === 401) {
          localStorage.removeItem("token");
          this.router.navigate(['login']);
        }
        this.message = error.error.message;
      }
    )
  }
  
  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {id: id, name: name};
    const dialogRef = this.dialog.open(ModalDeleteInstituteComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      (data) => {
        this.getPage(this.page);
      }
    );
  }

}
