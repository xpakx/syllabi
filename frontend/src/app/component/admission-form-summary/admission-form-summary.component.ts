import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AdmissionForm } from 'src/app/entity/admission-form';

@Component({
  selector: 'app-admission-form-summary',
  templateUrl: './admission-form-summary.component.html',
  styleUrls: ['./admission-form-summary.component.css']
})
export class AdmissionFormSummaryComponent  {
  @Input() form!: AdmissionForm;
  @Input() admin: boolean = false;
  @Output() deleteEvent = new EventEmitter<boolean>();

  constructor() { }

  delete(): void {
    this.deleteEvent.emit(true);
  }

}
