import { HttpErrorResponse } from "@angular/common/http";
import { ActivatedRoute, Router } from "@angular/router";
import { Page } from "src/app/entity/page";
import { User } from "src/app/entity/user";
import { ServiceWithGetAllChildren } from "src/app/service/service-with-get-all-children";
import { UserService } from "src/app/service/user.service";
import { PageableComponent } from "./pageable.component";

export abstract class PageableGetAllChildrenComponent<T, U> extends  PageableComponent<T> {
    protected id: number;
    parent!: U;

  constructor(protected service: ServiceWithGetAllChildren<T, U>, protected userService: UserService,
    protected router: Router, protected route: ActivatedRoute) { 
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
}