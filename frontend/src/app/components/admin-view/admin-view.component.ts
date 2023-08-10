import {Component, OnInit} from '@angular/core';
import {User} from '../../dtos/user';
import {AdminService} from '../../services/admin.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-admin-view',
  templateUrl: './admin-view.component.html',
  styleUrls: ['./admin-view.component.scss']
})
export class AdminViewComponent implements OnInit {
  users: User[];
  locked: boolean;
  constructor(private service: AdminService, private notification: ToastrService) {
    this.locked = false;
  }
  ngOnInit(): void {
    this.loadUsers(this.locked);
  }
  loadUsers(locked: boolean): void {
    this.service.getAdminUsers(locked).subscribe({
      next: (data) => {
        this.users = data;
      }
    });
  }
  resetPassword(id: number): void {
    this.service.resetPassword(id).subscribe({
      next: () => {
        this.notification.success('Password reset for user with ID ' + id);
      },
      error: (error) => {
        console.error('Could not reset password', error);
        this.notification.error(error.error.detail);
      }
    });
  }
  lockUser(id: number): void {
    this.service.lockUser(id).subscribe({
      next: () => {
        this.loadUsers(this.locked);
        this.notification.success('User with ID ' + id + ' successfully locked');
      },
      error: (error) => {
        console.error('Could not lock user', error);
        this.notification.error(error.error.detail);
      }
    });
  }
  unlockUser(id: number): void {
    this.service.unlockUser(id).subscribe({
      next: () => {
        this.loadUsers(this.locked);
        this.notification.success('User with ID ' + id + ' successfully unlocked');
      },
      error: (error) => {
        console.error('Could not unlock user', error);
        this.notification.error(error.error.detail);
      }
    });
  }
  toggleLocked(): void {
    this.locked = !this.locked;
    this.loadUsers(this.locked);
  }
}
