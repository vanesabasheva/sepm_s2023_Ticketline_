import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PerformanceOnLocationComponent } from './performance-on-location.component';

describe('PerformanceOnLocationComponent', () => {
  let component: PerformanceOnLocationComponent;
  let fixture: ComponentFixture<PerformanceOnLocationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PerformanceOnLocationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PerformanceOnLocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
