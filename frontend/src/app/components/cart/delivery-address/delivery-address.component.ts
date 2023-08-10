import { Component, Inject, Output, EventEmitter } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DialogData } from '../../../services/cart.service';

@Component({
  selector: 'app-delivery-address',
  templateUrl: './delivery-address.component.html',
  styleUrls: ['./delivery-address.component.scss'],
})
export class DeliveryAddressComponent {
  @Output() locationSelector = new EventEmitter<number>();
  constructor(
    public dialogRef: MatDialogRef<DeliveryAddressComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: DialogData
  ) {}
}
