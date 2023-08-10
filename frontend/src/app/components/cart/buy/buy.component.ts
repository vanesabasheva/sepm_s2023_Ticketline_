import { Component, Output, EventEmitter, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DialogData } from '../../../services/cart.service';

@Component({
  selector: 'app-buy',
  templateUrl: './buy.component.html',
  styleUrls: ['./buy.component.scss'],
})
export class BuyComponent {
  @Output() buyEmitter = new EventEmitter<boolean>();
  constructor(
    public dialogRef: MatDialogRef<BuyComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: DialogData
  ) {}
}
