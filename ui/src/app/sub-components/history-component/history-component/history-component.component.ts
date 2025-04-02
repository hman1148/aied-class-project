import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';

import { DialogModule } from 'primeng/dialog';
import { SidebarModule } from 'primeng/sidebar';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { patchState, signalState } from '@ngrx/signals';
import { initialHistoryState } from '../history-component.state';
import { HistoryStore } from '../../../../../stores';

@Component({
  selector: 'app-history-component',
  standalone: true,
  imports: [
    CommonModule,
    DialogModule,
    SidebarModule,
    CardModule,
    DividerModule,
    ProgressSpinnerModule,
  ],
  templateUrl: './history-component.component.html',
  styleUrl: './history-component.component.scss',
})
export class HistoryComponent {
  readonly state = signalState(initialHistoryState());
  readonly historyStore = inject(HistoryStore);

  onSelectHistoryItem(question: string): void {
    this.historyStore.getHistoryByQuestion(question);
  }

  onHideSideBar(): void {
    patchState(this.state, { visible: false });
  }
}
