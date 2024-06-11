import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SessionsTableComponent } from './sessions-table/sessions-table.component';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule,SessionsTableComponent],
  template: `<app-sessions-table></app-sessions-table>
  `,
})
export class AppComponent {}
