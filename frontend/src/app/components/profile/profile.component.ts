import {Component, OnInit} from '@angular/core';
import {UserProfile} from '../../dtos/user-profile';
import {UserService} from '../../services/user.service';
import {AuthService} from '../../services/auth.service';
import {ToastrService} from 'ngx-toastr';
import {MatDialog} from '@angular/material/dialog';
import {DeleteEventComponent} from './delete-event/delete-event.component';
import {CartService} from '../../services/cart.service';
import {CheckoutPaymentDetail} from '../../dtos/payment-detail';
import {CheckoutLocation} from '../../dtos/location';
import {CreateDeliveryLocationComponent} from '../cart/create-delivery-location/create-delivery-location.component';
import {CreatePaymentDetailComponent} from '../cart/create-payment-detail/create-payment-detail.component';
import {EditPaymentDetailComponent} from './edit-payment-detail/edit-payment-detail.component';
import {EditLocationsComponent} from './edit-locations/edit-locations.component';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit{
  user: UserProfile = new UserProfile();
  paymentDetails: CheckoutPaymentDetail[] = [];
  locations: CheckoutLocation[] = [];
  constructor(
    private service: UserService,
    private authService: AuthService,
    private notification: ToastrService,
    private dialog: MatDialog,
    private cartService: CartService,
  ) {
  }
  ngOnInit(): void {
    this.fetchUser();
    this.getCheckoutDetails();
  }
  fetchUser(): void {
    this.service.getUser(this.authService.getUserId()).subscribe({
      next: data => {
        this.user = data;
      },
      error: error => {
        console.error('Could not fetch user',error);
        this.notification.error('Could not fetch user', error.error.errors);
      }
    });
  }

  deleteUser(): void {
    this.service.deleteUser(this.authService.getUserId()).subscribe({
      next: () => {
        this.notification.success('Successfully deleted user');
        this.authService.logoutUser();
      },
      error: error => {
        console.error('Could not delete user', error);
        this.notification.error('Could not delete user', error.error.errors);
      }
    });
  }

  async deleteUserDialogDecision(): Promise<void> {
    const deleteUser = await this.openDeleteDialog();
    if (deleteUser) {
      this.deleteUser();
    }
  }

  openDeleteDialog(): Promise<boolean> {
    return new Promise((resolve, reject) => {
      const deleteRef = this.dialog.open(DeleteEventComponent, {
        width: '25%',
      });
      deleteRef.componentInstance.deleteEmitter.subscribe((deleteDialog: boolean) => {
        deleteRef.close();
        resolve(deleteDialog);
      });
    });
  }
  dateToLocaleDate(expirationDate: Date): string {
    return new Date(expirationDate).toLocaleDateString();
  }
    getCheckoutDetails(): void {
    this.cartService.getCheckoutDetails(this.authService.getUserId()).subscribe({
      next: data => {
        this.paymentDetails = data.paymentDetails;
        this.locations = data.locations;
      },
      error: error => {
        console.error('Could not fetch checkout details', error);
        this.notification.error('Could not fetch checkout details', error.error.errors);
      }
    });
  }
  async addPaymentDetail(): Promise<void> {
    const createdPayment = await this.openCreatePaymentDialog();
    this.paymentDetails.push(createdPayment);
  }

  openCreatePaymentDialog(): Promise<CheckoutPaymentDetail> {
    return new Promise((resolve, reject) => {
      const paymentRef = this.dialog.open(CreatePaymentDetailComponent, {
        width: '25%',
      });
      paymentRef.componentInstance.paymentDetailEmitter.subscribe(
          (paymentDetail: CheckoutPaymentDetail) => {
            paymentRef.close();
            resolve(paymentDetail);
          }
      );
    });
  }
    async addDeliveryLocation(): Promise<void> {
      const createdLocation = await this.openCreateLocationDialog();
      this.locations.push(createdLocation);
    }
    openCreateLocationDialog(): Promise<CheckoutLocation> {
    return new Promise((resolve, reject) => {
      const locationRef = this.dialog.open(CreateDeliveryLocationComponent, {
        width: '25%',
      });
      locationRef.componentInstance.locationEmitter.subscribe(
          (location: CheckoutLocation) => {
            locationRef.close();
            resolve(location);
          }
      );
    });
    }

    async editPaymentDetail(paymentD: CheckoutPaymentDetail): Promise<void> {
      await this.openEditPaymentDetail(paymentD);
      this.getCheckoutDetails();
    }

    openEditPaymentDetail(paymentD: CheckoutPaymentDetail): Promise<CheckoutPaymentDetail> {
      return new Promise((resolve, reject) => {
        const paymentRef = this.dialog.open(EditPaymentDetailComponent, {
          width: '25%',
          data: {paymentDetailNotUpdated: paymentD}
        });
        paymentRef.componentInstance.paymentDetailEditEmitter.subscribe(
            (paymentDetail: CheckoutPaymentDetail) => {
              paymentRef.close();
              resolve(paymentDetail);
            }
        );
      });
  }
  async editLocation(location: CheckoutLocation): Promise<void> {
    await this.openEditLocationDialog(location);
    this.getCheckoutDetails();
  }
  openEditLocationDialog(locationCheckout: CheckoutLocation): Promise<CheckoutLocation> {
    return new Promise((resolve, reject) => {
      const locationRef = this.dialog.open(EditLocationsComponent, {
        width: '25%',
        data: {locationNotUpdated: locationCheckout}
      });
      locationRef.componentInstance.locationEditEmitter.subscribe(
          (location: CheckoutLocation) => {
            locationRef.close();
            resolve(location);
          }
      );
    });
  }

}
