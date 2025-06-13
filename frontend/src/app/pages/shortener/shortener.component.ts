import { Component, inject, OnInit } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { heroLinkSolid } from '@ng-icons/heroicons/solid';
import { NgxCaptchaModule } from 'ngx-captcha';
import { environment } from '../../../environments/environment.development';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

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
  formBuilder: FormBuilder = inject(FormBuilder);
  siteKey: string = environment.recaptcha_sitekey;

  ngOnInit(): void {
    this.initializeForm();
  }

  onSubmit() {
    // TODO: implementar logica no backend + isso aqui:
    //se o form estiver valido, envia requisicao para o back
    //se der 200, grava o link curto na sessao e vai para a tela de resultado com o link
    //se der erro, exibe erro no toastr e reseta o form
    console.log(this.formShortener.value);
  }

  initializeForm() {
    this.formShortener = this.formBuilder.group({
      url_input: [
        '',
        [
          Validators.required,
          Validators.pattern(
            //validador de url
            '^(https?:\\/\\/)([a-zA-Z0-9\\-._~%]+)(:[0-9]{1,5})?(\\/[^\\s]*)?$',
          ),
        ],
      ],
      recaptcha: ['', [Validators.required]],
    });
  }
}
