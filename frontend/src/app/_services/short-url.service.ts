import { Injectable, signal, WritableSignal } from '@angular/core';
import ShortUrl from '../_models/short-url';

@Injectable({
  providedIn: 'root',
})
export class ShortUrlService {
  private shortUrl: WritableSignal<ShortUrl | undefined> = signal(undefined);

  getShortUrl(): ShortUrl | undefined {
    return this.shortUrl();
  }

  setShortUrl(shortUrl: ShortUrl): void {
    this.shortUrl.set(shortUrl);
  }

  clearShortUrl(): void {
    this.shortUrl.set(undefined);
  }
}
