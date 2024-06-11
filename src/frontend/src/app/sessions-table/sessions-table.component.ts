import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { LidarDataService } from '../lidar-data/lidar-data.service';
import { IndexDbService } from '../index-db.service';
import { SessionDto } from '../lidar-data/models/session-dto.model';

@Component({
  selector: 'app-sessions-table',
  standalone: true,
  imports: [CommonModule, MatTableModule],
  templateUrl: './sessions-table.component.html',
  styleUrls: ['./sessions-table.component.css']
})
export class SessionsTableComponent implements OnInit {
  dataSource = new MatTableDataSource<SessionDto>();

  constructor(
    private lidarDataService: LidarDataService,
    private indexDbService: IndexDbService
  ) {}

  ngOnInit() {
    this.loadSessions();
  }

  loadSessions() {
    this.lidarDataService.getSessions().subscribe(async (sessions: SessionDto[]) => {
      for (const session of sessions) {
        session.imported = await this.indexDbService.isSessionImported(session.sessionId);
      }
      this.dataSource.data = sessions;
    }, error => {
      console.error('Error fetching sessions:', error);
    });
  }

  fetchSessionData(sessionId: string) {
    this.lidarDataService.getMetadata(sessionId).subscribe(metadata => {
      this.lidarDataService.getBinaryData(sessionId, metadata.startGroupId, metadata.groups - 1).subscribe(async (data: ArrayBuffer) => {
        const session = { key: sessionId, value: data };
        try {
          await this.indexDbService.saveSessionData(session);
          console.log('Data saved to IndexedDB for', sessionId);
          this.updateSessionImportedStatus(sessionId, true);
        } catch (err) {
          console.error('Error saving data to IndexedDB:', err);
        }
      }, error => {
        console.error('Error fetching binary data:', error);
      });
    }, error => {
      console.error('Error fetching metadata:', error);
    });
  }

  private updateSessionImportedStatus(sessionId: string, imported: boolean) {
    const session = this.dataSource.data.find(s => s.sessionId === sessionId);
    if (session) {
      session.imported = imported;
      this.dataSource._updateChangeSubscription();
    }
  }
}
