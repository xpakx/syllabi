import { Component, Input, EventEmitter, Output } from '@angular/core';
import { CourseForPage } from 'src/app/entity/course-for-page';

@Component({
  selector: 'app-course-summary',
  templateUrl: './course-summary.component.html',
  styleUrls: ['./course-summary.component.css']
})
export class CourseSummaryComponent {
  @Input() course!: CourseForPage;
  @Input() admin: boolean = false;
  @Output() deleteEvent = new EventEmitter<boolean>();

  constructor() { }

  delete(): void {
    this.deleteEvent.emit(true);
  }
}
