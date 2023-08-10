import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators, ɵFormGroupValue, ɵTypedOrUntyped} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {AuthService} from '../../services/auth.service';
import {PasswordUpdate} from '../../dtos/passwordReset/passwordUpdate';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpStatusCode} from '@angular/common/http';

@Component({
  selector: 'app-reset',
  templateUrl: './reset.component.html',
  styleUrls: ['./reset.component.scss']
})
export class ResetComponent implements OnInit {
  formData: FormGroup;
  submitted = false;
  passwordVisible = false;
  token: string;

  constructor(
    private builder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private notification: ToastrService,
    private authService: AuthService,
  ) {
  }

  ngOnInit() {

    this.route.params.subscribe(params => {
      if (params['token'] == null || params['token'] === '') {
        this.notification.error('something went wrong');
        this.router.navigate(['/']);
      }
      this.token = params['token'];
    });

    this.formData = this.builder.group({
      password: ['',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d\W]{8,}$/)
        ]
      ],
    });

  }

  onSubmit(value: ɵTypedOrUntyped<any, ɵFormGroupValue<any>, any>) {
    this.submitted = true;
    if (this.formData.valid) {
      const req: PasswordUpdate = {password: value.password};
      this.authService.updatePassword(this.token, req).subscribe({
        next: () => {
          this.notification.success('Password has been reset. Please login with your new password');
          this.router.navigate(['/login']);
        }, error: error => {

          console.log(error.status);
          if (error.status === HttpStatusCode.Conflict) {
            this.notification.error('Invalid or Expired Token');
            this.router.navigate(['/']);
          } else if (error.status === HttpStatusCode.UnprocessableEntity) {
            this.notification.error('Invalid Password');
          }
        }
      });
    }

  }


  showPassword() {
    return this.passwordVisible === true ? 'text' : 'password';
  }

  changePasswordVisibility() {
    this.passwordVisible = !this.passwordVisible;
  }
}
