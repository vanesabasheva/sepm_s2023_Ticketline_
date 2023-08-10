import {ChangeDetectorRef, Component, OnInit, TemplateRef} from '@angular/core';
import {NewsService} from '../../services/news.service';
import {News} from '../../dtos/news';
import {NgbModal, NgbPaginationConfig} from '@ng-bootstrap/ng-bootstrap';
import {FormControl, FormGroup, UntypedFormBuilder} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {ToastrService} from 'ngx-toastr';
import {debounceTime} from 'rxjs';
import {HttpParams} from '@angular/common/http';
import {EventService} from '../../services/event.service';
import {Event} from '../../dtos/createEvent/event';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.scss']
})
export class NewsComponent implements OnInit {
  submitted = false;

  currentNews: News;

  carouselItems: News[] = [];

  showOldNews: false;

  selectedFile: File | null = null;

  imagePreview: string | ArrayBuffer;

  searchForm: FormGroup;

  searchEventName: string;

  events: Event[] = [];

  private news: News[] = [];


  constructor(private newsService: NewsService,
              private eventService: EventService,
              private notification: ToastrService,
              private ngbPaginationConfig: NgbPaginationConfig,
              private formBuilder: UntypedFormBuilder,
              private cd: ChangeDetectorRef,
              private authService: AuthService,
              private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      searchBar: new FormControl('')
    });
    this.loadNews();
  }

  /**
   * sets this.selectedFile to the file and shows a preview of the image
   *
   * @param event
   */
  onFileSelected(event: any): void {
    if (event != null) {
      this.selectedFile = event.target.files[0];
    }
    if (this.selectedFile != null) {
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  loadCarouselItems() {
    this.carouselItems = [];
    let counter = 0;
    for (const news of this.getNews()) {
      if (news.imagePath != null) {
        this.carouselItems.push(news);
        counter++;
      }
      if (counter === 5) {
        break;
      }
    }
  }


  /**
   * Returns true if the authenticated user is an admin
   */
  isAdmin(): boolean {
    return this.authService.getUserRole() === 'ADMIN';
  }


  openAddModal(newsAddModal: TemplateRef<any>) {
    this.currentNews = new News();
    this.modalService.open(newsAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  /**
   * Starts form validation and builds a news dto for sending a creation request if the form is valid.
   * If the procedure was successful, the form will be cleared.
   */
  addNews(form) {
    this.submitted = true;
    if (form.valid) {
      this.currentNews.publicationDate = new Date().toISOString();
      this.createNews(this.currentNews, this.selectedFile);
      this.clearForm();
    }
  }

  getNews(): News[] {
    return this.news;
  }

  /**
   * Loads the specified page of message from the backend
   */
  loadNews() {
    if (this.showOldNews) {
      this.newsService.getAllNews().subscribe({
        next: (news: News[]) => {
          this.news = news;
          this.loadCarouselItems();
        },
        error: error => {
          this.notification.error(error.error.detail);
        }
      });
    } else {
      this.newsService.getNews().subscribe({
        next: (news: News[]) => {
          this.news = news;
          this.loadCarouselItems();
        },
        error: error => {
          this.notification.error(error.error.detail);
        }
      });
    }
  }

  /**
   * when event changes
   */
  onChanges(): void {

    this.searchForm.get('searchBar').valueChanges.pipe(
      debounceTime(500)).subscribe({
      next: data => {
        this.searchEventName = data;
        this.searchEvents();

      },
      error: error => {
        console.error('Error fetching artists', error);
        this.notification.error('Could not fetch artists', error);
      }
    });

  }


  /**
   * Sends news creation request
   *
   * @param news the news which should be created
   * @param image the image which should be uploaded
   */
  private createNews(news: News, image: File) {
    if (image == null) {
      this.notification.error('Please select an image');
      return;
    }
    if (this.selectedFile.size / 1024 / 1024 >= 5) {
      this.notification.error('The image is too big. Please choose a smaller one. (max. 5MB)');
      return;
    }
    const eventName = this.searchForm.get('searchBar').value;
    console.log(eventName);
    const eventFound = this.events.filter(event =>
      event.name === eventName
    );

    if (eventFound == null || eventFound.length > 1 || eventFound.length === 0) {
      this.notification.error('Please select a valid event');
      return;
    }
    news.eventId = eventFound[0].id;


    this.newsService.createNews(news, image).subscribe({
        next: () => {
          this.loadNews();
        },
        error: error => {
          this.notification.error(error.error.detail);
        }
      }
    );
  }

  /**
   * looks up event by name
   *
   * @private
   */
  private searchEvents() {
    const params = new HttpParams().set('name', this.searchEventName)
      .set('description', '')
      .set('type', '')
      .set('length', '');
    this.eventService.searchEvents(params).pipe(
      debounceTime(600)).subscribe({
      next: data => {
        this.events = data;
      },
      error: error => {
        this.notification.error(error.message, 'Could not fetch events');
      }
    });
  }

  private clearForm() {
    this.currentNews = new News();
    this.submitted = false;
    this.selectedFile = null;
    this.imagePreview = null;
    this.searchForm.reset();
  }

}
