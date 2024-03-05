import { CanActivateFn, Router } from '@angular/router';
import {inject} from "@angular/core";

export const authGuardGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  if (!localStorage.getItem('token')) {
    router.navigate(['login']);
    return false;
  }
  return true;
};
