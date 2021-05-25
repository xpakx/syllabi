import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule} from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatDividerModule } from '@angular/material/divider';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSelectModule } from '@angular/material/select';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { JwtModule, JWT_OPTIONS } from '@auth0/angular-jwt';

import { MatListModule } from '@angular/material/list';
import { MatGridListModule } from '@angular/material/grid-list';

import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { AddCourseComponent } from './component/add-course/add-course.component';
import { AllCoursesComponent } from './component/all-courses/all-courses.component';
import { ModalOrganizerChoiceComponent } from './component/modal-organizer-choice/modal-organizer-choice.component';
import { ModalDeleteCourseComponent } from './component/modal-delete-course/modal-delete-course.component';
import { ShowCourseComponent } from './component/show-course/show-course.component';
import { ShowCourseYearsComponent } from './component/show-course-years/show-course-years.component';
import { ModalDeleteCourseYearComponent } from './component/modal-delete-course-year/modal-delete-course-year.component';
import { AddCourseYearComponent } from './component/add-course-year/add-course-year.component';
import { ModalCoordinatorsChoiceComponent } from './component/modal-coordinators-choice/modal-coordinators-choice.component';
import { EditCourseYearComponent } from './component/edit-course-year/edit-course-year.component';
import { EditCourseComponent } from './component/edit-course/edit-course.component';
import { ModalPrerequisiteChoiceComponent } from './component/modal-prerequisite-choice/modal-prerequisite-choice.component';
import { ModalProgramChoiceComponent } from './component/modal-program-choice/modal-program-choice.component';


//import { MatOptionModule } from '@angular/material/op';

export function tokenGetter() {
  return localStorage.getItem('token');
}

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    AddCourseComponent,
    AllCoursesComponent,
    ModalOrganizerChoiceComponent,
    ModalDeleteCourseComponent,
    ShowCourseComponent,
    ShowCourseYearsComponent,
    ModalDeleteCourseYearComponent,
    AddCourseYearComponent,
    ModalCoordinatorsChoiceComponent,
    EditCourseYearComponent,
    EditCourseComponent,
    ModalPrerequisiteChoiceComponent,
    ModalProgramChoiceComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    JwtModule.forRoot({
      config: {
        tokenGetter: tokenGetter,
        allowedDomains: ['localhost:8080', '192.168.1.204:8080'],
        
      }
    }),
    FlexLayoutModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatMenuModule,
    MatIconModule,
    MatButtonModule,
    MatTableModule,
    MatDividerModule,
    MatProgressSpinnerModule,
    MatInputModule,
    MatCardModule,
    MatSlideToggleModule,
    MatSelectModule,
    MatDialogModule,
    MatCheckboxModule,
    MatListModule,
    MatGridListModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
