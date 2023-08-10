import { Component, OnInit } from '@angular/core';
import { EventTicketCount } from '../../dtos/topTen/topTen';
import { ToastrService } from 'ngx-toastr';
import { TopTenService } from '../../services/top-ten.service';

@Component({
  selector: 'app-top-ten',
  templateUrl: './top-ten.component.html',
  styleUrls: ['./top-ten.component.scss'],
})
export class TopTenComponent implements OnInit {
  topEvents: EventTicketCount[] = [];
  uniqueTypes: string[] = [];
  eventsByType: { [type: string]: EventTicketCount[] } = {};
  error = false;
  finishedLoading = false;
  constructor(
    private topTenService: TopTenService,
    private notification: ToastrService
  ) {}

  ngOnInit() {
    this.loadTopTenEvents();
  }

  private loadTopTenEvents() {
    this.topTenService.getTopTenEvents().subscribe({
      next: (res) => {
        this.topEvents = res;
        this.filterByCategories();
        this.finishedLoading = true;
      },
      error: (error) => {
        console.log(error);
        this.error = true;
        this.notification.error('Unable to load top ten events');
      },
    });
  }

  private filterByCategories() {
    this.uniqueTypes = Array.from(
      new Set(this.topEvents.map((event) => event.type))
    );

    for (const event of this.topEvents) {
      if (this.eventsByType[event.type] === undefined) {
        this.eventsByType[event.type] = [];
      }
      this.eventsByType[event.type].push(event);
    }
  }
}
