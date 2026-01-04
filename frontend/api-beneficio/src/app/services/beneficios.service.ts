import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Beneficio, TranferenciaRequest } from '../models/beneficio.model';

@Injectable({
  providedIn: 'root'
})
export class BeneficioService {

  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:8089/api-rest/api/v1/beneficios';

  findAll(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(this.apiUrl);
  }

  findBeneficio(id: number): Observable<Beneficio> {
    return this.http.get<Beneficio>(`${this.apiUrl}/${id}`);
  }

  create(beneficio: Beneficio): Observable<Beneficio> {
    return this.http.post<Beneficio>(this.apiUrl, beneficio);
  }

  update(id: number, beneficio: Beneficio): Observable<Beneficio> {
    return this.http.put<Beneficio>(`${this.apiUrl}/${id}`, beneficio);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  transferir(dados: TranferenciaRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/transferir`, dados);
  }
}
