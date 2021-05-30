import { HttpErrorResponse } from "@angular/common/http";
import { Router } from "@angular/router";
import { Page } from "src/app/entity/page";
import { User } from "src/app/entity/user";
import { ServiceWithGetAll } from "src/app/service/service-with-get-all";
import { UserService } from "src/app/service/user.service";
import { PageableComponent } from "./pageable.component";

export abstract class PageableGetAllComponent<T> extends  PageableComponent<T> {
  admin: boolean = false;

  constructor(protected service: ServiceWithGetAll<T>, protected userService: UserService,
    protected router: Router) { 
      super();
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

  checkAuthority(role: string): void {
    this.userService.getUserById(Number(localStorage.getItem("user_id"))).subscribe(
      (response: User) => {
        let roles: string[] =  response.roles.map((p) => p.authority);
        if(roles.includes(role)) {
          this.admin = true;
        }
      },
      (error: HttpErrorResponse) => {}
    )
  }
}