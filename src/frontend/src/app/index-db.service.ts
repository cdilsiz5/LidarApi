import { Injectable } from '@angular/core';
import { openDB, IDBPDatabase, DBSchema } from 'idb';

interface Session {
  key: string;
  value: ArrayBuffer;
}

interface LidarDataSchema extends DBSchema {
  sessions: {
    value: Session;
    key: string;
    indexes: { 'by-session': string };
  };
}

@Injectable({
  providedIn: 'root'
})
export class IndexDbService {
  private db!: IDBPDatabase<LidarDataSchema>;

  constructor() {
    this.connectToDB();
  }

  async connectToDB() {
    this.db = await openDB<LidarDataSchema>('lidar-app-db', 1, {
      upgrade(db) {
        if (!db.objectStoreNames.contains('sessions')) {
          const store = db.createObjectStore('sessions', { keyPath: 'key' });
          store.createIndex('by-session', 'key');
        }
      }
    });
  }

  async saveSessionData(session: { sessionId: string, startGroupId: number, endGroupId: number, data: ArrayBuffer }) {
    const key = `${session.sessionId}-${session.startGroupId}-${session.endGroupId}`;
    return this.db.put('sessions', { key, value: session.data });
  }
  async isSessionImported(sessionId: string): Promise<boolean> {
    const session = await this.db.get('sessions', sessionId);
    return !!session;
  }

}
