import {Component, OnInit} from '@angular/core';
import {UserProfile} from '../../../dtos/user-profile';
import {UserService} from '../../../services/user.service';
import {AuthService} from '../../../services/auth.service';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute, Router} from '@angular/router';
import {NgForm} from '@angular/forms';
import {DeleteEventComponent} from '../delete-event/delete-event.component';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.scss']
})
export class EditProfileComponent implements OnInit{
  user: UserProfile = new UserProfile();

  constructor(
    private service: UserService,
    private authService: AuthService,
    private notification: ToastrService,
    private router: Router,
    private route: ActivatedRoute,
    public dialog: MatDialog
  ) {
  }
  ngOnInit(): void {
    this.fetchUser();
  }
  fetchUser(): void {
    this.service.getUser(this.authService.getUserId()).subscribe({
      next: data => {
        this.user = data;
      },
      error: error => {
        console.error('Could not fetch user',error);
        this.notification.error(error.error.detail,'Could not fetch user');
      }
    });
  }
  onSubmit(form: NgForm): void {
    if (form.valid) {
      this.service.editUser(this.authService.getUserId(), this.user).subscribe({
        next: () => {
          this.notification.success('Successfully updated user');
          this.router.navigate(['/profile'], {relativeTo: this.route});
        },
        error: error => {
          console.error('Could not update user', error);
          this.notification.error(error.error.detail,'Could not update user');
        }
      });
    }
  }
  deleteUser(): void {
      this.service.deleteUser(this.authService.getUserId()).subscribe({
        next: () => {
          this.notification.success('Successfully deleted user');
          this.authService.logoutUser();
        },
        error: error => {
          console.error('Could not delete user', error);
          this.notification.error(error.error.detail,'Could not delete user');
        }
      });
  }

  async deleteUserDialogDecision(): Promise<void> {
    const deleteUser = await this.openDeleteDialog();
    if (deleteUser) {
      this.deleteUser();
    }
  }

  openDeleteDialog(): Promise<boolean> {
    return new Promise((resolve, reject) => {
      const deleteRef = this.dialog.open(DeleteEventComponent, {
        width: '25%',
      });
      deleteRef.componentInstance.deleteEmitter.subscribe((deleteDialog: boolean) => {
        deleteRef.close();
        resolve(deleteDialog);
      });
    });
  }


}
