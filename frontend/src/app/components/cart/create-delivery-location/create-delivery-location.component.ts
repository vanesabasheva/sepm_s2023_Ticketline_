import { Component, Inject, EventEmitter, Output, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DialogData } from '../../../services/cart.service';
import { AuthService } from '../../../services/auth.service';
import { UserService } from '../../../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { CheckoutLocation, Location } from '../../../dtos/location';

@Component({
  selector: 'app-create-delivery-location',
  templateUrl: './create-delivery-location.component.html',
  styleUrls: ['./create-delivery-location.component.scss'],
})
export class CreateDeliveryLocationComponent {
  @Output() locationEmitter = new EventEmitter<CheckoutLocation>();
  location: Location = new Location();
  constructor(
    private authService: AuthService,
    private userService: UserService,
    private notification: ToastrService,
    public dialogRef: MatDialogRef<CreateDeliveryLocationComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: DialogData
  ) {}

  submitForm() {
    this.userService
      .createLocation(this.authService.getUserId(), this.location)
      .subscribe({
        next: (location) => {
          this.notification.success('Successfully created delivery address');
          this.locationEmitter.emit(location);
        },
        error: (err) => {
          this.notification.error(
            err.error.detail,
            'Failed creating delivery address'
          );
        },
      });
  }
}
