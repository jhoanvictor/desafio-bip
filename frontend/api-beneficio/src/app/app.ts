import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BeneficioService } from './services/beneficios.service';
import { Beneficio, TranferenciaRequest } from './models/beneficio.model';

@Component({
  selector: 'app-root',
  standalone: true,

  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})

export class App implements OnInit {
  private service = inject(BeneficioService);

  valorInput: string = '';

  listaBeneficios: Beneficio[] = [];

  novoBeneficio: Beneficio = { nome: '', descricao: '', valor: 0, ativo: true };

  // Objeto para controlar o Transfer
  dadosTransfer: TranferenciaRequest = { fromId: 0, toId: 0, amount: 0 };
  modoTransfer = false;

  msgSucesso: string = '';
  msgErro: string = '';

  ngOnInit(): void {
    this.carregarDados();
  }

  carregarDados() {
    this.service.findAll().subscribe({
      next: (dados) => this.listaBeneficios = dados,
      error: (e) => {
        console.error(e);
        this.msgErro = 'Erro ao conectar com o servidor.';
      }
    });
  }

  get listaDestinos() {
    return this.listaBeneficios.filter(b => b.id !== this.dadosTransfer.fromId);
  }

  salvar() {
    this.novoBeneficio.valor = this.converterParaNumero(this.valorInput)

    this.service.create(this.novoBeneficio).subscribe({
      next: () => {
        this.msgSucesso = 'Criado com sucesso!';
        this.msgErro = '';
        this.carregarDados();
        this.novoBeneficio = { nome: '', descricao: '', valor: 0, ativo: true };
      },
      error: () => this.msgErro = 'Erro ao salvar.'
    });
  }

  deletar(id: number) {
    if(confirm('Deseja excluir?')) {
      this.service.delete(id).subscribe({
        next: () => this.carregarDados(),
        error: () => this.msgErro = 'Erro ao deletar.'
      });
    }
  }

  abrirTransferencia(b: Beneficio) {
    this.modoTransfer = true;
    this.dadosTransfer.fromId = b.id!;
    this.dadosTransfer.toId = 0; // Reseta o destino
    this.dadosTransfer.amount = 0;

    this.valorInput = '';

    this.msgSucesso = '';
    this.msgErro = '';
    window.scrollTo(0,0);
  }

    confirmar() {

      this.dadosTransfer.amount = this.converterParaNumero(this.valorInput)

      if (this.dadosTransfer.toId === 0 || this.dadosTransfer.amount <= 0) {
        this.msgErro = "Selecione um destino e um valor válido.";
        return;
      }

      this.service.transferir(this.dadosTransfer).subscribe({
        next: () => {
          this.msgSucesso = 'Transferência realizada!';
          this.modoTransfer = false;
          this.carregarDados();
        },
        error: (err) => {
          this.msgErro = err.error ? JSON.stringify(err.error) : 'Erro ao processar transferencia';
        }
      });
    }

  cancelar() {
    this.modoTransfer = false;
  }

  private converterParaNumero(valorStr: string): number {
    if (!valorStr) return 0;
    const valor = valorStr.replace(/\./g, '').replace(',', '.');
    return parseFloat(valor);
  }

}
