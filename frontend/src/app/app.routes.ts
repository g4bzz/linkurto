import { Routes } from '@angular/router';
import { ShortenerComponent } from './pages/shortener/shortener.component';
import { ShortenedComponent } from './pages/shortened/shortened.component';
import { tokenGuard } from './guards/token.guard';
import { ResolveUrlComponent } from './pages/resolve-url/resolve-url.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';

export const routes: Routes = [
  {
    path: '',
    title: 'Linkurto',
    component: ShortenerComponent,
  },
  {
    path: 'shortened',
    title: 'URL shortened succesfully',
    component: ShortenedComponent,
    canActivate: [tokenGuard],
  },
  {
    path: '404',
    component: PageNotFoundComponent,
  },
  {
    path: ':shortUrl',
    component: ResolveUrlComponent,
  },
];
