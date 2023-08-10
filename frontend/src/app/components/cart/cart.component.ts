import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { AuthService } from '../../services/auth.service';
import { BookingTicket, CartTicket } from '../../dtos/ticket';
import { ToastrService } from 'ngx-toastr';
import {
  CheckoutPaymentDetail,
} from '../../dtos/payment-detail';
import { Booking } from 'src/app/dtos/booking';
import { MatDialog } from '@angular/material/dialog';
import { PaymentDetailComponent } from './payment-detail/payment-detail.component';
import { DeliveryAddressComponent } from './delivery-address/delivery-address.component';
import { BuyComponent } from './buy/buy.component';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { BookingMerchandise, Merchandise } from '../../dtos/merchandise';
import { CreatePaymentDetailComponent } from './create-payment-detail/create-payment-detail.component';
import { CheckoutLocation } from '../../dtos/location';
import { CreateDeliveryLocationComponent } from './create-delivery-location/create-delivery-location.component';
import { Cart } from '../../dtos/cart';
import { MerchandiseEventComponent } from '../merchandise/merchandise-event/merchandise-event.component';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss'],
})
export class CartComponent implements OnInit {
  cartTickets: CartTicket[] = [];
  cartMerch: Merchandise[] = [];
  cart: Cart = new Cart();

  bannerError: string | null = null;

  constructor(
    private service: CartService,
    private authService: AuthService,
    private notification: ToastrService,
    private dialog: MatDialog,
    private router: Router,
    private cookie: CookieService,
    private  userService: UserService,
  ) {}

  ngOnInit(): void {
    this.reloadCart();
    this.loadFromCookie();
  }

  reloadCart() {
    this.service.getCartTickets(this.authService.getUserId()).subscribe({
      next: (data) => {
        this.cart = data;
        this.cartTickets = data.tickets;
        this.cartTickets.forEach((cartTicket) => {
          cartTicket.reservation = false;
        });
      },
      error: (error) => {
        console.error('Error fetching cart', error);
        const errorMessage =
          error.status === 0 ? 'Is the user logged in?' : error.message.message;
        this.notification.error(errorMessage, 'Could Not Fetch Cart');
      },
    });
  }

  removeTicket(ticketId: number) {
    this.service
      .removeTicketFromCart(this.authService.getUserId(), ticketId)
      .subscribe({
        next: () => {
          this.reloadCart();
        },
        error: (error) => {
          console.error(error.message);
        },
      });
  }

  dateToLocaleDate(cartTicket: CartTicket): string {
    return new Date(cartTicket.date).toLocaleDateString();
  }

  seatToString(cartTicket: CartTicket): string {
    return 'Row: ' + cartTicket.seatRow + ' Seat: ' + cartTicket.seatNumber;
  }

  locationToString(cartTicket: CartTicket) {
    return (
      cartTicket.locationCity +
      ', ' +
      cartTicket.locationStreet +
      '' +
      '\nHall: ' +
      cartTicket.hallName
    );
  }

  checkout() {
    this.service.getCheckoutDetails(this.authService.getUserId()).subscribe({
      next: async (checkoutDetails) => {
        const booking = new Booking();
        // convert cartTickets to bookingTickets
        booking.tickets = this.cartTickets.map((cartTicket) => {
          const bookingTicket = new BookingTicket();
          bookingTicket.ticketId = cartTicket.id;
          bookingTicket.reservation = cartTicket.reservation;
          return bookingTicket;
        });

        booking.merchandise = this.cartMerch.map(
          (cartMerch) =>
            new BookingMerchandise(
              cartMerch.id,
              cartMerch.quantity,
              cartMerch.buyWithPoints
            )
        );

        try {
          let paymentDetailId = -1;
          do {
            paymentDetailId = await this.openPaymentDialog(
              checkoutDetails.paymentDetails
            );
            if (paymentDetailId === -1) {
              const createdPayment = await this.openCreatePaymentDialog();
              checkoutDetails.paymentDetails.push(createdPayment);
            }
          } while (paymentDetailId === -1);
          booking.paymentDetailId = paymentDetailId;

          let locationId = -1;
          do {
            locationId = await this.openLocationDialog(
              checkoutDetails.locations
            );
            if (locationId === -1) {
              const createdLocation = await this.openCreateLocationDialog();
              checkoutDetails.locations.push(createdLocation);
            }
          } while (locationId === -1);
          booking.locationId = locationId;

          const buy = await this.openBuyDialog();
          if (buy) {
            this.service.buy(booking).subscribe({
              next: (orderResp) => {
                this.notification.success('Successfully booked tickets');
                this.cookie.delete('merchandise');
                this.userService.getUserPoints(this.authService.getUserId());
                if (orderResp !== null) {
                  this.router.navigate(['/orders/' + orderResp.id]);
                } else {
                  this.router.navigate(['/reservations']);
                }
              },
              error: (err) => {
                console.error(err);
                this.notification.error(
                  err.error.detail,
                  'Could Not Book Tickets'
                );
                this.reloadCart();
              },
            });
          }
        } catch (err) {
          console.error(err);
          this.notification.error(
            err.error.detail,
            'Could Not Fetch Checkout Details'
          );
        }
      },
      error: (err) => {
        console.error(err);
        this.notification.error(
          err.error.detail,
          'Could Not Fetch Checkout Details'
        );
      },
    });
  }

