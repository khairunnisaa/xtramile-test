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
      pid: [{value: '', disabled: true}],
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
      const dob = this.data.patient.dateOfBirth || this.data.patient.dob;
      this.patientForm.patchValue({
        pid: this.data.patient.pid || '',
        firstName: this.data.patient.firstName,
        lastName: this.data.patient.lastName,
        dateOfBirth: dob ? new Date(dob) : null,
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
      
      if (this.isEditMode && this.data.patient?.pid) {
        patient.pid = this.data.patient.pid;
      }

      if (this.isEditMode && this.data.patient) {
        const patientId = this.data.patient.patientId || this.data.patient.id;
        if (!patientId) {
          console.error('Patient ID not found:', this.data.patient);
          this.snackBar.open('Patient ID not found', 'Close', { duration: 3000 });
          return;
        }
        console.log('Updating patient with ID:', patientId, 'Data:', patient);
        this.patientService.updatePatient(patientId, patient).subscribe({
          next: () => {
            this.snackBar.open('Patient updated successfully', 'Close', { duration: 3000 });
            this.dialogRef.close(true);
          },
          error: (error) => {
            let errorMessage = 'Error updating patient';
            if (error.error && error.error.message) {
              errorMessage = error.error.message;
            } else if (error.message) {
              errorMessage = error.message;
            }
            this.snackBar.open(errorMessage, 'Close', { duration: 5000 });
            console.error('Error updating patient:', error);
          }
        });
      } else {
        this.patientService.createPatient(patient).subscribe({
          next: () => {
            this.snackBar.open('Patient created successfully', 'Close', { duration: 3000 });
            this.dialogRef.close(true);
          },
          error: (error) => {
            let errorMessage = 'Error creating patient';
            if (error.error && error.error.message) {
              errorMessage = error.error.message;
            } else if (error.message) {
              errorMessage = error.message;
            }
            this.snackBar.open(errorMessage, 'Close', { duration: 5000 });
            console.error('Error creating patient:', error);
          }
        });
      }
    }
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}

