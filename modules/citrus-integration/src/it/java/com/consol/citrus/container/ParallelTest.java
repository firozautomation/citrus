package com.consol.citrus.container;

import org.testng.annotations.Test;

import com.consol.citrus.AbstractIntegrationTest;

/**
 * 
 * @author deppisch Christoph Deppisch Consol* Software GmbH
 * @since 31.10.2008
 */
public class ParallelTest extends AbstractIntegrationTest {
    @Test
    public void iterateTest() {
        executeTest();
    }
}