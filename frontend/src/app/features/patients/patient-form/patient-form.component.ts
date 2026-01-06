import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PatientService } from '../../../core/services/patient.service';
import { Patient } from '../../../shared/models/patient.model';

@Component({
  selector: 'app-patient-form',
  templateUrl: './patient-form.component.html',
  styleUrls: ['./patient-form.component.scss']
})
export class PatientFormComponent implements OnInit {
  patientForm: FormGroup;
  isEditMode = false;

  constructor(
    private fb: FormBuilder,
    private patientService: PatientService,
    private dialogRef: MatDialogRef<PatientFormComponent>,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: { patient: Patient | null }
  ) {
    this.patientForm = this.fb.group({
      pid: [''],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      dateOfBirth: ['', Validators.required],
      gender: [''],
      phone: ['', Validators.required],
      address: this.fb.group({
        address: [''],
        suburb: [''],
        state: [''],
        postcode: ['']
      })
    });
  }

  ngOnInit(): void {
    if (this.data.patient) {
      this.isEditMode = true;
      this.patientForm.patchValue({
        pid: this.data.patient.pid || '',
        firstName: this.data.patient.firstName,
        lastName: this.data.patient.lastName,
        dateOfBirth: this.data.patient.dateOfBirth || this.data.patient.dob,
        gender: this.data.patient.gender || '',
        phone: this.data.patient.phone || '',
        address: this.data.patient.address || {
          address: '',
          suburb: '',
          state: '',
          postcode: ''
        }
      });
    }
  }

  onSubmit(): void {
    if (this.patientForm.valid) {
      const formValue = this.patientForm.value;
      const patient: any = {
        pid: formValue.pid || null,
        firstName: formValue.firstName,
        lastName: formValue.lastName,
        dateOfBirth: formValue.dateOfBirth instanceof Date 
          ? formValue.dateOfBirth.toISOString().split('T')[0] 
          : formValue.dateOfBirth,
        gender: formValue.gender || null,
        phone: formValue.phone || null,
        email: null,
        address: formValue.address && (formValue.address.address || formValue.address.suburb || formValue.address.state || formValue.address.postcode)
          ? formValue.address
          : null
      };

      if (this.isEditMode && this.data.patient?.id) {
        this.patientService.updatePatient(this.data.patient.id, patient).subscribe({
          next: () => {
            this.snackBar.open('Patient updated successfully', 'Close', { duration: 3000 });
            this.dialogRef.close(true);
          },
          error: (error) => {
            this.snackBar.open('Error updating patient', 'Close', { duration: 3000 });
            console.error(error);
          }
        });
      } else {
        this.patientService.createPatient(patient).subscribe({
          next: () => {
            this.snackBar.open('Patient created successfully', 'Close', { duration: 3000 });
            this.dialogRef.close(true);
          },
          error: (error) => {
            this.snackBar.open('Error creating patient', 'Close', { duration: 3000 });
            console.error(error);
          }
        });
      }
    }
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}

