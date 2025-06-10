import { Component } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { heroLinkSolid } from '@ng-icons/heroicons/solid';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [NgIcon],
  providers: [provideIcons({ heroLinkSolid })],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css',
})
export class FooterComponent {}
