import { Component, Inject, EventEmitter, Output, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DialogData } from '../../../services/cart.service';
import { AuthService } from '../../../services/auth.service';
import { UserService } from '../../../services/user.service';
import { ToastrService } from 'ngx-toastr';
import {
  CheckoutPaymentDetail,
  PaymentDetail,
} from '../../../dtos/payment-detail';

@Component({
  selector: 'app-create-payment-detail',
  templateUrl: './create-payment-detail.component.html',
  styleUrls: ['./create-payment-detail.component.scss'],
})
export class CreatePaymentDetailComponent implements OnInit {
  @Output() paymentDetailEmitter = new EventEmitter<CheckoutPaymentDetail>();
  paymentDetail: PaymentDetail = new PaymentDetail();
  constructor(
    private authService: AuthService,
    private userService: UserService,
    private notification: ToastrService,
    public dialogRef: MatDialogRef<CreatePaymentDetailComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: DialogData
  ) {}

  ngOnInit(): void {
    this.paymentDetail.userId = this.authService.getUserId();
  }

  submitForm() {
    this.userService
      .createPaymentDetail(this.paymentDetail.userId, this.paymentDetail)
      .subscribe({
        next: (paymentDetail) => {
          this.notification.success('Successfully created payment detail');
          this.paymentDetailEmitter.emit(paymentDetail);
        },
        error: (err) => {
          this.notification.error(
            err.error.detail,
            'Failed creating payment detail'
          );
        },
      });
  }
}
