import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePaymentDetailComponent } from './create-payment-detail.component';

describe('CreatePaymentDetailComponent', () => {
  let component: CreatePaymentDetailComponent;
  let fixture: ComponentFixture<CreatePaymentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreatePaymentDetailComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreatePaymentDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
