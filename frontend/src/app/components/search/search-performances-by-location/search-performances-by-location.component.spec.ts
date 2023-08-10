import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchPerformancesByLocationComponent } from './search-performances-by-location.component';

describe('SearchPerformancesByLocationComponent', () => {
  let component: SearchPerformancesByLocationComponent;
  let fixture: ComponentFixture<SearchPerformancesByLocationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchPerformancesByLocationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchPerformancesByLocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
