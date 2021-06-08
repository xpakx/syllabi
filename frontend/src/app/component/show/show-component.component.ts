import { HttpErrorResponse } from "@angular/common/http";
import { MatDialog, MatDialogConfig } from "@angular/material/dialog";
import { ActivatedRoute, Router } from "@angular/router";
import { User } from "src/app/entity/user";
import { ServiceWithGetById } from "src/app/service/service-with-get-by-id";
import { UserService } from "src/app/service/user.service";
import { ModalDeleteComponent } from "../modal-delete/modal-delete.component";

export abstract class ShowComponent<T> {
  id: number;
  message: string = '';
  elem: T | undefined;
  admin: boolean = false;
  redir: string = '';
  elemTypeName: string = "";
  parentTypeName: string = "";

  constructor(protected service: ServiceWithGetById<T>, protected userService: UserService,
    protected router: Router, protected route: ActivatedRoute, protected dialog: MatDialog) { 
    this.id = Number(this.route.snapshot.paramMap.get('id'));
  }

  getElem(): void {
    this.service.getById(this.id).subscribe(
      (result: T) => {
        this.elem = result;
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

  loadNewElem(id: number): void {
    this.router.navigate([this.redir+id]);
    this.service.getById(id).subscribe(
      (result: T) => {
        this.elem = result;
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

  checkAuthority(role: string): void {
    this.userService.getById(Number(localStorage.getItem("user_id"))).subscribe(
      (response: User) => {
        let roles: string[] =  response.roles.map((p) => p.authority);
        if(roles.includes(role)) {
          this.admin = true;
        }
      },
      (error: HttpErrorResponse) => {}
    )
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
    
  }

  afterDeleteError(error: HttpErrorResponse) {
    this.message = error.error.message;
  }
}