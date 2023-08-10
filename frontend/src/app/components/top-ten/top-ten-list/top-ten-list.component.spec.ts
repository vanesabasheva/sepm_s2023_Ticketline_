import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopTenListComponent } from './top-ten-list.component';

describe('TopTenListComponent', () => {
  let component: TopTenListComponent;
  let fixture: ComponentFixture<TopTenListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopTenListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TopTenListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
