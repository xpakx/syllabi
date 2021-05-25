import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddCourseLiteratureComponent } from './component/add-course-literature/add-course-literature.component';
import { AddCourseTypeComponent } from './component/add-course-type/add-course-type.component';
import { AddCourseYearComponent } from './component/add-course-year/add-course-year.component';
import { AddCourseComponent } from './component/add-course/add-course.component';
import { AddGroupLiteratureComponent } from './component/add-group-literature/add-group-literature.component';
import { AddProgramComponent } from './component/add-program/add-program.component';
import { AddStudyGroupComponent } from './component/add-study-group/add-study-group.component';
import { AllCoursesComponent } from './component/all-courses/all-courses.component';
import { EditCourseLiteratureComponent } from './component/edit-course-literature/edit-course-literature.component';
import { EditCourseTypeComponent } from './component/edit-course-type/edit-course-type.component';
import { EditCourseYearComponent } from './component/edit-course-year/edit-course-year.component';
import { EditCourseComponent } from './component/edit-course/edit-course.component';
import { EditGroupLiteratureComponent } from './component/edit-group-literature/edit-group-literature.component';
import { EditProgramComponent } from './component/edit-program/edit-program.component';
import { EditStudyGroupComponent } from './component/edit-study-group/edit-study-group.component';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { ShowAllCourseLiteratureComponent } from './component/show-all-course-literature/show-all-course-literature.component';
import { ShowAllGroupLiteratureComponent } from './component/show-all-group-literature/show-all-group-literature.component';
import { ShowCourseLiteratureComponent } from './component/show-course-literature/show-course-literature.component';
import { ShowCourseTypesComponent } from './component/show-course-types/show-course-types.component';
import { ShowCourseYearComponent } from './component/show-course-year/show-course-year.component';
import { ShowCourseYearsComponent } from './component/show-course-years/show-course-years.component';
import { ShowCourseComponent } from './component/show-course/show-course.component';
import { ShowGroupLiteratureComponent } from './component/show-group-literature/show-group-literature.component';
import { ShowGroupStudentsComponent } from './component/show-group-students/show-group-students.component';
import { ShowProgramCoursesComponent } from './component/show-program-courses/show-program-courses.component';
import { ShowProgramComponent } from './component/show-program/show-program.component';
import { ShowProgramsComponent } from './component/show-programs/show-programs.component';
import { ShowStudyGroupComponent } from './component/show-study-group/show-study-group.component';
import { ShowStudyGroupsComponent } from './component/show-study-groups/show-study-groups.component';
import { ShowYearStudentsComponent } from './component/show-year-students/show-year-students.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'courses',
    component: AllCoursesComponent
  },
  {
    path: 'courses/add',
    component: AddCourseComponent
  },
  {
    path: 'courses/:id',
    component: ShowCourseComponent
  },
  {
    path: 'courses/:id/years',
    component: ShowCourseYearsComponent
  },
  {
    path: 'courses/:id/years/add',
    component: AddCourseYearComponent
  },
  {
    path: 'years/:id/edit',
    component: EditCourseYearComponent
  },
  {
    path: 'courses/:id/edit',
    component: EditCourseComponent
  },
  {
    path: 'courses/:id/literature',
    component: ShowAllCourseLiteratureComponent
  },
  {
    path: 'courses/:id/literature/add',
    component: AddCourseLiteratureComponent
  },
  {
    path: 'courses/literature/:id',
    component: ShowCourseLiteratureComponent
  },
  {
    path: 'courses/literature/:id/edit',
    component: EditCourseLiteratureComponent
  },
  {
    path: 'years/:id',
    component: ShowCourseYearComponent
  },
  {
    path: 'years/:id/groups',
    component: ShowStudyGroupsComponent
  },
  {
    path: 'years/:id/groups/add',
    component: AddStudyGroupComponent
  },
  {
    path: 'years/:id/students',
    component: ShowYearStudentsComponent
  },
  {
    path: 'groups/:id',
    component: ShowStudyGroupComponent
  },
  {
    path: 'groups/:id/edit',
    component: EditStudyGroupComponent
  },
  {
    path: 'groups/:id/literature',
    component: ShowAllGroupLiteratureComponent
  },
  {
    path: 'groups/:id/literature/add',
    component: AddGroupLiteratureComponent
  },
  {
    path: 'groups/literature/:id',
    component: ShowGroupLiteratureComponent
  },
  {
    path: 'groups/literature/:id/edit',
    component: EditGroupLiteratureComponent
  },
  {
    path: 'groups/:id/students',
    component: ShowGroupStudentsComponent
  },
  {
    path: 'types',
    component: ShowCourseTypesComponent
  },
  {
    path: 'types/add',
    component: AddCourseTypeComponent
  },
  {
    path: 'types/:id/edit',
    component: EditCourseTypeComponent
  },
  {
    path: 'programs',
    component: ShowProgramsComponent
  },
  {
    path: 'programs/add',
    component: AddProgramComponent
  },
  {
    path: 'programs/:id',
    component: ShowProgramComponent
  },
  {
    path: 'programs/:id/edit',
    component: EditProgramComponent
  },
  {
    path: 'programs/:id/courses',
    component: ShowProgramCoursesComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
