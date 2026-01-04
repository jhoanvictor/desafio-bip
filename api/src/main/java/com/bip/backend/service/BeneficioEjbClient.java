package com.bip.backend.service;


import com.bip.service.BeneficioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.math.BigDecimal;
import java.util.Properties;

@Configuration
public class BeneficioEjbClient {

    @Bean
    public static BeneficioService beneficioEjbService() throws NamingException {
        Properties jndiProps = new Properties();

        // Configurações para WildFly moderno (usando http-remoting)
        jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");

        // Ajuste o IP e a porta (8080 é o padrão http do WildFly)
        jndiProps.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
//        jndiProps.put(Context.PROVIDER_URL, "remote+http://localhost:8080");

        jndiProps.put("jboss.naming.client.ejb.context", true);

        //usado no docker
//        jndiProps.put(Context.SECURITY_PRINCIPAL, "ejbuser");
//        jndiProps.put(Context.SECURITY_CREDENTIALS, "ejb123!");

        // Se houver autenticação no WildFly, adicione:
        // jndiProps.put(Context.SECURITY_PRINCIPAL, "usuario");
        // jndiProps.put(Context.SECURITY_CREDENTIALS, "senha");

        Context context = new InitialContext(jndiProps);
        String jndiName = "ejb:/beneficio-0.0.1-SNAPSHOT/BeneficioEjbServiceImpl!com.bip.service.BeneficioService";

        return (BeneficioService) context.lookup(jndiName);
    }

    public void transfer(Long fromId, Long toId, BigDecimal amount) throws Exception{
        beneficioEjbService().transfer(fromId, toId, amount);
    }


}
