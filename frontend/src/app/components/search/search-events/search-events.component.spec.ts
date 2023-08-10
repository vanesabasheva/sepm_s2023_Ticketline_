import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchEventsComponent } from './search-events.component';

describe('SearchEventsComponent', () => {
  let component: SearchEventsComponent;
  let fixture: ComponentFixture<SearchEventsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchEventsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchEventsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
