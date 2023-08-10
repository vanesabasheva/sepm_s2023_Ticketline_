import { TestBed } from '@angular/core/testing';

import { NewsService } from './news.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';

describe('NewsService', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        ReactiveFormsModule,
      ],
    })
  );

  it('should be created', () => {
    const service: NewsService = TestBed.inject(NewsService);
    expect(service).toBeTruthy();
  });
});
