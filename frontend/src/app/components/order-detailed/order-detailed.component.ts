import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderPage } from 'src/app/dtos/order';
import { ToastrService } from 'ngx-toastr';
import { OrderPageTicket } from 'src/app/dtos/ticket';
import { OrderMerchandise } from 'src/app/dtos/merchandise';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-order-detailed',
  templateUrl: './order-detailed.component.html',
  styleUrls: ['./order-detailed.component.scss'],
})
export class OrderDetailedComponent implements OnInit {
  orderPage: OrderPage = null;
  selectedTickets: number[] = [];
  selectedMerchandise: number[] = [];

  constructor(
    private orderService: OrderService,
    private userService: UserService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router,
    private notification: ToastrService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(() => {
      this.fetchOrder();
    });
  }

  fetchOrder(): void {
    this.orderPage = null;
    this.orderService
      .getOrderById(+this.route.snapshot.paramMap.get('id'))
      .subscribe({
        next: (res) => {
          this.orderPage = res;
          console.log(res);
        },
        error: (err) => {
          if (err.status === 404) {
            this.router.navigate(['/404']);
          } else {
            this.notification.error(err.error.detail, 'Failed loading order');
            console.log(err);
          }
        },
      });
  }

  totalAmount(): number {
    return this.orderPage.transactions.reduce(
      (acc, curr) => acc + curr.deductedAmount,
      0
    );
  }

  totalPoints(): number {
    return this.orderPage.transactions.reduce(
      (acc, curr) => acc + curr.deductedPoints,
      0
    );
  }

  deductedReceived(amount: number): string {
    return amount >= 0 ? 'deducted' : 'received';
  }

  abs(amount: number): number {
    return Math.abs(Math.round(amount * 100) / 100);
  }

  isTicketSelected(ticket: OrderPageTicket): boolean {
    return this.selectedTickets.some(
      (selectedTicket) => selectedTicket === ticket.id
    );
  }

  toggleTicket(ticket: OrderPageTicket): void {
    if (this.isTicketSelected(ticket)) {
      this.selectedTickets = this.selectedTickets.filter(
        (selectedTicket) => selectedTicket !== ticket.id
      );
    } else {
      this.selectedTickets.push(ticket.id);
    }
  }

  isMerchandiseSelected(merchandise: OrderMerchandise): boolean {
    return this.selectedMerchandise.some(
      (selectedMerchandise) => selectedMerchandise === merchandise.id
    );
  }

  toggleMerchandise(merchandise: OrderMerchandise): void {
    if (this.isMerchandiseSelected(merchandise)) {
      this.selectedMerchandise = this.selectedMerchandise.filter(
        (selectedMerchandise) => selectedMerchandise !== merchandise.id
      );
    } else {
      this.selectedMerchandise.push(merchandise.id);
    }
  }

  areItemsSelected(): boolean {
    return (
      this.selectedTickets.length > 0 || this.selectedMerchandise.length > 0
    );
  }

  cancelOrder(): void {
    this.orderService
      .cancelOrder(+this.route.snapshot.paramMap.get('id'))
      .subscribe({
        next: () => {
          this.notification.success(
            'Your order has been canceled',
            'Order canceled'
          );
          this.fetchOrder();
          this.selectedTickets = [];
          this.selectedMerchandise = [];
          this.userService.getUserPoints(this.authService.getUserId());
        },
        error: (err) => {
          this.notification.error(err.error.detail, 'Failed canceling order');
          console.log(err);
        },
      });
  }

  cancelSelected(): void {
    this.orderService
      .cancelItems(
        +this.route.snapshot.paramMap.get('id'),
        this.selectedTickets,
        this.selectedMerchandise
      )
      .subscribe({
        next: () => {
          this.notification.success(
            'Your items have been canceled',
            'Items canceled'
          );
          this.fetchOrder();
          this.selectedTickets = [];
          this.selectedMerchandise = [];
          this.userService.getUserPoints(this.authService.getUserId());
        },
        error: (err) => {
          this.notification.error(err.error.detail, 'Failed canceling items');
          console.log(err);
        },
      });
  }

  printReceipt(transactionId: number): void {
    this.orderService.getReceipt(transactionId).subscribe({
      next: (pdfBlob: Blob) => {
        const fileURL = URL.createObjectURL(pdfBlob);
        window.open(fileURL, '_blank');
      },
      error: (err) => {
        this.notification.error(err.error.detail, 'Failed printing receipt');
        console.log(err);
      },
    });
  }
}
