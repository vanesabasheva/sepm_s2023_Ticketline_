import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchPerformancesComponent } from './search-performances.component';

describe('SearchPerformancesComponent', () => {
  let component: SearchPerformancesComponent;
  let fixture: ComponentFixture<SearchPerformancesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchPerformancesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchPerformancesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
