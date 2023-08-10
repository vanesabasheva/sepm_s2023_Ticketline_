import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateDeliveryLocationComponent } from './create-delivery-location.component';

describe('CreateDeliveryLocationComponent', () => {
  let component: CreateDeliveryLocationComponent;
  let fixture: ComponentFixture<CreateDeliveryLocationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateDeliveryLocationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateDeliveryLocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
