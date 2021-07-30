import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-modal-add-weight',
  templateUrl: './modal-add-weight.component.html',
  styleUrls: ['./modal-add-weight.component.css']
})
export class ModalAddWeightComponent {
  form: FormGroup;
  
  constructor(private dialogRef: MatDialogRef<ModalAddWeightComponent>, private fb: FormBuilder) { 
    this.form = this.fb.group({
      name: ['', Validators.required],
      weight: ['', [Validators.required, Validators.pattern("^[0-9]*$")]]
    });
  }


  close(): void {
    if(!this.form.invalid) {
      this.dialogRef.close({name: this.form.controls.name.value, 
      weight: this.form.controls.weight.value});
    }
  }

  cancelAll() {
    this.dialogRef.close();
  }
}
