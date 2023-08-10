import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditPaymentDetailComponent } from './edit-payment-detail.component';

describe('EditPaymentDetailComponent', () => {
  let component: EditPaymentDetailComponent;
  let fixture: ComponentFixture<EditPaymentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditPaymentDetailComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditPaymentDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
