import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddCourseComponent } from './component/add-course/add-course.component';
import { AllCoursesComponent } from './component/all-courses/all-courses.component';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { ShowCourseYearsComponent } from './component/show-course-years/show-course-years.component';
import { ShowCourseComponent } from './component/show-course/show-course.component';

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
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
