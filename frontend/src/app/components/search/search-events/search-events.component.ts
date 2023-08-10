import {Component, OnInit} from '@angular/core';
import {Event} from '../../../dtos/createEvent/event';
import {HttpParams} from '@angular/common/http';
import {EventService} from '../../../services/event.service';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';
import {debounceTime} from 'rxjs';

@Component({
  selector: 'app-search-events',
  templateUrl: './search-events.component.html',
  styleUrls: ['./search-events.component.scss'],
})
export class SearchEventsComponent implements OnInit {
  events: Event[] = [];
  searchName = '';
  searchDescription = '';
  searchType = '';
  searchLength = '';
  searchPerformed = false;

  constructor(
    private eventService: EventService,
    private notification: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  searchEvents() {
    this.searchPerformed = true;
    const params = new HttpParams()
      .set('name', this.searchName)
      .set('description', this.searchDescription)
      .set('type', this.searchType.replace(/\s/g, ''))
      .set('length', this.searchLength);
    this.eventService
      .searchEvents(params)
      .pipe(debounceTime(600))
      .subscribe({
        next: (data) => {
          this.events = data;
        },
        error: (error) => {
          console.error('Error fetching events with given parameters');
          this.notification.error(error.message, 'Could not fetch events');
        },
      });
  }

  formatLength(event: Event): string {
    const pattern = /(\d+)/; // Regular expression to match digits
    const matches = event.length.toString().match(pattern); // Find matches in the string
    let durationValue = 0;
    if (matches && matches.length > 1) {
      durationValue = parseInt(matches[1], 10); // Parse the matched digits as an integer
    }
    return durationValue + 'h';
  }

  redirectToEvent(id: number) {
    this.router.navigate(['events/' + id]);
  }

  clearFilter() {
    // eslint-disable-next-line @typescript-eslint/no-unused-expressions
    this.searchLength === '';
    this.searchEvents();
  }
}
