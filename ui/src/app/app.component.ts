import { Component, inject, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { patchState } from '@ngrx/signals';
import { FormsModule } from '@angular/forms';

import { MenubarModule } from 'primeng/menubar';
import { MenuItem, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { TabViewModule } from 'primeng/tabview';
import { DividerModule } from 'primeng/divider';
import { ChartModule } from 'primeng/chart';
import { ToastModule } from 'primeng/toast';
import { SplitterModule } from 'primeng/splitter';
import { PanelModule } from 'primeng/panel';
import { ProgressBarModule } from 'primeng/progressbar';
import { ProgressSpinner } from 'primeng/progressspinner';
import { TableModule } from 'primeng/table';
import { RadioButtonModule } from 'primeng/radiobutton';

import { HistoryComponent } from './sub-components/history-component/history-component/history-component.component';
import { StockComponent } from './sub-components/stock-component/stock-component/stock-component.component';
import { StockStore } from '../../stores/stocks/stock.store';
import { PortfolioStore, TutorStore } from '../../stores';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    HistoryComponent,
    RadioButtonModule,
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
    ProgressSpinner,
    TableModule,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {
  @ViewChild(HistoryComponent) historyComponent: HistoryComponent | undefined =
    undefined;

  readonly stockStore = inject(StockStore);
  readonly tutorStore = inject(TutorStore);
  readonly portfolioStore = inject(PortfolioStore);
  readonly messageService = inject(MessageService);

  title = 'Stock Tutor';
  menuItems: MenuItem[] = [];
  learningProgress: number = 0;
  activeTabIndex: number = 0;
  totalCorrectQuestions: number = 0;
  selectedAnswer: string = '';

  ngOnInit(): void {
    this.totalCorrectQuestions = this.tutorStore
      .tutorEntities()
      .filter((q) => q.isCorrect).length;

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

  calculateLearningProgress(): number {
    const entities = this.tutorStore.tutorEntities();
    if (entities && entities.length > 0) {
      const correctAnswers = entities.filter((q) => q.isCorrect).length;
      this.learningProgress = (correctAnswers / entities.length) * 100;
    }

    return this.learningProgress;
  }

  toggleHistorySidebar(): void {
    if (!this.historyComponent) return;

    patchState(this.historyComponent.state, {
      visible: !this.historyComponent.state().visible,
    });
  }

  submitAnswer(answer: string): void {
    if (!answer || !this.tutorStore.currentTutorQuestion()) {
      return;
    }

    this.tutorStore.submitAnswer();

    const currentQuestion = this.tutorStore.currentTutorQuestion();
    const selectAnswerObj = currentQuestion.answers.find(
      (a) => a.option === answer
    );

    const isCorrect =
      selectAnswerObj &&
      currentQuestion.correctAnswer.option == selectAnswerObj.option;

    if (isCorrect) {
      this.messageService.add({
        severity: 'success',
        summary: 'Correct Answer',
        detail: 'You got it right!',
      });
      this.totalCorrectQuestions++;
      this.calculateLearningProgress();

      this.tutorStore.getRandomQuestion();
    } else {
      this.messageService.add({
        severity: 'error',
        summary: 'Wrong Answer',
        detail: `The correct answer is ${currentQuestion.correctAnswer.option}`,
      });
    }

    this.selectedAnswer = '';
  }
}
