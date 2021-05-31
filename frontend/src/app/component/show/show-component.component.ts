import { HttpErrorResponse } from "@angular/common/http";
import { ActivatedRoute, Router } from "@angular/router";
import { User } from "src/app/entity/user";
import { ServiceWithGetById } from "src/app/service/service-with-get-by-id";
import { UserService } from "src/app/service/user.service";

export abstract class ShowComponent<T> {
  id: number;
  message: string = '';
  elem: T | undefined;
  admin: boolean = false;

  constructor(protected service: ServiceWithGetById<T>, protected userService: UserService,
    protected router: Router, protected route: ActivatedRoute) { 
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
}