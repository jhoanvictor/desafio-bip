package com.bip.service;

import com.bip.model.Beneficio;
import jakarta.ejb.Remote;
import jakarta.ws.rs.NotFoundException;

import java.math.BigDecimal;
import java.util.List;

@Remote
public interface BeneficioService {

    void transfer(Long fromId, Long toId, BigDecimal amount) throws Exception;

    List<Beneficio> listAll();

    Beneficio getBeneficio(long id);

    void remove(long id) throws Exception;

    Beneficio save(long id, Beneficio beneficio) throws Exception;
}
