import { HttpErrorResponse } from "@angular/common/http";
import { MatDialog, MatDialogConfig } from "@angular/material/dialog";
import { ActivatedRoute, Router } from "@angular/router";
import { Page } from "src/app/entity/page";
import { User } from "src/app/entity/user";
import { ServiceWithGetAllChildren } from "src/app/service/service-with-get-all-children";
import { UserService } from "src/app/service/user.service";
import { ModalDeleteComponent } from "../modal-delete/modal-delete.component";
import { PageableComponent } from "./pageable.component";

export abstract class PageableGetAllChildrenComponent<T, U> extends  PageableComponent<T> {
    protected id: number;
    parent!: U;
    elemTypeName: string = "";
    parentTypeName: string = "";

  constructor(protected service: ServiceWithGetAllChildren<T, U>, protected userService: UserService,
    protected router: Router, protected route: ActivatedRoute, protected dialog: MatDialog) { 
    super(userService);
    this.id = Number(this.route.snapshot.paramMap.get('id'));
  }

  getFirstPage(): void {
    this.service.getAllByParentId(this.id).subscribe(
      (response: Page<T>) => {
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

  getPage(page: number): void {
    this.service.getAllByParentIdForPage(this.id, page).subscribe(
      (response: Page<T>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        this.message = error.error.message;
      }
    )
  }

  getParent() {
    this.service.getParentById(this.id).subscribe(
      (result: U) => {
        this.afterGetParentSuccess(result);
      },
      (error: HttpErrorResponse) => {
        this.afterGetParentError(error);
      }
    );
  }
    
  afterGetParentSuccess(result: U) {
    this.parent = result;
  }  

  afterGetParentError(error: HttpErrorResponse) {
    if(error.status === 401) {
      localStorage.removeItem("token");
      this.router.navigate(['login']);
    }
    this.message = error.error.message;
  }

  delete(id: number, name?: string, parentName?: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {
      title: parentName ? `Delete ${this.elemTypeName} for ${this.parentTypeName} ${parentName}` : `Delete ${this.elemTypeName}`, 
      question: name ? `Do you want to remove ${this.elemTypeName} ${name}?` : `Do you want to remove ${this.elemTypeName}?`
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
        this.afterDeleteSuccess();
      },
      (error: HttpErrorResponse) => {
        this.afterDeleteError(error);
      }
    );
  }

  afterDeleteSuccess() {
    this.getPage(this.page);
  }

  afterDeleteError(error: HttpErrorResponse) {
    this.message = error.error.message;
  }
}