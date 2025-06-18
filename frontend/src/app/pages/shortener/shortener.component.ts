import { Component, inject, OnInit } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { heroLinkSolid } from '@ng-icons/heroicons/solid';
import { NgxCaptchaModule } from 'ngx-captcha';
import { environment } from '../../../environments/environment';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { LinkurtoService } from '../../_services/linkurto.service';
import { ToastrService } from 'ngx-toastr';
import ShortUrl from '../../_models/short-url';
import { ShortUrlService } from '../../_services/short-url.service';
import { Router } from '@angular/router';
import ResponseError from '../../_models/response-error';

@Component({
  selector: 'app-shortener',
  standalone: true,
  imports: [NgIcon, ReactiveFormsModule, NgxCaptchaModule],
  providers: [provideIcons({ heroLinkSolid })],
  templateUrl: './shortener.component.html',
  styleUrl: './shortener.component.css',
})
export class ShortenerComponent implements OnInit {
  formShortener: FormGroup = new FormGroup({});
  toastr: ToastrService = inject(ToastrService);
  formBuilder: FormBuilder = inject(FormBuilder);
  shortUrlService: ShortUrlService = inject(ShortUrlService);
  linkurtoService: LinkurtoService = inject(LinkurtoService);
  router: Router = inject(Router);
  siteKey: string = environment.recaptcha_sitekey;

  ngOnInit(): void {
    this.initializeForm();
  }

  onSubmit() {
    if (this.formShortener.valid) {
      this.linkurtoService.shortenLongUrl(this.formShortener.value).subscribe({
        next: (response) => {
          this.getShortenedUrlFromResponse(response);
          this.router.navigate(['/shortened']);
        },
        error: (error) => {
          this.errorMessage(error.error);
        },
      });
    }
  }

  successMessage(message: string) {
    this.toastr.success(message);
  }

  errorMessage(error: ResponseError) {
    this.toastr.error(error.error, error.message);
  }

  getShortenedUrlFromResponse(shortUrl: ShortUrl) {
    this.shortUrlService.setShortUrl(shortUrl);
  }

  initializeForm() {
    this.formShortener = this.formBuilder.group({
      url: [
        '',
        [
          Validators.required,
          Validators.pattern(
            //validador de url
            '^(https?:\\/\\/)([a-zA-Z0-9\\-._~%]+)(:[0-9]{1,5})?(\\/[^\\s]*)?$',
          ),
        ],
      ],
      recaptchaToken: ['', [Validators.required]],
    });
  }
}
