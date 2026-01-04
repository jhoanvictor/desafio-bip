export interface Beneficio {
  id?: number;
  nome: string;
  descricao: string;
  valor: number;
  ativo: boolean;
}

export interface TranferenciaRequest {
  fromId: number;
  toId: number;
  amount: number;
}
