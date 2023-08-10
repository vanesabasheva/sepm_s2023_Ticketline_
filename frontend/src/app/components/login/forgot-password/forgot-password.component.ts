import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators, ɵFormGroupValue, ɵTypedOrUntyped} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {AuthService} from '../../../services/auth.service';
import {PasswordResetRequest} from '../../../dtos/passwordReset/passwordResetRequest';
import {HttpStatusCode} from '@angular/common/http';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {
  formData: FormGroup;

  constructor(
    private builder: FormBuilder,
    private notification: ToastrService,
    private authService: AuthService,
  ) {
  }

  ngOnInit() {
    this.formData = this.builder.group({
      email: new FormControl('', [Validators.compose([Validators.required, Validators.email])]),
    });
  }

  onSubmit(value: ɵTypedOrUntyped<any, ɵFormGroupValue<any>, any>) {
    const req: PasswordResetRequest = {email: value.email};
    this.authService.sendResetPasswordEmail(req).subscribe({
      next: () => {
        this.notification.success('Password reset link sent to ' + value.email + ', if user exists');

      }, error: error => {
        if (error.status === HttpStatusCode.UnprocessableEntity) {
          this.notification.error('Invalid Password');
        } else if (error.status >= 400) {
          this.notification.error('Something went wrong');
        }
      }
    });
  }

}
