import {Component, OnInit} from '@angular/core';
import {Performance} from '../../../dtos/performance';
import {PerformanceService} from '../../../services/performance.service';
import {ToastrService} from 'ngx-toastr';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-performance-on-location',
  templateUrl: './performance-on-location.component.html',
  styleUrls: ['./performance-on-location.component.scss']
})
export class PerformanceOnLocationComponent implements OnInit {
  performances: Performance[] = [];

  constructor(
    private performanceService: PerformanceService,
    private notification: ToastrService,
    private router: Router,
    private route: ActivatedRoute
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

  private getPerformances(id: number): void {
    this.performanceService.getPerformancesOnLocationById(id).subscribe({
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
