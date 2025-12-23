import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import UrlInput from '../_models/url-input';
import { environment } from '../../environments/environment';
import { map, Observable } from 'rxjs';
import ShortUrl from '../_models/short-url';

@Injectable({
  providedIn: 'root',
})
export class LinkurtoService {
  private client: HttpClient = inject(HttpClient);
  private linkurto_url = environment.linkurto_proxy_server_url;

  shortenLongUrl(urlRequestBody: UrlInput): Observable<ShortUrl> {
    return this.client
      .post<ShortUrl>(this.linkurto_url + 'shorten', urlRequestBody)
      .pipe(
        map((response: ShortUrl) => {
          return response;
        }),
      );
  }

  shortUrlExists(shortUrl: string): Observable<ShortUrl> {
    return this.client
      .get<ShortUrl>(this.linkurto_url + 'exists/' + shortUrl)
      .pipe(
        map((response: ShortUrl) => {
          return response;
        }),
      );
  }
}
