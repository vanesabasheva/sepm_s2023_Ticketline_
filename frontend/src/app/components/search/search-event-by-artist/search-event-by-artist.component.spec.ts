import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchEventByArtistComponent } from './search-event-by-artist.component';

describe('SearchEventByArtistComponent', () => {
  let component: SearchEventByArtistComponent;
  let fixture: ComponentFixture<SearchEventByArtistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchEventByArtistComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SearchEventByArtistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
