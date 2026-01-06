import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { PatientService } from '../../../core/services/patient.service';
import { Patient } from '../../../shared/models/patient.model';
import { Page } from '../../../shared/models/page.model';
import { PatientFormComponent } from '../patient-form/patient-form.component';
import { DeleteConfirmationDialogComponent } from '../delete-confirmation-dialog/delete-confirmation-dialog.component';

@Component({
  selector: 'app-patient-list',
  templateUrl: './patient-list.component.html',
  styleUrls: ['./patient-list.component.scss']
})
export class PatientListComponent implements OnInit {
  displayedColumns: string[] = ['pid', 'firstName', 'lastName', 'dob', 'gender', 'actions'];
  dataSource = new MatTableDataSource<Patient>([]);
  
  searchControl = new FormControl('');
  
  totalElements = 0;
  pageSize = 20;
  pageIndex = 0;
  
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private patientService: PatientService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadPatients();
    
    this.searchControl.valueChanges
      .pipe(
        debounceTime(500),
        distinctUntilChanged()
      )
      .subscribe(value => {
        this.pageIndex = 0;
        this.loadPatients(value || '');
      });
  }

  loadPatients(searchTerm: string = ''): void {
    if (searchTerm) {
      this.patientService.searchPatients(searchTerm, undefined, this.pageIndex, this.pageSize)
        .subscribe({
          next: (page: Page<Patient>) => {
            this.dataSource.data = page.content.map(p => ({
              ...p,
              id: p.patientId || p.id
            }));
            this.totalElements = page.totalElements;
          },
          error: (error) => {
            this.snackBar.open('Error loading patients', 'Close', { duration: 3000 });
            console.error(error);
          }
        });
    } else {
      this.patientService.getPatients(this.pageIndex, this.pageSize)
        .subscribe({
          next: (page: Page<Patient>) => {
            this.dataSource.data = page.content.map(p => ({
              ...p,
              id: p.patientId || p.id
            }));
            this.totalElements = page.totalElements;
          },
          error: (error) => {
            this.snackBar.open('Error loading patients', 'Close', { duration: 3000 });
            console.error(error);
          }
        });
    }
  }

  onPageChange(event: PageEvent): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadPatients(this.searchControl.value || '');
  }

  openAddDialog(): void {
    const dialogRef = this.dialog.open(PatientFormComponent, {
      width: '600px',
      data: { patient: null }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadPatients(this.searchControl.value || '');
      }
    });
  }

  openEditDialog(patient: Patient): void {
    const dialogRef = this.dialog.open(PatientFormComponent, {
      width: '600px',
      data: { patient }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadPatients(this.searchControl.value || '');
      }
    });
  }

  deletePatient(patient: Patient): void {
    const patientName = `${patient.firstName} ${patient.lastName}`;
    const dialogRef = this.dialog.open(DeleteConfirmationDialogComponent, {
      width: '400px',
      data: { patientName }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const patientId = patient.patientId || patient.id;
        if (patientId) {
          this.patientService.deletePatient(patientId).subscribe({
            next: () => {
              this.snackBar.open('Patient deleted successfully', 'Close', { duration: 3000 });
              this.loadPatients(this.searchControl.value || '');
            },
            error: (error) => {
              let errorMessage = 'Error deleting patient';
              if (error.error && error.error.message) {
                errorMessage = error.error.message;
              } else if (error.message) {
                errorMessage = error.message;
              }
              this.snackBar.open(errorMessage, 'Close', { duration: 5000 });
              console.error('Error deleting patient:', error);
            }
          });
        }
      }
    });
  }

  formatDate(date: string | undefined): string {
    if (!date) return '-';
    try {
      const d = new Date(date);
      return d.toLocaleDateString('en-AU', { year: 'numeric', month: '2-digit', day: '2-digit' });
    } catch {
      return date;
    }
  }

  formatGender(gender: string | undefined): string {
    if (!gender) return '-';
    return gender.charAt(0) + gender.slice(1).toLowerCase();
  }
}

