import {Component, OnInit} from '@angular/core';
import {Performance} from '../../dtos/performance';
import {ActivatedRoute, Router} from '@angular/router';
import {PerformanceService} from '../../services/performance.service';
import {EventService} from '../../services/event.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.scss']
})
export class EventDetailsComponent implements OnInit {
  performances: Performance[] = [];

  constructor(
    private performanceService: PerformanceService,
    private eventService: EventService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
  ) {
  }
  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id != null) {
      this.getPerformances(Number(id));
    }
  }

  buyTickets(id: number): void {
    this.router.navigate(['performances/' + id]);
  }

  formatDateTime(performance: Performance): string {
    return new Date(performance.dateTime).toLocaleDateString();
  }

  formatLocation(performance: Performance): string {
    return performance.location.city + ',' + performance.location.country + ', ' + performance.location.postalCode +
      ', ' + performance.location.street;
  }

  private getPerformances(id: number) {
    this.performanceService.getPerformancesOfEventWithId(id).subscribe( {
      next: data => {
        this.performances = data;
      },
      error: error => {
        console.log(error);
        this.notification.error('Could not get performances of event', error);
      }
    });
  }
}
