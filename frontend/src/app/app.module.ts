/* eslint-disable max-len */
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { NewsComponent } from './components/news/news.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { httpInterceptorProviders } from './interceptors';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PerformanceComponent } from './components/performance/performance.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { CartComponent } from './components/cart/cart.component';
import { ReservationComponent } from './components/reservation/reservation.component';
import { OrderHistoryComponent } from './components/order-history/order-history.component';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { PaymentDetailComponent } from './components/cart/payment-detail/payment-detail.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatTableModule } from '@angular/material/table';
import { DeliveryAddressComponent } from './components/cart/delivery-address/delivery-address.component';
import { BuyComponent } from './components/cart/buy/buy.component';
import { MerchandiseComponent } from './components/merchandise/merchandise.component';
import { CookieService } from 'ngx-cookie-service';
import { MerchandiseEventComponent } from './components/merchandise/merchandise-event/merchandise-event.component';
import { RegisterComponent } from './components/register/register.component';
import { CreatePaymentDetailComponent } from './components/cart/create-payment-detail/create-payment-detail.component';
import { CreateDeliveryLocationComponent } from './components/cart/create-delivery-location/create-delivery-location.component';
import { SearchEventByArtistComponent } from './components/search/search-event-by-artist/search-event-by-artist.component';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { EventDetailsComponent } from './components/event-details/event-details.component';
import { OrderDetailedComponent } from './components/order-detailed/order-detailed.component';
import { NewsDetailedComponent } from './components/news/news-detailed/news-detailed.component';
import { ForgotPasswordComponent } from './components/login/forgot-password/forgot-password.component';
import { ResetComponent } from './components/reset/reset.component';
import { ProfileComponent } from './components/profile/profile.component';
import { EditProfileComponent } from './components/profile/edit-profile/edit-profile.component';
import { DeleteEventComponent } from './components/profile/delete-event/delete-event.component';
import { EditPaymentDetailComponent } from './components/profile/edit-payment-detail/edit-payment-detail.component';
import { EditLocationsComponent } from './components/profile/edit-locations/edit-locations.component';
import { SearchPerformancesByLocationComponent } from './components/search/search-performances-by-location/search-performances-by-location.component';
import { PerformanceOnLocationComponent } from './components/performance/performance-on-location/performance-on-location.component';
import { AdminViewComponent } from './components/admin-view/admin-view.component';
import { TopTenComponent } from './components/top-ten/top-ten.component';
import { TopTenListComponent } from './components/top-ten/top-ten-list/top-ten-list.component';
import { SearchEventsComponent } from './components/search/search-events/search-events.component';
import { HighchartsChartModule } from 'highcharts-angular';
import { EditPasswordComponent } from './components/profile/edit-password/edit-password.component';
import { SearchPerformancesComponent } from './components/search/search-performances/search-performances.component';
import { EventComponent } from './components/event/event.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    NewsComponent,
    PerformanceComponent,
    PageNotFoundComponent,
    CartComponent,
    PaymentDetailComponent,
    ReservationComponent,
    OrderHistoryComponent,
    DeliveryAddressComponent,
    BuyComponent,
    MerchandiseComponent,
    MerchandiseComponent,
    MerchandiseEventComponent,
    RegisterComponent,
    NewsDetailedComponent,
    CreatePaymentDetailComponent,
    CreateDeliveryLocationComponent,
    SearchEventByArtistComponent,
    EventDetailsComponent,
    OrderDetailedComponent,
    ForgotPasswordComponent,
    ResetComponent,
    ProfileComponent,
    EditProfileComponent,
    DeleteEventComponent,
    EditPaymentDetailComponent,
    EditLocationsComponent,
    SearchPerformancesByLocationComponent,
    PerformanceOnLocationComponent,
    TopTenComponent,
    TopTenListComponent,
    SearchEventsComponent,
    AdminViewComponent,
    EditPasswordComponent,
    SearchPerformancesComponent,
    EventComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    //Needed for Toastr
    ToastrModule.forRoot({
      timeOut: 3000,
      extendedTimeOut: 1000,
      closeButton: true,
      progressBar: true,
      tapToDismiss: false,
    }),
    BrowserAnimationsModule,
    ReactiveFormsModule,
    //-------
    NgbModule,
    FormsModule,
    //Needed for MatDialog
    MatDialogModule,
    MatTableModule,
    MatCardModule,
    MatButtonModule,
    MatInputModule,
    MatIconModule,
    MatToolbarModule,
    MatAutocompleteModule,
    MatButtonToggleModule,
    HighchartsChartModule,
  ],
  providers: [httpInterceptorProviders, CookieService],
  bootstrap: [AppComponent],
})
export class AppModule {}
