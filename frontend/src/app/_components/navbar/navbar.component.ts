import { Component } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { heroLinkSolid } from '@ng-icons/heroicons/solid';
import { bootstrapGithub, bootstrapLinkedin } from '@ng-icons/bootstrap-icons';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [NgIcon],
  providers: [
    provideIcons({ heroLinkSolid, bootstrapGithub, bootstrapLinkedin }),
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {}
