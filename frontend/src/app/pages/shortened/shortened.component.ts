import {
  Component,
  HostListener,
  inject,
  OnDestroy,
  OnInit,
} from '@angular/core';
import ShortUrl from '../../_models/short-url';
import { ShortUrlService } from '../../_services/short-url.service';
import { RouterLink } from '@angular/router';
@Component({
  selector: 'app-shortened',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './shortened.component.html',
  styleUrl: './shortened.component.css',
})
export class ShortenedComponent implements OnInit, OnDestroy {
  shortUrl: ShortUrl | undefined;
  shortUrlService: ShortUrlService = inject(ShortUrlService);

  ngOnInit(): void {
    this.shortUrl = this.shortUrlService.getShortUrl();

    //REMOVE

    // this.shortUrl = {
    //   expirationDate: new Date(2025, 7, 5),
    //   longUrl: 'https:github.com',
    //   shortUrl: 'https:linkurto.com/h2y37sgt',
    // };

    //this.shortUrl = undefined;
  }

  //Garante que o método será executado quando os seguintes eventos do navegador forem executados:
  //  Page Refresh, Tab Close, Browser Close & Navigation Away From Page
  @HostListener('window:beforeunload')
  ngOnDestroy(): void {
    this.shortUrlService.clearShortUrl();
  }

  copyShortUrlToClipboard() {
    if (this.shortUrl) {
      navigator.clipboard.writeText(this.shortUrl.shortUrl);
    }
  }
  getExpirationDateFormated() {
    let expirationDate: Date = new Date();

    if (this.shortUrl) {
      expirationDate = this.shortUrl.expirationDate;
    }

    return expirationDate.toLocaleString('en-CA', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      weekday: 'long',
      hour: '2-digit',
      hour12: false,
      minute: '2-digit',
      second: '2-digit',
    });
  }
}
