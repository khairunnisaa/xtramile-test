import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page } from '../../shared/models/page.model';
import { Patient } from '../../shared/models/patient.model';

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  private apiUrl = '/api/patients';

  constructor(private http: HttpClient) {}

  getPatients(page: number = 0, size: number = 20, sort: string = 'createdAt,desc'): Observable<Page<Patient>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    
    return this.http.get<Page<Patient>>(this.apiUrl, { params });
  }

  searchPatients(name?: string, pid?: string, page: number = 0, size: number = 20): Observable<Page<Patient>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    if (name) {
      params = params.set('name', name);
    }
    if (pid) {
      params = params.set('pid', pid);
    }
    
    return this.http.get<Page<Patient>>(`${this.apiUrl}/search`, { params });
  }

  getPatient(id: string): Observable<Patient> {
    return this.http.get<Patient>(`${this.apiUrl}/${id}`);
  }

  createPatient(patient: Patient): Observable<Patient> {
    return this.http.post<Patient>(this.apiUrl, patient);
  }

  updatePatient(id: string, patient: Patient): Observable<Patient> {
    return this.http.put<Patient>(`${this.apiUrl}/${id}`, patient);
  }

  deletePatient(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

