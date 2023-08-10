import { Component, Inject, EventEmitter, Output } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DialogData } from '../../../services/cart.service';

@Component({
  selector: 'app-payment-detail',
  templateUrl: './payment-detail.component.html',
  styleUrls: ['./payment-detail.component.scss'],
})
export class PaymentDetailComponent {
  @Output() paymentSelector = new EventEmitter<number>();
  constructor(
    public dialogRef: MatDialogRef<PaymentDetailComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: DialogData
  ) {}
}
