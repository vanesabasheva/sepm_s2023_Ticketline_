import { TestBed } from '@angular/core/testing';

import { TopTenService } from './top-ten.service';

describe('TopTenService', () => {
  let service: TopTenService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TopTenService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
