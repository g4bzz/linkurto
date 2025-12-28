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
import { ToastrService } from 'ngx-toastr';
import { environment } from '../../../environments/environment';
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
  toastr: ToastrService = inject(ToastrService);
  baseUrl: string = environment.linkurto_front_url;
  ngOnInit(): void {
    this.shortUrl = this.shortUrlService.getShortUrl();
  }

  //Garante que o método será executado quando os seguintes eventos do navegador forem executados:
  //  Page Refresh, Tab Close, Browser Close & Navigation Away From Page
  @HostListener('window:beforeunload')
  ngOnDestroy(): void {
    this.shortUrlService.clearShortUrl();
  }

  copyShortUrlToClipboard() {
    if (this.shortUrl) {
      const fullUrl = this.baseUrl + this.shortUrl.shortUrl;

      if (typeof navigator.clipboard !== 'undefined') {
        navigator.clipboard.writeText(fullUrl).then(() => {
          this.toastr.success('Url copied to clipboard successfully');
        }).catch(err => {
          this.fallbackCopyTextToClipboard(fullUrl);
        });
      } else {
        this.fallbackCopyTextToClipboard(fullUrl);
      }
    }
  }

  private fallbackCopyTextToClipboard(text: string) {
    const textArea = document.createElement("textarea");
    textArea.value = text;

    textArea.style.position = "fixed";
    textArea.style.left = "-9999px";
    textArea.style.top = "0";
    document.body.appendChild(textArea);

    textArea.focus();
    textArea.select();

    try {
      const successful = document.execCommand('copy');
      if (successful) {
        this.toastr.success('Url copied to clipboard successfully');
      } else {
        this.toastr.error('Unable to copy URL');
      }
    } catch (err) {
      this.toastr.error('Error copying URL');
    }

    document.body.removeChild(textArea);
  }

  getExpirationDateFormated() {
    let expirationDate: Date = new Date();

    if (this.shortUrl) {
      expirationDate = new Date(
        this.shortUrl.expirationDate.toString().replace('T', ' '),
      );
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
