import {Component} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {ToastrService} from 'ngx-toastr';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  userId: number;
  userPoints: number;
  cartItems: number;
  bannerError: string | null = null;
  gotPoints = false;

  constructor(public authService: AuthService,
              private service: UserService,
              private notification: ToastrService) {}

  getPoints(): number {
    if (this.authService.isLoggedIn() && !this.gotPoints) {
      this.userId = this.authService.getUserId();

      this.service.getUserPoints(this.userId).subscribe({
        next: (data) => {
          this.userPoints = data;
        },
        error: (error) => {
            this.bannerError = error;
            this.notification.error(error.error.details, 'Error while fetching points');
        }
      });
    }
    this.gotPoints = true;
    return this.userPoints;
  }

  getCartItems() {
    return '10';
  }
}

