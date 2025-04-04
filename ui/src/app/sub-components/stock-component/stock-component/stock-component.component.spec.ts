import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StockComponentComponent } from './stock-component.component';

describe('StockComponentComponent', () => {
  let component: StockComponentComponent;
  let fixture: ComponentFixture<StockComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StockComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StockComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
