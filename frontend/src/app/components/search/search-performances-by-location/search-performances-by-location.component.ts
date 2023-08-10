import { Component, OnInit } from '@angular/core';
import { LocationSearch } from '../../../dtos/location';
import { EventService } from '../../../services/event.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { HttpParams } from '@angular/common/http';
import { debounceTime } from 'rxjs';

@Component({
  selector: 'app-search-performances-by-location',
  templateUrl: './search-performances-by-location.component.html',
  styleUrls: ['./search-performances-by-location.component.scss'],
})
export class SearchPerformancesByLocationComponent implements OnInit {
  search = false;
  locations: LocationSearch[] = [];
  searchPostalCode: number;
  searchStreet = '';
  searchCity = '';
  searchCountry = '';

  constructor(
    private eventService: EventService,
    private notification: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  searchLocations() {
    this.search = true;
    let params = new HttpParams()
      .set('street', this.searchStreet)
      .set('country', this.searchCountry)
      .set('city', this.searchCity);
    if (this.searchPostalCode) {
      params = params.set('postalCode', this.searchPostalCode);
    }
    this.eventService
      .searchLocations(params)
      .pipe(debounceTime(600))
      .subscribe({
        next: (data) => {
          this.locations = data;
        },
        error: (error) => {
          console.error('Error fetching locations with given parameters');
          this.notification.error(error.message, 'Could not fetch locations');
        },
      });
  }

  redirectToPerformances(locationId: number) {
    this.router.navigate(['locations/' + locationId + '/performances']);
  }
}
