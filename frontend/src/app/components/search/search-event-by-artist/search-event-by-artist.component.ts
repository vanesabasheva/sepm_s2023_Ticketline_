import {Component, OnInit} from '@angular/core';
import {EventService} from '../../../services/event.service';
import {Artist} from '../../../dtos/createEvent/artist';
import {debounceTime} from 'rxjs';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Event} from '../../../dtos/createEvent/event';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-search-event-by-artist',
  templateUrl: './search-event-by-artist.component.html',
  styleUrls: ['./search-event-by-artist.component.scss'],
})
export class SearchEventByArtistComponent implements OnInit {
  search: false;
  searchName = '';
  artists: Artist[] = [];
  events: Event[] = [];
  searchForm: FormGroup;
  searchPerformed = false;

  constructor(
    private eventService: EventService,
    private formBuilder: FormBuilder,
    private router: Router,
    private notification: ToastrService
  ) {}

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      searchBar: '',
    });
  }

  onChanges(): void {
    this.searchForm
      .get('searchBar')
      .valueChanges.pipe(debounceTime(500))
      .subscribe({
        next: (data) => {
          this.searchName = data;
          this.searchArtists();
        },
        error: (error) => {
          console.error('Error fetching artists', error);
          this.notification.error('Could not fetch artists', error);
        },
      });
  }

  searchArtists() {
    this.eventService
      .searchArtistsByName(this.searchName)
      .pipe(debounceTime(600))
      .subscribe({
        next: (data) => {
          this.artists = data;
          if (
            this.artists.length === 0 &&
            this.notification.currentlyActive === 0
          ) {
            this.notification.error('No artists match your search');
          }
        },
        error: (error) => {
          console.error('Error fetching artists', error);
          this.notification.error('Could not fetch artists', error);
        },
      });
  }

  searchEvents() {
    const selectedArtist = this.artists.find(
      (artist) => artist.name === this.searchForm.value.searchBar
    );
    this.eventService
      .searchEventsByArtistName(selectedArtist.id, this.searchName)
      .pipe(debounceTime(600))
      .subscribe({
        next: (data) => {
          this.events = data;
          this.searchPerformed = true;
        },
        error: (error) => {
          console.error('Error fetching events of artists', error);
          this.notification.error('Could not fetch events of artists', error);
        },
      });
  }

  redirectToEvent(eventId: number) {
    this.router.navigate(['events/' + eventId]);
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
}
