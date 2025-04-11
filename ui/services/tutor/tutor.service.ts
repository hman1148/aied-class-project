import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { EnvironmentStore } from '../../stores';
import {
  Apis,
  CorrectAnswer,
  ItemResponse,
  ItemsResponse,
  Page,
  TutorQuestion,
} from '../../models';

@Injectable({
  providedIn: 'root',
})
export class TutorService {
  readonly env = inject(EnvironmentStore);
  readonly http = inject(HttpClient);

  getQuestion(): Observable<ItemResponse<TutorQuestion>> {
    const url = this.env.apiUrl() + Apis.Tutor + Page.Question;
    return this.http.get<ItemResponse<TutorQuestion>>(url);
  }

  getStockQuestion(ticker: string): Observable<ItemResponse<TutorQuestion>> {
    const url = this.env.apiUrl() + Apis.Tutor + Page.Question + '/' + ticker;
    return this.http.get<ItemResponse<TutorQuestion>>(url);
  }

  getRandomQuestion(): Observable<ItemResponse<TutorQuestion>> {
    const url = this.env.apiUrl() + Apis.Tutor + Page.RandomQuestion;
    return this.http.get<ItemResponse<TutorQuestion>>(url);
  }

  getAnswer(selectedQuestion: string): Observable<ItemResponse<CorrectAnswer>> {
    const url = this.env.apiUrl() + Apis.Tutor + Page.Answer;
    const requestBody = { selectedQuestion: selectedQuestion };
    return this.http.post<ItemResponse<CorrectAnswer>>(url, requestBody);
  }

  getHistory(): Observable<ItemsResponse<TutorQuestion>> {
    const url = this.env.apiUrl() + Apis.Tutor + Page.History;
    return this.http.get<ItemsResponse<TutorQuestion>>(url);
  }

  resetHistory(): Observable<ItemResponse<string>> {
    const url = this.env.apiUrl() + Apis.Tutor + Page.ResetHistory;
    return this.http.delete<ItemResponse<string>>(url);
  }
}
