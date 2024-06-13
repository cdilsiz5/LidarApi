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
      const totalGroups = metadata.groups;
      const maxGroupsPerRequest = 20;
      const numRequests = Math.ceil(totalGroups / maxGroupsPerRequest);
      let completedRequests = 0;

      for (let i = 0; i < numRequests; i++) {
        const startGroupId = i * maxGroupsPerRequest;
        const endGroupId = Math.min((i + 1) * maxGroupsPerRequest - 1, totalGroups - 1);

        const worker = new Worker('./assets/fetchData.worker.js', { type: 'module' });
        worker.postMessage({
          sessionId: sessionId,
          startGroupId: startGroupId,
          endGroupId: endGroupId,
          apiUrl: 'http://localhost:8080/api/v2/luxoft'
        });

        worker.onmessage = (event) => {
          const { data, sessionId, startGroupId, endGroupId } = event.data;
          this.indexDbService.saveSessionData({
            sessionId,
            startGroupId,
            endGroupId,
            data
          }).then(() => {
            console.log(`Data saved to IndexedDB for ${sessionId} from group ${startGroupId} to ${endGroupId}`);
            completedRequests++;
            if (completedRequests === numRequests) {
              this.updateSessionImportedStatus(sessionId);
            }
          }).catch(err => {
            console.error('Error saving data to IndexedDB:', err);
          });
          worker.terminate();
        };

        worker.onerror = (error) => {
          console.error('Error in worker:', error);
        };
      }
    }, error => {
      console.error('Error fetching metadata:', error);
    });
  }



  private updateSessionImportedStatus(sessionId: string) {
    const sessionIndex = this.dataSource.data.findIndex(s => s.sessionId === sessionId);
    if (sessionIndex !== -1) {
      this.dataSource.data[sessionIndex].imported = true;
      this.dataSource._updateChangeSubscription();
    }
  }

}
