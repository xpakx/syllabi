import { HttpErrorResponse } from "@angular/common/http";
import { ActivatedRoute, Router } from "@angular/router";
import { Page } from "src/app/entity/page";
import { ServiceWithGetAllChildren } from "src/app/service/service-with-get-all-children";
import { PageableComponent } from "./pageable.component";

export abstract class PageableGetAllChildrenComponent<T> extends  PageableComponent<T> {
    protected id: number;

    constructor(protected service: ServiceWithGetAllChildren<T>, protected router: Router,
      protected route: ActivatedRoute) { 
        super();
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
}