import { Injectable, NgZone } from '@angular/core';
import { Observable, Subscriber } from 'rxjs';
import { API_URL } from 'src/app/constants/app.constants';
import { EventSourcePolyfill } from 'event-source-polyfill';


@Injectable({
    providedIn: 'root'
})
export class EventSourceService {
    readonly API_URL = API_URL.DEV;
    private eventSource: EventSource | null = null;

    constructor(private zone: NgZone) { }

    connectToServerSentEvents(url: string, options: EventSourceInit): Observable<MessageEvent> {
        return new Observable<MessageEvent>((observer) => {
            const eventSource = new EventSourcePolyfill(`${this.API_URL}${url}`, options);

            eventSource.onmessage = (event: MessageEvent) => {
                observer.next(event);
            };

            eventSource.onerror = (error) => {
                observer.error(error);
                eventSource.close();
            };

            return () => {
                eventSource.close();
            };
        });
    }

    /**
     * Ferme la connexion SSE
     */
    close(): void {
        if (this.eventSource) {
            this.eventSource.close();
            this.eventSource = null;
        }
    }
}