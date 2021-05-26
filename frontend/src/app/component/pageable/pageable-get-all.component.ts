import { HttpErrorResponse } from "@angular/common/http";
import { Router } from "@angular/router";
import { Page } from "src/app/entity/page";
import { ServiceWithGetAll } from "src/app/service/service-with-get-all";
import { PageableComponent } from "./pageable.component";

export abstract class PageableGetAllComponent<T> extends  PageableComponent<T> {
    constructor(protected service: ServiceWithGetAll<T>, protected router: Router) { 
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
}