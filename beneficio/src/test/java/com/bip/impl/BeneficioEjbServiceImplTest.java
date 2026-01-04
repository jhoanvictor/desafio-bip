package com.bip.impl;

import com.bip.model.Beneficio;
import com.bip.service.BeneficioService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficioEjbServiceImplTest {

    @Mock
    private EntityManager em;

    @InjectMocks
    private BeneficioEjbServiceImpl service;

    @Test
    void deveTransferirValorComSucessoEAtualizarSaldos() throws Exception {
        // 1. ARRANGE (Cenário)
        Long idOrigem = 1L;
        Long idDestino = 2L;
        BigDecimal valorTransferencia = new BigDecimal("100.00");

        Beneficio origem = criarBeneficio(idOrigem, "1000.00");
        Beneficio destino = criarBeneficio(idDestino, "500.00");

        // Ensinamos o Mock a responder EXATAMENTE quando chamado com o LockMode correto
        when(em.find(Beneficio.class, idOrigem, LockModeType.PESSIMISTIC_WRITE)).thenReturn(origem);
        when(em.find(Beneficio.class, idDestino, LockModeType.PESSIMISTIC_WRITE)).thenReturn(destino);

        // 2. ACT (Ação)
        service.transfer(idOrigem, idDestino, valorTransferencia);

        // 3. ASSERT (Validação)
        // Valida se os valores em memória foram alterados corretamente
        assertEquals(new BigDecimal("900.00"), origem.getValor()); // 1000 - 100
        assertEquals(new BigDecimal("600.00"), destino.getValor()); // 500 + 100

        // Valida se o EntityManager fez o merge (salvou) os dois objetos
        verify(em).merge(origem);
        verify(em).merge(destino);
    }

    // --- CENÁRIOS DE ERRO (EXCEPTIONS) ---

    @Test
    void deveLancarErroQuandoBeneficioNaoEncontrado() {
        Long idOrigem = 1L;
        Long idDestino = 2L;

        // Simula que a origem não existe (retorna null)
        when(em.find(Beneficio.class, idOrigem, LockModeType.PESSIMISTIC_WRITE)).thenReturn(null);
        // O destino nem precisa ser mockado pois vai falhar antes

        Exception exception = assertThrows(Exception.class, () -> {
            service.transfer(idOrigem, idDestino, new BigDecimal("100.00"));
        });

        assertEquals("Benefício não encontrado", exception.getMessage());

        // Garante que nada foi salvo
        verify(em, never()).merge(any());
    }

    @Test
    void deveLancarErroQuandoContasForemIguais() {
        Long idIgual = 5L;

        // Retorna dois objetos diferentes na memória, mas com o mesmo ID
        Beneficio b1 = criarBeneficio(idIgual, "100.00");
        Beneficio b2 = criarBeneficio(idIgual, "200.00");

        when(em.find(Beneficio.class, idIgual, LockModeType.PESSIMISTIC_WRITE)).thenReturn(b1);
        // Note: no seu código, se você passar o mesmo ID nos parametros, o find será chamado 2x com o mesmo ID

        Exception exception = assertThrows(Exception.class, () -> {
            // Passando o mesmo ID na origem e destino
            service.transfer(idIgual, idIgual, new BigDecimal("50.00"));
        });

        assertEquals("As contas não podem ser iguais", exception.getMessage());
        verify(em, never()).merge(any());
    }

    @Test
    void deveLancarErroSeValorForZeroOuNegativo() {
        Long idOrigem = 1L;
        Long idDestino = 2L;

        Beneficio origem = criarBeneficio(idOrigem, "100.00");
        Beneficio destino = criarBeneficio(idDestino, "100.00");

        when(em.find(Beneficio.class, idOrigem, LockModeType.PESSIMISTIC_WRITE)).thenReturn(origem);
        when(em.find(Beneficio.class, idDestino, LockModeType.PESSIMISTIC_WRITE)).thenReturn(destino);

        // Teste com ZERO
        Exception exZero = assertThrows(Exception.class, () -> {
            service.transfer(idOrigem, idDestino, BigDecimal.ZERO);
        });
        assertEquals("Valor inválido", exZero.getMessage());

        // Teste com NEGATIVO
        Exception exNegativo = assertThrows(Exception.class, () -> {
            service.transfer(idOrigem, idDestino, new BigDecimal("-10.00"));
        });
        assertEquals("Valor inválido", exNegativo.getMessage());

        verify(em, never()).merge(any());
    }

    @Test
    void deveLancarErroSeSaldoInsuficiente() {
        Long idOrigem = 1L;
        Long idDestino = 2L;

        // Origem tem apenas 50.00
        Beneficio origem = criarBeneficio(idOrigem, "50.00");
        Beneficio destino = criarBeneficio(idDestino, "100.00");

        when(em.find(Beneficio.class, idOrigem, LockModeType.PESSIMISTIC_WRITE)).thenReturn(origem);
        when(em.find(Beneficio.class, idDestino, LockModeType.PESSIMISTIC_WRITE)).thenReturn(destino);

        // Tenta transferir 100.00
        Exception exception = assertThrows(Exception.class, () -> {
            service.transfer(idOrigem, idDestino, new BigDecimal("100.00"));
        });

        assertEquals("Saldo insuficiente", exception.getMessage());
        verify(em, never()).merge(any());
    }

    // --- MÉTODOS AUXILIARES ---

    private Beneficio criarBeneficio(Long id, String valor) {
        Beneficio b = new Beneficio();
        b.setId(id);
        b.setValor(new BigDecimal(valor));
        return b;
    }
    
}