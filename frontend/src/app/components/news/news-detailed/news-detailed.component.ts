import { Component, OnInit} from '@angular/core';
import {News} from '../../../dtos/news';
import {NewsService} from '../../../services/news.service';
import {ActivatedRoute} from '@angular/router';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-news-detailed',
  templateUrl: './news-detailed.component.html',
  styleUrls: ['./news-detailed.component.scss']
})
export class NewsDetailedComponent implements OnInit{

  error = false;
  id: number = null;

  currentNews: News = null;

  constructor(private newsService: NewsService,
              private notification: ToastrService,
              private route: ActivatedRoute) {
  }


  ngOnInit() {
    this.id = parseInt(this.route.snapshot.paramMap.get('id'),10);
    this.loadNews();
  }


  /**
   * Loads the specified page of message from the backend
   */
  private loadNews() {
    this.newsService.getNewsById(this.id).subscribe({
      next: res => {
        this.currentNews = res;
      },
      error: error => {
        console.log(error);
        this.error = true;
        this.notification.error(error.error.detail);
      }
    });

  }
}


