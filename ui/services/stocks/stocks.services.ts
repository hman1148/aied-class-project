import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { EnvironmentStore } from '../../stores';

@Injectable({
  providedIn: 'root',
})
export class StockService {
  readonly env = inject(EnvironmentStore);
  readonly http = inject(HttpClient);
}
