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

@Component({
  selector: 'app-patient-list',
  templateUrl: './patient-list.component.html',
  styleUrls: ['./patient-list.component.scss']
})
export class PatientListComponent implements OnInit {
  displayedColumns: string[] = ['firstName', 'lastName', 'dob', 'gender', 'actions'];
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
            this.dataSource.data = page.content;
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
            this.dataSource.data = page.content;
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
    if (confirm(`Are you sure you want to delete ${patient.firstName} ${patient.lastName}?`)) {
      if (patient.id) {
        this.patientService.deletePatient(patient.id).subscribe({
          next: () => {
            this.snackBar.open('Patient deleted successfully', 'Close', { duration: 3000 });
            this.loadPatients(this.searchControl.value || '');
          },
          error: (error) => {
            this.snackBar.open('Error deleting patient', 'Close', { duration: 3000 });
            console.error(error);
          }
        });
      }
    }
  }
}

