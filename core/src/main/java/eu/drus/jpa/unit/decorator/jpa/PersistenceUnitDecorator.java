package eu.drus.jpa.unit.decorator.jpa;

import static eu.drus.jpa.unit.util.ReflectionUtils.injectValue;

import javax.persistence.EntityManagerFactory;

import eu.drus.jpa.unit.spi.TestMethodDecorator;
import eu.drus.jpa.unit.spi.TestMethodInvocation;

public class PersistenceUnitDecorator implements TestMethodDecorator {

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public void apply(final TestMethodInvocation invocation) throws Exception {
        final EntityManagerFactory emf = (EntityManagerFactory) invocation.getContext().getData("emf");

        final Class<?> fieldType = invocation.getContext().getPersistenceField().getType();
        if (fieldType.equals(EntityManagerFactory.class)) {
            injectValue(invocation.getContext().getPersistenceField(), invocation.getTarget(), emf);
        }

        invocation.proceed();
    }

}