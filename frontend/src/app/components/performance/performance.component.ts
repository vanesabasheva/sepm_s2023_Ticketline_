import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Performance } from '../../dtos/performance';
import { PerformanceTicket, SimpleTicket } from '../../dtos/ticket';
import { PerformanceService } from '../../services/performance.service';
import { UserService } from '../../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { add } from 'lodash';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-performance',
  templateUrl: './performance.component.html',
  styleUrls: ['./performance.component.scss'],
})
export class PerformanceComponent implements OnInit {
  performance: Performance = null;
  selectedTickets: PerformanceTicket[] = [];
  hoverSectorId: number = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private performanceService: PerformanceService,
    private userService: UserService,
    private authService: AuthService,
    private notification: ToastrService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(() => {
      this.fetchPerformance();
    });
  }

  fetchPerformance(): void {
    this.performance = null;
    this.performanceService
      .getPerformanceById(+this.route.snapshot.paramMap.get('id'))
      .subscribe({
        next: (res) => {
          this.performance = res;
          // remove tickets that are no longer available or reserved from selection
          this.selectedTickets = this.selectedTickets.filter((selectedTicket) =>
            this.performance.tickets.some((row) =>
              row.some(
                (ticket) =>
                  ticket &&
                  ticket.ticketId === selectedTicket.ticketId &&
                  !ticket.reserved
              )
            )
          );
          if (this.hasPerformancePassed()) {
            this.clearSelectedTickets();
          }
        },
        error: (err) => {
          if (err.status === 404) {
            this.router.navigate(['/404']);
          } else {
            this.notification.error(
              err.error.detail,
              'Failed loading performance'
            );
            console.log(err);
          }
        },
      });
  }

  generateGridStyles(): { [key: string]: string } {
    if (!this.performance) {
      return {};
    }
    const cols = this.performance.tickets[0].length;
    const templateColumns = `repeat(${cols}, 2rem)`;

    return {
      // eslint-disable-next-line @typescript-eslint/naming-convention
      'grid-template-columns': templateColumns,
    };
  }

  toggleTicket(ticket: PerformanceTicket): void {
    if (this.hasPerformancePassed()) {
      this.clearSelectedTickets();
      this.notification.warning(
        'You cannot reserve tickets for this performance anymore.',
      );
      return;
    }
    if (!ticket) {
      return;
    }

    if (this.isTicketStanding(ticket)) {
      this.toggleStandingSector(ticket.sectorId);
      return;
    }

    if (ticket.reserved) {
      return;
    }

    if (this.isTicketSelected(ticket)) {
      this.selectedTickets = this.selectedTickets.filter(
        (selectedTicket) => selectedTicket.ticketId !== ticket.ticketId
      );
    } else if (this.selectedTickets.length < 6) {
      this.selectedTickets.push(ticket);
    }
  }

  isTicketSelected(ticket: PerformanceTicket): boolean {
    return (
      ticket &&
      this.selectedTickets.some(
        (selectedTicket) => selectedTicket.ticketId === ticket.ticketId
      )
    );
  }

  isTicketStanding(ticket: PerformanceTicket): boolean {
    return (
      ticket &&
      this.performance.performanceSector[ticket.sectorId].standing === true
    );
  }

  toggleStandingSector(sectorId: number): void {
    if (this.isWholeSectorReserved(sectorId)) {
      return;
    }

    if (this.isSomeSectorSelected(sectorId)) {
      this.selectedTickets = this.selectedTickets.filter(
        (ticket) => ticket.sectorId !== sectorId
      );
    } else {
      this.addStandingTicket(sectorId);
    }
  }

  isWholeSectorReserved(sectorId: number): boolean {
    return !this.performance.tickets.some((row) =>
      row.some(
        (ticket) => ticket && ticket.sectorId === sectorId && !ticket.reserved
      )
    );
  }

  isSomeSectorSelected(sectorId: number): boolean {
    return this.selectedTickets.some((ticket) => ticket.sectorId === sectorId);
  }

  addStandingTicket(sectorId: number): void {
    if (this.canAddStandingTicket(sectorId)) {
      this.selectedTickets.push(this.findTicketWithSectorId(sectorId));
    }
  }

  canAddStandingTicket(sectorId: number): boolean {
    return (
      this.selectedTickets.length < 6 &&
      this.findTicketWithSectorId(sectorId) !== null
    );
  }

  removeTicketFromSelected(ticket: PerformanceTicket): void {
    this.selectedTickets = this.selectedTickets.filter(
      (t) => t && t.ticketId !== ticket.ticketId
    );
  }

  findTicketWithSectorId(sectorId: number): PerformanceTicket | null {
    for (const row of this.performance.tickets) {
      for (const ticket of row) {
        if (
          ticket &&
          ticket.sectorId === sectorId &&
          !ticket.reserved &&
          !this.selectedTickets.some(
            (selectedTicket) => selectedTicket.ticketId === ticket.ticketId
          )
        ) {
          return ticket;
        }
      }
    }
    return null; // No suitable ticket with the specified sector ID was found
  }

  getTicketClasses(ticket: PerformanceTicket): string {
    if (!ticket) {
      return '';
    }

    let classes = '';
    const standing = this.isTicketStanding(ticket);

    if (standing) {
      classes += 'standing ';

      if (this.isSomeSectorSelected(ticket.sectorId)) {
        classes += 'selected ';
      }
      if (this.isWholeSectorReserved(ticket.sectorId)) {
        classes += 'reserved ';
      } else if (this.hoverSectorId === ticket.sectorId) {
        classes += 'hovered ';
      }
    } else {
      classes += 'seat ';
      if (ticket.reserved) {
        classes += 'reserved ';
      }
      if (this.isTicketSelected(ticket)) {
        classes += 'selected ';
      }
    }

    return classes.trim();
  }

  hoverTicket(ticket: PerformanceTicket): void {
    if (!ticket) {
      this.hoverSectorId = null;
      return;
    }
    this.hoverSectorId = ticket.sectorId;
  }

  totalTicketPrice(): number {
    let total = 0;
    for (const ticket of this.selectedTickets) {
      total += this.performance.performanceSector[ticket.sectorId].price;
    }
    return total;
  }

  getSectorName(ticket: PerformanceTicket): string {
    return this.performance.performanceSector[ticket.sectorId].name;
  }

  getPrice(ticket: PerformanceTicket): number {
    return this.performance.performanceSector[ticket.sectorId].price;
  }

  clearSelectedTickets(): void {
    this.selectedTickets = [];
  }

  addTicketsToCart(): void {
    const ticketIds: SimpleTicket[] = this.selectedTickets.map((t) => ({
      id: t.ticketId,
    }));
    this.userService
      .addTicketsToCart(this.authService.getUserId(), ticketIds)
      .subscribe({
        next: () => {
          this.notification.success('Items added to cart');
          this.router.navigate(['/cart']);
        },
        error: (err) => {
          this.notification.error(
            err.error.detail,
            'Failed to add items to cart'
          );
          this.fetchPerformance();
        },
      });
  }

  hasPerformancePassed(): boolean {
    const performanceDateTime = new Date(this.performance.dateTime);
    performanceDateTime.setMinutes(
      performanceDateTime.getMinutes() - 30
    );
    const now = new Date();
    return performanceDateTime < now;
  }
}
