import { patchState, signalState } from '@ngrx/signals';
import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';

// PrimeNG Components
import { CardModule } from 'primeng/card';
import { DialogModule } from 'primeng/dialog';
import { DividerModule } from 'primeng/divider';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ButtonModule } from 'primeng/button';
import { InputNumberModule } from 'primeng/inputnumber';
import { RadioButtonModule } from 'primeng/radiobutton';
import { MessageModule } from 'primeng/message';
import { TableModule } from 'primeng/table';

// Application Imports
import { initialStockComponentState } from '../stock-component.state';
import { StockStore } from '../../../../../stores/stocks/stock.store';
import { TutorStore } from '../../../../../stores/tutor/tutor.store';
import { Stock } from '../../../../../models';

@Component({
  selector: 'app-stock-component',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DialogModule,
    CardModule,
    DividerModule,
    ProgressSpinnerModule,
    ButtonModule,
    InputNumberModule,
    RadioButtonModule,
    MessageModule,
    TableModule,
  ],
  templateUrl: './stock-component.component.html',
  styleUrl: './stock-component.component.scss',
})
export class StockComponent {
  readonly state = signalState(initialStockComponentState());
  readonly stockStore = inject(StockStore);
  readonly tutorStore = inject(TutorStore);

  onRowSelect(event: any): void {
    const selectedStock = event.data;
    this.stockStore.resolveStock(selectedStock.tickerSymobol);
    patchState(this.state, { quantity: 1 });
  }

  onPurchase(): void {
    if (!this.stockStore.currentStock() || this.state().quantity <= 0) {
      return;
    }

    patchState(this.state, {
      showPurchasingDialog: true,
      selectedAnswer: '',
    });

    this.loadTutorQuestion();
  }

  /**
   * Load tutor question for current stock
   */
  loadTutorQuestion(): void {
    if (!this.stockStore.currentStock()) return;

    const ticker: string = this.stockStore.currentStock().tickerSymobol;
    this.tutorStore.getStockQuestion(ticker);
  }

  /**
   * Cancel purchase and close dialog
   */
  onCancelPurchase(): void {
    patchState(this.state, {
      showPurchasingDialog: false,
      selectedAnswer: '',
    });
  }
}
