package eu.drus.jpa.unit.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import eu.drus.jpa.unit.api.Cleanup;
import eu.drus.jpa.unit.api.CleanupCache;
import eu.drus.jpa.unit.api.CleanupPhase;
import eu.drus.jpa.unit.api.InitialDataSets;
import eu.drus.jpa.unit.api.JpaUnitRunner;
import eu.drus.jpa.unit.test.model.Depositor;

@RunWith(JpaUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CleanupCacheTest {

    @PersistenceContext(unitName = "my-cache-unit")
    private EntityManager manager;

    @Before
    public void configureEntityManager() {
        // These are defaults but put here for documentation purposes
        manager.setProperty("javax.persistence.cache.storeMode", CacheStoreMode.USE);
        manager.setProperty("javax.persistence.cache.retrieveMode", CacheRetrieveMode.USE);
    }

    @Test
    @InitialDataSets("datasets/initial-data.json")
    public void test1() {
        final Depositor entity = manager.find(Depositor.class, 100L);

        assertNotNull(entity);
        entity.setName("David");
    }

    @Test
    @Cleanup(phase = CleanupPhase.BEFORE)
    public void test2() {
        // Even the DB is explicitly deleted before the actual test start, we can still find our
        // entity thanks to the used second level cache
        final Depositor entity = manager.find(Depositor.class, 100L);
        assertNotNull(entity);

        assertThat(entity.getName(), equalTo("David"));
    }

    @Test
    @CleanupCache(value = true, phase = CleanupPhase.BEFORE)
    public void test3() {
        // Cleaning the second level cache will make it now impossible to find the entity
        final Depositor entity = manager.find(Depositor.class, 100L);
        assertNull(entity);
    }

    @Test
    @InitialDataSets("datasets/initial-data.json")
    public void test4() {
        final Depositor entity = manager.find(Depositor.class, 100L);

        assertNotNull(entity);
    }

    @Test
    @CleanupCache(value = true, phase = CleanupPhase.AFTER)
    public void test5() {
        // Even the DB is implicitly deleted after the previous test, we can still find our
        // entity thanks to the used second level cache
        final Depositor entity = manager.find(Depositor.class, 100L);
        assertNotNull(entity);

        // Cleaning the second level cache will make it impossible for the next test to find the
        // entity
    }

    @Test
    public void test6() {
        final Depositor entity = manager.find(Depositor.class, 100L);
        assertNull(entity);
    }
}
