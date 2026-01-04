FROM quay.io/wildfly/wildfly:37.0.0.Final-jdk21


RUN /opt/jboss/wildfly/bin/add-user.sh -u admin -p "admin123!" --silent
RUN /opt/jboss/wildfly/bin/add-user.sh -a -u ejbuser -p "ejb123!" --silent

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "-Djboss.bind.address=0.0.0.0"]

COPY target/beneficio-0.0.1-SNAPSHOT.jar /opt/jboss/wildfly/standalone/deployments/

# Expondo portas padrão
EXPOSE 8080 9990