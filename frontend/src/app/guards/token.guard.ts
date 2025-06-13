import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { ShortUrlService } from '../_services/short-url.service';

export const tokenGuard: CanActivateFn = () => {
  const router: Router = inject(Router);
  const shortUrlService: ShortUrlService = inject(ShortUrlService);

  if (shortUrlService.getShortUrl()) {
    return true;
  } else {
    return router.navigate(['']);
  }
  // REMOVE
  //return true;
};
