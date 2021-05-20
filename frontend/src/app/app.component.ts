import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { AuthenticationService } from './service/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title: string = 'syllabing';

  constructor(private authService: AuthenticationService, private dialog: MatDialog,
  private router: Router) {}

  public isAuthenticated(): boolean {
    let user = localStorage.getItem("token");
    return !(user === null);
  }

  public logOut(): void {
    localStorage.removeItem("token");
    this.router.navigate(['login']);
  }
}
