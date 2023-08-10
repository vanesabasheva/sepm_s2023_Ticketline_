import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {MerchandiseService} from '../../services/merchandise.service';
import {BookingMerchandise, Merchandise} from '../../dtos/merchandise';
import {ToastrService} from 'ngx-toastr';
import {CookieService} from 'ngx-cookie-service';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'app-merchandise',
  templateUrl: './merchandise.component.html',
  styleUrls: ['./merchandise.component.scss'],
})
export class MerchandiseComponent implements OnInit {
  merchandiseList: Merchandise[] = [];
  isChecked = false;

  tempQuantity = {};

  cart: BookingMerchandise[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private notification: ToastrService,
    private merchandiseService: MerchandiseService,
    private cookie: CookieService,
    private authService: AuthService
  ) {
  }

  ngOnInit(): void {
    this.reloadMerch(false);
    this.loadFromCookie();
  }

  reloadMerch(withPoints: boolean): void {
    this.merchandiseService.getMerchandise(withPoints).subscribe({
      next: (data) => {
        this.merchandiseList = data;
      },
      error: (error) => {
        console.log(error);
        this.notification.error('Something went wrong');
      },
    });
  }

  addMerchToCart(id: number, quantity: number): void {
    if (id >= 0 && quantity > 0 && Number.isInteger(quantity)) {
      if (this.getCartMerch(id) === undefined) {
        this.addMerch(id, quantity);
        this.notification.info('Added to Cart');
      } else {
        if (this.getCartMerch(id).quantity !== quantity) {
          this.addMerch(id, quantity);
          this.notification.info('Updated quantity');
        } else {
          this.notification.warning('Already Added to Cart');
        }
      }
      //cart expires after one day
      this.cookie.set('merchandise', JSON.stringify(this.cart), 1);
    } else {
      this.notification.error('enter correct Quantity');
    }
  }

  getPlaceholder(id: number): string {
    const value: BookingMerchandise = this.getCartMerch(id);
    return value === undefined ? '0' : value.quantity.toString();
  }

  getMerchCheck(): void {
    if (this.isChecked) {
      this.reloadMerch(true);
    } else {
      this.reloadMerch(false);
    }
  }

  //add merchandise to cart
  private addMerch(id: number, quantity: number): void {
    //check if cart has the merchandise
    if (this.checkCart(id)) {
      //update quantity
      this.getCartMerch(id).quantity = quantity;
    } else {
      this.cart.push({id, quantity});
    }
  }

  //get merchandise by id in the cart
  private getCartMerch(id: number): BookingMerchandise {
    return this.cart.find((merch) => merch.id === id);
  }

  //check if the merchandise is already in the cart
  private checkCart(id: number): boolean {
    return this.cart.some((item) => item.id === id);
  }

  //load cart from cookie
  private loadFromCookie() {
    const value: string = this.cookie.get('merchandise');
    if (value !== '') {
      Object.values(JSON.parse(value)).forEach((val) => {
        // val -> Object { id: 1, quantity: 3 }
        if (val['id'] >= 0 && val['quantity'] > 0) {
          this.addMerch(val['id'], val['quantity']);
        }
      });
    }
  }
}
