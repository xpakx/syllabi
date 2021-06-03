import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Admission } from 'src/app/entity/admission';

@Component({
  selector: 'app-admission-summary',
  templateUrl: './admission-summary.component.html',
  styleUrls: ['./admission-summary.component.css']
})
export class AdmissionSummaryComponent {
  @Input() admission!: Admission;
  @Input() admin: boolean = false;
  @Output() deleteEvent = new EventEmitter<boolean>();

  constructor() { }

  delete(): void {
    this.deleteEvent.emit(true);
  }
}
