import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { MenubarModule } from 'primeng/menubar';
import { MenuItem } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { TabViewModule } from 'primeng/tabview';
import { DividerModule } from 'primeng/divider';
import { ChartModule } from 'primeng/chart';
import { ToastModule } from 'primeng/toast';
import { SplitterModule } from 'primeng/splitter';
import { PanelModule } from 'primeng/panel';
import { AvatarModule } from 'primeng/avatar';
import { ProgressBarModule } from 'primeng/progressbar';

import { HistoryComponent } from './sub-components/history-component/history-component/history-component.component';
import { StockComponent } from './sub-components/stock-component/stock-component/stock-component.component';
import { StockStore } from '../../stores/stocks/stock.store';
import { TutorStore } from '../../stores';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    RouterOutlet,
    HistoryComponent,
    StockComponent,
    FormsModule,
    MenubarModule,
    ButtonModule,
    CardModule,
    TabViewModule,
    DividerModule,
    ChartModule,
    ToastModule,
    SplitterModule,
    PanelModule,
    ProgressBarModule,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {
  @ViewChild(HistoryComponent) historyComponent: HistoryComponent | undefined =
    undefined;

  readonly stockStore = inject(StockStore);
  readonly tutorStore = inject(TutorStore);

  title = 'Stock Tutor';
  menuItems: MenuItem[] = [];
  learningProgress: number = 0;
  activeTabIndex: number = 0;

  ngOnInit(): void {
    this.menuItems = [
      {
        label: 'Dashboard',
        icon: 'pi pi-home',
        command: () => (this.activeTabIndex = 0),
      },
      {
        label: 'Stock Market',
        icon: 'pi pi-chart-line',
        command: () => (this.activeTabIndex = 1),
      },
      {
        label: 'Portfolio',
        icon: 'pi pi-briefcase',
        command: () => (this.activeTabIndex = 2),
      },
      {
        label: 'Learning',
        icon: 'pi pi-book',
        command: () => (this.activeTabIndex = 3),
      },
    ];
  }

  get portfolioChartData() {
    return {
      labels: [
        'Technology',
        'Healthcare',
        'Finance',
        'Energy',
        'Consumer Goods',
      ],
      datasets: [
        {
          data: [40, 20, 15, 10, 15],
          backgroundColor: [
            '#FF6384',
            '#36A2EB',
            '#FFCE56',
            '#4BC0C0',
            '#9966FF',
          ],
        },
      ],
    };
  }

  get chartOptions() {
    return {
      plugins: {
        legend: {
          position: 'bottom',
        },
      },
    };
  }

  calculateLearningProgress(): void {
    const entities = this.tutorStore.tutorEntities();
    if (entities && entities.length > 0) {
      const correctAnswers = entities.filter((q) => q.isCorrect).length;
      this.learningProgress = (correctAnswers / entities.length) * 100;
    }
  }

  showHistory(): void {
    if (this.historyComponent) {
      this.historyComponent.state.update((state) => ({
        ...state,
        visible: true,
      }));
    }
  }
}