  openCreateLocationDialog(): Promise<CheckoutLocation> {
    return new Promise((resolve, reject) => {
      const locationRef = this.dialog.open(CreateDeliveryLocationComponent, {
        width: '25%',
      });
      locationRef.componentInstance.locationEmitter.subscribe(
        (location: CheckoutLocation) => {
          locationRef.close();
          resolve(location);
        }
      );
    });
  }

  openCreatePaymentDialog(): Promise<CheckoutPaymentDetail> {
    return new Promise((resolve, reject) => {
      const paymentRef = this.dialog.open(CreatePaymentDetailComponent, {
        width: '25%',
      });
      paymentRef.componentInstance.paymentDetailEmitter.subscribe(
        (paymentDetail: CheckoutPaymentDetail) => {
          paymentRef.close();
          resolve(paymentDetail);
        }
      );
    });
  }


  openPaymentDialog(paymentDetails): Promise<number> {
    return new Promise((resolve, reject) => {
      const paymentRef = this.dialog.open(PaymentDetailComponent, {
        width: '25%',
        data: { paymentDetails },
      });

      paymentRef.componentInstance.paymentSelector.subscribe(
        (paymentDetailId: number) => {
          paymentRef.close();
          resolve(paymentDetailId);
        }
      );
    });
  }

  openLocationDialog(locations): Promise<number> {
    return new Promise((resolve, reject) => {
      const locationRef = this.dialog.open(DeliveryAddressComponent, {
        width: '25%',
        data: { locations },
      });

      locationRef.componentInstance.locationSelector.subscribe(
        (locationId: number) => {
          locationRef.close();
          resolve(locationId);
        }
      );
    });
  }

  openBuyDialog(): Promise<boolean> {
    return new Promise((resolve, reject) => {
      const buyRef = this.dialog.open(BuyComponent, {
        width: '25%',
      });

      buyRef.componentInstance.buyEmitter.subscribe((buy: boolean) => {
        buyRef.close();
        resolve(buy);
      });
    });
  }

  openMerchDialog() {
    this.dialog.open(MerchandiseEventComponent, {
      width: '25%',
    });
  }

  checkoutPointsValidation(usedPoints: number): void {
    if (usedPoints > this.cart.userPoints) {
      this.openMerchDialog();
    } else {
      this.checkout();
    }
  }
  pointsUsed(): number {
    let points = 0;
    this.cartMerch.forEach((cartMerch) => {
      if (cartMerch.buyWithPoints) {
        points += cartMerch.pointsPrice * cartMerch.quantity;
      }
    });
    return points;
  }

  totalPrice(price: number, quantity: number): number {
    return Number((price * quantity).toFixed(2));
  }

  totalPoints(points: number, quantity: number): number {
    return Number(Math.ceil(points * quantity));
  }

  overallPrice(): number {
    let price = 0;
    this.cartTickets.forEach((cartTicket) => {
      if (!cartTicket.reservation) {
        price += cartTicket.price;
      }
    });
    this.cartMerch.forEach((cartMerch) => {
      if (!cartMerch.buyWithPoints) {
        price += cartMerch.price * cartMerch.quantity;
      }
    });
    return Number(price.toFixed(2));
  }

  receivedPoints(): number {
    let points = 0;
    this.cartTickets.forEach((cartTicket) => {
      if (!cartTicket.reservation) {
        points += Math.floor(cartTicket.price);
      }
    });
    this.cartMerch.forEach((cartMerch) => {
      if (!cartMerch.buyWithPoints) {
        points += Math.floor(cartMerch.price * cartMerch.quantity);
      }
    });
    return points;
  }

  //check if user has enough points to buy merch
  handleCheckboxClickMerch(points: number, checkbox: HTMLInputElement): void {
    if (points > this.cart.userPoints) {
      this.notification.error(
        'You do not have enough points.',
        'Not enough points'
      );
      checkbox.checked = false;
    }
  }

  // remove merch from cart
  removeMerch(merchId: number) {
    const value: string = this.cookie.get('merchandise');
    if (value !== '') {
      //delete merch from this.cartMerch
      this.cartMerch = this.cartMerch.filter((merch) => merch.id !== merchId);
      //delete merch from cookie
      const bookingMerchandises: BookingMerchandise[] = Object.values(
        JSON.parse(value)
      ).map((entry) => new BookingMerchandise(entry['id'], entry['quantity']));
      const newBookingMerchandises: BookingMerchandise[] =
        bookingMerchandises.filter((merch) => merch.id !== merchId);
      this.cookie.set('merchandise', JSON.stringify(newBookingMerchandises));
    }
  }

  //load merch from cookie and then fetch remaining Data from backend
  private loadFromCookie() {
    const value: string = this.cookie.get('merchandise');
    if (value !== '') {
      //create BookingMerchandise[] obj from json
      const bookingMerchandises: BookingMerchandise[] = Object.values(
        JSON.parse(value)
      ).map((entry) => new BookingMerchandise(entry['id'], entry['quantity']));
      //get merch info from backend
      this.service.getMerchInfo(bookingMerchandises).subscribe({
        next: (data: Merchandise[]) => {
          data.map((merch) => {
            merch.quantity = bookingMerchandises.find(
              (bookingMerch) => bookingMerch.id === merch.id
            ).quantity;
          });
          this.cartMerch = data;
        },
        error: (error) => {
          this.notification.error(error.message, 'Could Not Fetch Merchandise');
        },
      });
    }
  }
}
