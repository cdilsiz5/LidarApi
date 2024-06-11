import {Injectable, ɵɵinject} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SessionDto } from './models/session-dto.model';
import { MetadataDto } from './models/metadata-dto.model';

@Injectable({
  providedIn: 'root',
  useFactory: () => new LidarDataService(ɵɵinject(HttpClient)),
  deps: [HttpClient]
})
export class LidarDataService {
  private baseUrl = 'http://localhost:8080/api/v2/luxoft';

  constructor(private http: HttpClient) {}

  getSessions(): Observable<SessionDto[]> {
    return this.http.get<SessionDto[]>(`${this.baseUrl}/sessions`);
  }

  getMetadata(sessionId: string): Observable<MetadataDto> {
    return this.http.get<MetadataDto>(`${this.baseUrl}/metadata/${sessionId}`);
  }

  getBinaryData(sessionId: string, startGroupId: number, endGroupId: number): Observable<Uint8Array> {
    return this.http.get<Uint8Array>(`${this.baseUrl}/data/${sessionId}`, {
      params: { startGroupId, endGroupId },
      responseType: 'arraybuffer' as 'json'
    });
  }
}
