import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <div class="container">
      <h1>Patient MPI System</h1>
      <app-patient-list></app-patient-list>
    </div>
  `,
  styles: [`
    .container {
      padding: 20px;
      max-width: 1200px;
      margin: 0 auto;
    }
    h1 {
      color: #3f51b5;
      margin-bottom: 20px;
    }
  `]
})
export class AppComponent {
  title = 'Patient MPI System';
}

