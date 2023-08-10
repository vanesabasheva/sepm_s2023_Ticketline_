import {Component, EventEmitter, Inject, Output} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DialogData} from '../../../services/cart.service';

@Component({
  selector: 'app-delete-event',
  templateUrl: './delete-event.component.html',
  styleUrls: ['./delete-event.component.scss']
})
export class DeleteEventComponent {
  @Output() deleteEmitter = new EventEmitter<boolean>();
  constructor(
      public dialogRef: MatDialogRef<DeleteEventComponent>,
      @Inject(MAT_DIALOG_DATA) public dialogData: DialogData
  ) {}
}
