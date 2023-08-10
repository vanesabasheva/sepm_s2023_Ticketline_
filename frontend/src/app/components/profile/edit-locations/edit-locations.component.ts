import {Component, EventEmitter, Inject, Output} from '@angular/core';
import {AuthService} from '../../../services/auth.service';
import {UserService} from '../../../services/user.service';
import {ToastrService} from 'ngx-toastr';
import {CartService} from '../../../services/cart.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {CheckoutLocation} from '../../../dtos/location';

@Component({
  selector: 'app-edit-locations',
  templateUrl: './edit-locations.component.html',
  styleUrls: ['./edit-locations.component.scss']
})
export class EditLocationsComponent {
  @Output() locationEditEmitter = new EventEmitter<CheckoutLocation>();
  locations: CheckoutLocation[];
  location: CheckoutLocation;
  userId: number;
  constructor(
      private authService: AuthService,
      private userService: UserService,
      private notification: ToastrService,
      private cartService: CartService,
      public dialogRef: MatDialogRef<EditLocationsComponent>,
      @Inject(MAT_DIALOG_DATA) public data: {locationNotUpdated: CheckoutLocation}
  ) {
    this.userId = this.authService.getUserId();
    this.fetchLocations();
    this.location = this.data.locationNotUpdated;
  }

  fetchLocations(): void {
    this.cartService.getCheckoutDetails(this.userId).subscribe({
      next: data => {
        this.locations = data.locations;
      },
      error: error => {
        console.error('Could not fetch locations',error);
        this.notification.error(error.error.detail,'Could not fetch locations');
      }
    });
  }

    submitForm(): void {
    this.userService.updateLocation(this.userId, this.location).subscribe({
        next: (location) => {
            this.notification.success('Location updated successfully');
            this.locationEditEmitter.emit(location);
        },
      error: (err) => {
        this.notification.error(
            err.error.detail,
            'Failed updating payment detail'
        );
      },
    });
}
  deleteLocation(): void {
    this.userService.deleteLocation(this.userId, this.location.locationId).subscribe({
      next: () => {
        this.notification.success('Location deleted successfully');
        this.locationEditEmitter.emit(null);
      },
      error: error => {
        console.error('Could not delete location',error);
        this.notification.error(error.error.detail,'Could not delete location');
      }
    });
  }
}
