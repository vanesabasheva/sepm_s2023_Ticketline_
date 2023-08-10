import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MerchandiseEventComponent } from './merchandise-event.component';

describe('MerchandiseEventComponent', () => {
  let component: MerchandiseEventComponent;
  let fixture: ComponentFixture<MerchandiseEventComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MerchandiseEventComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MerchandiseEventComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
