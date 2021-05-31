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
import { ShowAllCourseLiteratureComponent } from './component/show-all-course-literature/show-all-course-literature.component';
import { ModalDeleteCourseLiteratureComponent } from './component/modal-delete-course-literature/modal-delete-course-literature.component';
import { AddCourseLiteratureComponent } from './component/add-course-literature/add-course-literature.component';
import { ShowCourseLiteratureComponent } from './component/show-course-literature/show-course-literature.component';
import { EditCourseLiteratureComponent } from './component/edit-course-literature/edit-course-literature.component';
import { ShowCourseYearComponent } from './component/show-course-year/show-course-year.component';
import { ShowStudyGroupsComponent } from './component/show-study-groups/show-study-groups.component';
import { ModalDeleteStudyGroupComponent } from './component/modal-delete-study-group/modal-delete-study-group.component';
import { AddStudyGroupComponent } from './component/add-study-group/add-study-group.component';
import { ModalCourseTypeChoiceComponent } from './component/modal-course-type-choice/modal-course-type-choice.component';
import { ShowYearStudentsComponent } from './component/show-year-students/show-year-students.component';
import { ModalStudentDeleteComponent } from './component/modal-student-delete/modal-student-delete.component';
import { ShowStudyGroupComponent } from './component/show-study-group/show-study-group.component';
import { EditStudyGroupComponent } from './component/edit-study-group/edit-study-group.component';
import { ShowAllGroupLiteratureComponent } from './component/show-all-group-literature/show-all-group-literature.component';
import { ModalDeleteGroupLiteratureComponent } from './component/modal-delete-group-literature/modal-delete-group-literature.component';
import { AddGroupLiteratureComponent } from './component/add-group-literature/add-group-literature.component';
import { ShowGroupLiteratureComponent } from './component/show-group-literature/show-group-literature.component';
import { EditGroupLiteratureComponent } from './component/edit-group-literature/edit-group-literature.component';
import { ShowGroupStudentsComponent } from './component/show-group-students/show-group-students.component';
import { ShowCourseTypesComponent } from './component/show-course-types/show-course-types.component';
import { ModalDeleteCourseTypeComponent } from './component/modal-delete-course-type/modal-delete-course-type.component';
import { AddCourseTypeComponent } from './component/add-course-type/add-course-type.component';
import { EditCourseTypeComponent } from './component/edit-course-type/edit-course-type.component';
import { ShowProgramsComponent } from './component/show-programs/show-programs.component';
import { ModalProgramDeleteComponent } from './component/modal-program-delete/modal-program-delete.component';
import { AddProgramComponent } from './component/add-program/add-program.component';
import { ShowProgramComponent } from './component/show-program/show-program.component';
import { EditProgramComponent } from './component/edit-program/edit-program.component';
import { ShowProgramCoursesComponent } from './component/show-program-courses/show-program-courses.component';
import { ShowInstitutesComponent } from './component/show-institutes/show-institutes.component';
import { ModalDeleteInstituteComponent } from './component/modal-delete-institute/modal-delete-institute.component';
import { AddInstituteComponent } from './component/add-institute/add-institute.component';
import { ShowInstituteComponent } from './component/show-institute/show-institute.component';
import { EditInstituteComponent } from './component/edit-institute/edit-institute.component';
import { ShowInstituteCoursesComponent } from './component/show-institute-courses/show-institute-courses.component';
import { ShowInstituteProgramsComponent } from './component/show-institute-programs/show-institute-programs.component';
import { ShowInstituteChildrenComponent } from './component/show-institute-children/show-institute-children.component';
import { ShowStudentComponent } from './component/show-student/show-student.component';
import { AddStudentComponent } from './component/add-student/add-student.component';
import { EditStudentComponent } from './component/edit-student/edit-student.component';
import { AddTeacherComponent } from './component/add-teacher/add-teacher.component';
import { EditTeacherComponent } from './component/edit-teacher/edit-teacher.component';
import { EditTeacherJobComponent } from './component/edit-teacher-job/edit-teacher-job.component';
import { ShowTeacherComponent } from './component/show-teacher/show-teacher.component';
import { ModalTeacherDeleteComponent } from './component/modal-teacher-delete/modal-teacher-delete.component';
import { ShowTeachersComponent } from './component/show-teachers/show-teachers.component';
import { ShowUsersComponent } from './component/show-users/show-users.component';
import { ModalUserDeleteComponent } from './component/modal-user-delete/modal-user-delete.component';
import { ShowUserComponent } from './component/show-user/show-user.component';
import { AddUserRoleComponent } from './component/add-user-role/add-user-role.component';
import { ShowUserCoursesComponent } from './component/show-user-courses/show-user-courses.component';
import { ChangePasswordComponent } from './component/change-password/change-password.component';
import { NotFoundComponent } from './component/not-found/not-found.component';
import { PaginationComponent } from './component/pagination/pagination.component';
import { CourseSummaryComponent } from './component/course-summary/course-summary.component';
import { ProgramSemestersComponent } from './component/program-semesters/program-semesters.component';
import { ShowSemesterComponent } from './component/show-semester/show-semester.component';
import { ModalDeleteSemesterComponent } from './component/modal-delete-semester/modal-delete-semester.component';
import { AddSemesterComponent } from './component/add-semester/add-semester.component';
import { EditSemesterComponent } from './component/edit-semester/edit-semester.component';
import { ShowSemesterCoursesComponent } from './component/show-semester-courses/show-semester-courses.component';
import { ModalDeleteComponent } from './component/modal-delete/modal-delete.component';


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
    ModalProgramChoiceComponent,
    ShowAllCourseLiteratureComponent,
    ModalDeleteCourseLiteratureComponent,
    AddCourseLiteratureComponent,
    ShowCourseLiteratureComponent,
    EditCourseLiteratureComponent,
    ShowCourseYearComponent,
    ShowStudyGroupsComponent,
    ModalDeleteStudyGroupComponent,
    AddStudyGroupComponent,
    ModalCourseTypeChoiceComponent,
    ShowYearStudentsComponent,
    ModalStudentDeleteComponent,
    ShowStudyGroupComponent,
    EditStudyGroupComponent,
    ShowAllGroupLiteratureComponent,
    ModalDeleteGroupLiteratureComponent,
    AddGroupLiteratureComponent,
    ShowGroupLiteratureComponent,
    EditGroupLiteratureComponent,
    ShowGroupStudentsComponent,
    ShowCourseTypesComponent,
    ModalDeleteCourseTypeComponent,
    AddCourseTypeComponent,
    EditCourseTypeComponent,
    ShowProgramsComponent,
    ModalProgramDeleteComponent,
    AddProgramComponent,
    ShowProgramComponent,
    EditProgramComponent,
    ShowProgramCoursesComponent,
    ShowInstitutesComponent,
    ModalDeleteInstituteComponent,
    AddInstituteComponent,
    ShowInstituteComponent,
    EditInstituteComponent,
    ShowInstituteCoursesComponent,
    ShowInstituteProgramsComponent,
    ShowInstituteChildrenComponent,
    ShowStudentComponent,
    AddStudentComponent,
    EditStudentComponent,
    AddTeacherComponent,
    EditTeacherComponent,
    EditTeacherJobComponent,
    ShowTeacherComponent,
    ModalTeacherDeleteComponent,
    ShowTeachersComponent,
    ShowUsersComponent,
    ModalUserDeleteComponent,
    ShowUserComponent,
    AddUserRoleComponent,
    ShowUserCoursesComponent,
    ChangePasswordComponent,
    NotFoundComponent,
    PaginationComponent,
    CourseSummaryComponent,
    ProgramSemestersComponent,
    ShowSemesterComponent,
    ModalDeleteSemesterComponent,
    AddSemesterComponent,
    EditSemesterComponent,
    ShowSemesterCoursesComponent,
    ModalDeleteComponent
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
