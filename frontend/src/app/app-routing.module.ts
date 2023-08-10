/* eslint-disable max-len */
import { Injectable, NgModule } from '@angular/core';
import { CanActivate, Router, RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard } from './guards/auth.guard';
import { NewsComponent } from './components/news/news.component';
import { PerformanceComponent } from './components/performance/performance.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { OrderHistoryComponent } from './components/order-history/order-history.component';
import { ReservationComponent } from './components/reservation/reservation.component';
import { CartComponent } from './components/cart/cart.component';
import { MerchandiseComponent } from './components/merchandise/merchandise.component';
import { RegisterComponent } from './components/register/register.component';
import { NewsDetailedComponent } from './components/news/news-detailed/news-detailed.component';
import { SearchEventByArtistComponent } from './components/search/search-event-by-artist/search-event-by-artist.component';
import { EventDetailsComponent } from './components/event-details/event-details.component';
import { ForgotPasswordComponent } from './components/login/forgot-password/forgot-password.component';
import { ResetComponent } from './components/reset/reset.component';
import { OrderDetailedComponent } from './components/order-detailed/order-detailed.component';
import { ProfileComponent } from './components/profile/profile.component';
import { EditProfileComponent } from './components/profile/edit-profile/edit-profile.component';
import { SearchPerformancesByLocationComponent } from './components/search/search-performances-by-location/search-performances-by-location.component';
import { PerformanceOnLocationComponent } from './components/performance/performance-on-location/performance-on-location.component';
import { TopTenComponent } from './components/top-ten/top-ten.component';
import { SearchEventsComponent } from './components/search/search-events/search-events.component';
import { AdminViewComponent } from './components/admin-view/admin-view.component';
import { AuthService } from './services/auth.service';
import { EditPasswordComponent } from './components/profile/edit-password/edit-password.component';
import { SearchPerformancesComponent } from './components/search/search-performances/search-performances.component';
import { EventComponent } from './components/event/event.component';

@Injectable({
  providedIn: 'root',
})
export class RoleGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    if (this.authService.getUserRole() === 'ADMIN') {
      return true;
    } else {
      this.router.navigate(['/']);
      return false;
    }
  }
}

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'edit-profile',canActivate: [AuthGuard], component: EditProfileComponent },
  { path: 'profile', canActivate: [AuthGuard], component: ProfileComponent },
  { path: 'edit-password', canActivate: [AuthGuard],component: EditPasswordComponent },
  { path: 'news', canActivate: [AuthGuard], component: NewsComponent },
  { path: 'news/:id', canActivate: [AuthGuard], component: NewsDetailedComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'reset/:token', component: ResetComponent },
  { path: 'performances/:id', canActivate: [AuthGuard],component: PerformanceComponent },
  { path: 'locations/:id/performances',canActivate: [AuthGuard], component: PerformanceOnLocationComponent},
  { path: 'reservations',canActivate: [AuthGuard], component: ReservationComponent },
  { path: 'order-history',canActivate: [AuthGuard],component: OrderHistoryComponent },
  { path: 'merchandise',canActivate: [AuthGuard], component: MerchandiseComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'cart',canActivate: [AuthGuard], component: CartComponent },
  { path: 'top-ten', canActivate: [AuthGuard],component: TopTenComponent},
  {
    path: 'search', children: [
      { path: 'artists', canActivate: [AuthGuard],component: SearchEventByArtistComponent },
      { path: 'locations', canActivate: [AuthGuard],component: SearchPerformancesByLocationComponent },
      { path: 'events',canActivate: [AuthGuard], component: SearchEventsComponent },
      { path: 'performances',canActivate: [AuthGuard], component: SearchPerformancesComponent}
    ],
  },
  { path: 'events/:id',canActivate: [AuthGuard], component: EventDetailsComponent },
  { path: 'orders/:id',canActivate: [AuthGuard], component: OrderDetailedComponent },
  { path: 'admin', component: AdminViewComponent, canActivate: [RoleGuard] },
  { path: 'admin/users/register', component: RegisterComponent, data: { isAdmin: true }, canActivate: [RoleGuard] },
  { path: 'admin/events/create', component: EventComponent, canActivate: [RoleGuard] },
  { path: '**', component: PageNotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
