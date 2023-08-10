import {Component, EventEmitter, Inject, OnInit, Output} from '@angular/core';
import {CheckoutPaymentDetail, PaymentDetail} from '../../../dtos/payment-detail';
import {AuthService} from '../../../services/auth.service';
import {UserService} from '../../../services/user.service';
import {ToastrService} from 'ngx-toastr';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {CartService} from '../../../services/cart.service';

@Component({
  selector: 'app-edit-payment-detail',
  templateUrl: './edit-payment-detail.component.html',
  styleUrls: ['./edit-payment-detail.component.scss']
})
export class EditPaymentDetailComponent implements OnInit{
  @Output() paymentDetailEditEmitter = new EventEmitter<CheckoutPaymentDetail>();
  paymentDetail: CheckoutPaymentDetail = new CheckoutPaymentDetail();
  paymentDetailUpdated: PaymentDetail = new PaymentDetail();
  paymentDetails: CheckoutPaymentDetail[];
  cardHolderValue: string;
  cardNumberValue: string;
  cvvValue: string;
  expirationDateValue: Date;
  constructor(
      private authService: AuthService,
      private userService: UserService,
      private notification: ToastrService,
      private cartService: CartService,
      public dialogRef: MatDialogRef<EditPaymentDetailComponent>,
      @Inject(MAT_DIALOG_DATA) public data: {paymentDetailNotUpdated: CheckoutPaymentDetail}
  ) {
  }

  ngOnInit(): void {
    this.paymentDetailUpdated.userId = this.authService.getUserId();
    this.fetchPaymentDetail();
    this.paymentDetail = this.data.paymentDetailNotUpdated;
    this.cardHolderValue = this.paymentDetail.cardHolder;
    this.cardNumberValue = '•••• - •••• - •••• - ' + this.paymentDetail.lastFourDigits;
    this.cvvValue = '•••';
    this.expirationDateValue = this.paymentDetail.expirationDate;
  }
  fetchPaymentDetail(): void {
    this.cartService.getCheckoutDetails(this.paymentDetailUpdated.userId).subscribe({
      next: data => {
        this.paymentDetails = data.paymentDetails;
      },
      error: error => {
        console.error('Could not fetch payment details',error);
        this.notification.error('Could not fetch payment details', error.error.errors);
      }
    });
  }

    submitForm(): void {
    this.paymentDetailUpdated.id = this.paymentDetail.paymentDetailId;
    this.paymentDetailUpdated.cardHolder = this.cardHolderValue;
    this.paymentDetailUpdated.cardNumber = this.cardNumberValue;
    this.paymentDetailUpdated.cvv = Number(this.cvvValue);
    this.paymentDetailUpdated.expirationDate = this.expirationDateValue;
    this.userService
        .updatePaymentDetail(this.paymentDetailUpdated.userId, this.paymentDetailUpdated)
        .subscribe({
          next: (paymentDetail) => {
            this.notification.success('Successfully updated payment detail');
            this.paymentDetailEditEmitter.emit(paymentDetail);
          },
          error: (err) => {
            this.notification.error(
                err.error.detail,
                'Failed updating payment detail'
            );
          },
        });
    }
    deletePaymentDetail(): void {
        this.userService.deletePaymentDetail(this.paymentDetailUpdated.userId,this.paymentDetail.paymentDetailId).subscribe({
            next: () => {
            this.notification.success('Successfully deleted payment detail');
            this.paymentDetailEditEmitter.emit(null);
            },
            error: (err) => {
            this.notification.error(
                err.error.detail,
                'Failed deleting payment detail'
            );
            },
        });
    }
}
