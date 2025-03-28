import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

import { DialogModule } from 'primeng/dialog';
import { SidebarModule } from 'primeng/sidebar';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { signalState } from '@ngrx/signals';
import { initialHistoryState } from '../history-component.state';

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
export class HistoryComponentComponent {
  readonly state = signalState(initialHistoryState());

  onHideSideBar(): void {}
}
