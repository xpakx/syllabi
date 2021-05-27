import { HttpErrorResponse } from "@angular/common/http";
import { ActivatedRoute, Router } from "@angular/router";
import { ServiceWithGetById } from "src/app/service/service-with-get-by-id";

export abstract class ShowComponent<T> {
    id: number;
    message: string = '';
    elem: T | undefined;

    constructor(protected service: ServiceWithGetById<T>, protected router: Router,
        protected route: ActivatedRoute) { 
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

    loadCourse(id: number): void {
        this.router.navigate(['courses/'+id]);
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
}