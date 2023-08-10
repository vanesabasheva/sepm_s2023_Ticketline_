import { Component, Input, OnInit } from '@angular/core';
import { EventTicketCount } from '../../../dtos/topTen/topTen';
import * as Highcharts from 'highcharts';
import { Router } from '@angular/router';

@Component({
  selector: 'app-top-ten-list',
  templateUrl: './top-ten-list.component.html',
  styleUrls: ['./top-ten-list.component.scss'],
})
export class TopTenListComponent implements OnInit {
  @Input() topTenList: EventTicketCount[];
  highcharts: typeof Highcharts = Highcharts;
  chartOptions: Highcharts.Options = {
    title: {
      text: undefined,
    },
    legend: {
      enabled: false,
    },
    xAxis: {
      categories: [
        '1st',
        '2nd',
        '3rd',
        '4th',
        '5th',
        '6th',
        '7th',
        '8th',
        '9th',
        '10th',
      ],
    },
    yAxis: {
      title: {
        text: 'Tickets Sold',
      },
    },
  };

  constructor(private router: Router) {}

  ngOnInit() {
    const series: Highcharts.SeriesColumnOptions = {
      type: 'column',
      name: 'Tickets Sold',
      data: this.topTenList.map((event) => ({
        name: event.name,
        y: event.ticketCount,
        events: {
          click: () => {
            this.router.navigate(['/events', event.id]);
          },
        },
        color: '#d16663',
      })),
    };
    this.chartOptions.series = [series];
  }
}
