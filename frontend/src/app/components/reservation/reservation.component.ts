import { Component, OnInit } from '@angular/core';
import { DetailedReservation } from '../../dtos/reservation';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { ReservationService } from '../../services/reservation.service';
import { SimpleTicket } from '../../dtos/ticket';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.scss'],
})
export class ReservationComponent implements OnInit {
  reservations: DetailedReservation[] = [];
  tickets: SimpleTicket[] = [];
  public clickedReservations = new Map<number, boolean>();

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private authService: AuthService,
    private reservationService: ReservationService,
    private notification: ToastrService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(() => {
      this.fetchReservations();
    });
  }

  fetchReservations(): void {
    this.reservations = [];
    this.reservationService
      .getReservationsByUserId(this.authService.getUserId())
      .subscribe({
        next: (res) => {
          this.reservations = res;
        },
        error: (err) => {
          if (err.status === 404) {
            this.router.navigate(['/404']);
          } else {
            this.notification.error(
              err.error.detail,
              'Failed loading reservations'
            );
          }
        },
      });
  }

  public selectTicket(reservation: DetailedReservation): void {
    this.clickedReservations.set(reservation.id, true);
    // Rest of your code to handle adding the ticket
    const ticket: SimpleTicket = { id: reservation.ticket.id };
    this.tickets.push(ticket);
  }

  unselectTicket(reservation: DetailedReservation): void {
    this.clickedReservations.set(reservation.id, false);
    const index = this.tickets.findIndex(
      (ticket) => ticket.id === reservation.ticket.id
    );
    if (index !== -1) {
      this.tickets.splice(index, 1);
    }
  }

  deleteReservation(reservation: DetailedReservation): void {
    this.reservationService.deleteReservation(reservation.id).subscribe({
      next: () => {
        this.notification.success('Reservation deleted');
        this.fetchReservations();
      },
      error: (err) => {
        this.notification.error(
          err.error.detail,
          'Failed to delete reservation'
        );
      },
    });
  }

  addTicketsToCart(): void {
    this.userService
      .addTicketsToCart(this.authService.getUserId(), this.tickets)
      .subscribe({
        next: () => {
          this.notification.success('Reservations added to cart');
          this.router.navigate(['/cart']);
        },
        error: (err) => {
          this.notification.error(
            err.error.detail,
            'Failed to add reservations to cart'
          );
          this.fetchReservations();
        },
      });
  }
}
