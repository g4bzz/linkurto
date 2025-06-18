import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LinkurtoService } from '../../_services/linkurto.service';
import { environment } from '../../../environments/environment';
import ShortUrl from '../../_models/short-url';

@Component({
  selector: 'app-resolve-url',
  standalone: true,
  imports: [],
  templateUrl: './resolve-url.component.html',
  styleUrl: './resolve-url.component.css',
})
export class ResolveUrlComponent implements OnInit {
  private activatedRoute: ActivatedRoute = inject(ActivatedRoute);
  private linkurtoService: LinkurtoService = inject(LinkurtoService);
  redirectUrl: string = environment.linkurto_server_url;
  retrievedUrl: ShortUrl | undefined;
  shortUrl = '';

  getShortUrl() {
    this.activatedRoute.paramMap.subscribe((params) => {
      this.shortUrl =
        params.get('shortUrl') != null ? params.get('shortUrl')! : '';
      this.redirectUrl += this.shortUrl;
    });
  }

  ngOnInit(): void {
    this.getShortUrl();

    this.linkurtoService.shortUrlExists(this.shortUrl).subscribe({
      next: (response) => {
        this.retrievedUrl = response;
      },
    });
  }
}
