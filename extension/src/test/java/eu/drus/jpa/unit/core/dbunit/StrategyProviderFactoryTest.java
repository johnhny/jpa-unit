package eu.drus.jpa.unit.core.dbunit;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.drus.jpa.unit.core.dbunit.CleanupStrategyProvider;
import eu.drus.jpa.unit.core.dbunit.DataSeedStrategyProvider;
import eu.drus.jpa.unit.core.dbunit.StrategyProviderFactory;

public class StrategyProviderFactoryTest {

    private static final StrategyProviderFactory FACTORY = new StrategyProviderFactory();

    @Test
    public void testCreateCleanupStrategyProvider() {
        final CleanupStrategyProvider provider = FACTORY.createCleanupStrategyProvider();

        assertThat(provider, notNullValue());
    }

    @Test
    public void testCreateDataSeedStrategyProvider() {
        final DataSeedStrategyProvider provider = FACTORY.createDataSeedStrategyProvider();

        assertThat(provider, notNullValue());
    }
}