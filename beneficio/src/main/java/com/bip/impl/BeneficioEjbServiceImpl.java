package com.bip.impl;

import com.bip.model.Beneficio;
import com.bip.service.BeneficioService;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.NotFoundException;

import java.math.BigDecimal;
import java.util.List;

@Stateless
public class BeneficioEjbServiceImpl implements BeneficioService {

    @PersistenceContext
    private EntityManager em;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void transfer(Long fromId, Long toId, BigDecimal amount) throws Exception {

        Beneficio from = em.find(Beneficio.class, fromId, LockModeType.PESSIMISTIC_WRITE);
        Beneficio to   = em.find(Beneficio.class, toId, LockModeType.PESSIMISTIC_WRITE);

        if (from == null || to == null){
            throw new Exception("Benefício não encontrado");
        }

        if (from.getId() == to.getId()){
            throw new Exception("As contas não podem ser iguais");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Valor inválido");
        }

        if (amount.compareTo(from.getValor()) > 0){
            throw new Exception("Saldo insuficiente");
        }

        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        em.merge(from);
        em.merge(to);
    }

    @Override
    public Beneficio save(long id, Beneficio beneficio) throws Exception{

        if (id > 0) {
            Beneficio beneficioOld = em.find(Beneficio.class, id);

            if (beneficioOld != null && beneficioOld.getId() > 0) {
                //update
                beneficio.setId(beneficioOld.getId());
                beneficio = em.merge(beneficio);
            } else {
                throw new Exception("Benefício não existe");
            }
        } else {
            //create
            beneficio = em.merge(beneficio);
        }

        return beneficio;
    }

    @Override
    public List<Beneficio> listAll() {
        return em.createQuery("select b from Beneficio b", Beneficio.class).getResultList();
    }

    @Override
    public Beneficio getBeneficio(long id) {
        return em.find(Beneficio.class, id);
    }

    @Override
    public void remove(long id) throws Exception {
        Beneficio beneficio = em.find(Beneficio.class, id);

        if (beneficio != null){
            em.remove(beneficio);
        } else {
            throw new Exception("Benefício não existe");
        }
    }

}
