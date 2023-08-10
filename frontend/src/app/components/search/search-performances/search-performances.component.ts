import {Component, OnInit} from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';
import {PerformanceService} from '../../../services/performance.service';
import {HttpParams} from '@angular/common/http';
import {debounceTime} from 'rxjs';
import {PerformanceSearch} from '../../../dtos/performance';

@Component({
  selector: 'app-search-performances',
  templateUrl: './search-performances.component.html',
  styleUrls: ['./search-performances.component.scss']
})
export class SearchPerformancesComponent implements OnInit {
  searchEventName = '';
  searchHallName = '';
  searchDateTime = '';
  searchPrice: number;
  searchPerformed = false;
  performances: PerformanceSearch[] = [];

  constructor(
    private performanceService: PerformanceService,
    private notification: ToastrService,
    private router: Router) {
  }

  ngOnInit(): void {
  }

  searchPerformances() {
    this.searchPerformed = true;
    let params = new HttpParams()
      .set('eventName', this.searchEventName)
      .set('hallName', this.searchHallName)
      .set('dateTime', this.searchDateTime);
      if (this.searchPrice){
        params = params.set('price', this.searchPrice);
      }
    this.performanceService.searchPerformances(params).pipe(
      debounceTime(600)).subscribe({
      next: data => {
        this.performances = data;
      },
      error: error => {
        console.error('Error fetching events with given parameters');
        this.notification.error(error.message, 'Could not fetch events');
      }
    });
  }

  buyTickets(id: number) {
    this.router.navigate(['performances/' + id]);
  }

  formatDateTime(dateTime: Date) {
    return new Date(dateTime).toLocaleDateString() + ';   ' + new Date(dateTime).toLocaleTimeString();
  }
}
