import { Component } from '@angular/core';
import {PasswordChange} from '../../../dtos/passwordReset/passwordChange';
import {UserService} from '../../../services/user.service';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';
import {NgForm} from '@angular/forms';
import {AuthService} from '../../../services/auth.service';

@Component({
  selector: 'app-edit-password',
  templateUrl: './edit-password.component.html',
  styleUrls: ['./edit-password.component.scss']
})
export class EditPasswordComponent {
  data: PasswordChange = new PasswordChange();
  constructor(
    private service: UserService,
    private authService: AuthService,
    private notification: ToastrService,
    private router: Router
  ) {
  }
  onSubmit(form: NgForm): void {
    if (form.valid) {
      this.service.changePassword(this.data).subscribe({
        next: () => {
          this.notification.success('Successfully changed password');
          this.router.navigate(['/profile']);
        },
        error: error => {
          console.error('Could not change password', error);
          this.notification.error(error.error.detail,'Could not change password', {timeOut: 8000});
          if (error.error.detail === 'Old password is not correct. Conflicts: ' +
            'Your account has been locked due to too many failed attempts. ' +
            'Please contact the administrator to unlock your account.') {
            this.authService.logoutUser();
            this.router.navigate(['/login']);
          }
        }
      });
    }
  }
}
