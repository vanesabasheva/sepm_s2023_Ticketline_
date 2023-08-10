import {Component, EventEmitter, Inject, Output} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DialogData} from '../../../services/cart.service';

@Component({
  selector: 'app-merchandise-event',
  templateUrl: './merchandise-event.component.html',
  styleUrls: ['./merchandise-event.component.scss']
})
export class MerchandiseEventComponent {
  @Output() merchEmitter = new EventEmitter<boolean>();
  constructor(
    public dialogRef: MatDialogRef<MerchandiseEventComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: DialogData
  ) {}
}
