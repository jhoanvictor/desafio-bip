package com.bip.impl;

import com.bip.model.Beneficio;
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

        Long idOrigem = 1L;
        Long idDestino = 2L;
        BigDecimal valorTransferencia = new BigDecimal("100.00");

        Beneficio origem = criarBeneficio(idOrigem, "1000.00");
        Beneficio destino = criarBeneficio(idDestino, "500.00");

        when(em.find(Beneficio.class, idOrigem, LockModeType.PESSIMISTIC_WRITE)).thenReturn(origem);
        when(em.find(Beneficio.class, idDestino, LockModeType.PESSIMISTIC_WRITE)).thenReturn(destino);

        service.transfer(idOrigem, idDestino, valorTransferencia);

        assertEquals(new BigDecimal("900.00"), origem.getValor());
        assertEquals(new BigDecimal("600.00"), destino.getValor());

        verify(em).merge(origem);
        verify(em).merge(destino);
    }

    @Test
    void deveLancarErroQuandoBeneficioNaoEncontrado() {
        Long idOrigem = 1L;
        Long idDestino = 2L;

        when(em.find(Beneficio.class, idOrigem, LockModeType.PESSIMISTIC_WRITE)).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            service.transfer(idOrigem, idDestino, new BigDecimal("100.00"));
        });

        assertEquals("Benefício não encontrado", exception.getMessage());

        verify(em, never()).merge(any());
    }

    @Test
    void deveLancarErroQuandoContasForemIguais() {
        Long idIgual = 5L;

        Beneficio b1 = criarBeneficio(idIgual, "100.00");
        Beneficio b2 = criarBeneficio(idIgual, "200.00");

        when(em.find(Beneficio.class, idIgual, LockModeType.PESSIMISTIC_WRITE)).thenReturn(b1);

        Exception exception = assertThrows(Exception.class, () -> {
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

        Exception exZero = assertThrows(Exception.class, () -> {
            service.transfer(idOrigem, idDestino, BigDecimal.ZERO);
        });
        assertEquals("Valor inválido", exZero.getMessage());

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

        Beneficio origem = criarBeneficio(idOrigem, "50.00");
        Beneficio destino = criarBeneficio(idDestino, "100.00");

        when(em.find(Beneficio.class, idOrigem, LockModeType.PESSIMISTIC_WRITE)).thenReturn(origem);
        when(em.find(Beneficio.class, idDestino, LockModeType.PESSIMISTIC_WRITE)).thenReturn(destino);

        Exception exception = assertThrows(Exception.class, () -> {
            service.transfer(idOrigem, idDestino, new BigDecimal("100.00"));
        });

        assertEquals("Saldo insuficiente", exception.getMessage());
        verify(em, never()).merge(any());
    }

    private Beneficio criarBeneficio(Long id, String valor) {
        Beneficio b = new Beneficio();
        b.setId(id);
        b.setValor(new BigDecimal(valor));
        return b;
    }
    
}