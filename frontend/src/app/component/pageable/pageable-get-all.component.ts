import { HttpErrorResponse } from "@angular/common/http";
import { MatDialog, MatDialogConfig } from "@angular/material/dialog";
import { Router } from "@angular/router";
import { Page } from "src/app/entity/page";
import { User } from "src/app/entity/user";
import { ServiceWithGetAll } from "src/app/service/service-with-get-all";
import { UserService } from "src/app/service/user.service";
import { ModalDeleteComponent } from "../modal-delete/modal-delete.component";
import { PageableComponent } from "./pageable.component";

export abstract class PageableGetAllComponent<T> extends  PageableComponent<T> {
  elemTypeName: string = "";

  constructor(protected service: ServiceWithGetAll<T>, protected userService: UserService,
  protected router: Router, protected dialog: MatDialog) { 
    super(userService);
  }

  getFirstPage(): void {
    this.service.getAll().subscribe(
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
    this.service.getAllForPage(page).subscribe(
      (response: Page<T>) => {
        this.printPage(response);
      },
      (error: HttpErrorResponse) => {
        this.message = error.error.message;
      }
    )
  }

  delete(id: number, name: string) {
    const dialogConfig: MatDialogConfig = new MatDialogConfig();
    dialogConfig.hasBackdrop = true;
    dialogConfig.data = {
      title: `Delete ${this.elemTypeName}`, 
      question: `Do you want to remove ${this.elemTypeName} ${name}?`
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