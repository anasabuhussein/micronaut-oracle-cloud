package io.micronaut.oraclecloud.core;

import com.oracle.bmc.auth.BasicAuthenticationDetailsProvider;
import com.oracle.bmc.auth.InstancePrincipalsAuthenticationDetailsProvider;
import io.micronaut.aop.InterceptedProxy;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.StringUtils;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.inject.Provider;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
@Property(name = InstancePrincipalConfiguration.PREFIX + ".enabled", value = StringUtils.TRUE)
public class InstancePrincipalConfigurationTest {

    @Inject
    InstancePrincipalConfiguration configuration;

    @Inject
    Provider<BasicAuthenticationDetailsProvider> detailsProvider;

    @Test
    void testConfig() {
        assertTrue(configuration.isEnabled());
        BasicAuthenticationDetailsProvider basicAuthenticationDetailsProvider = detailsProvider.get();
        BasicAuthenticationDetailsProvider target = ((InterceptedProxy<BasicAuthenticationDetailsProvider>) basicAuthenticationDetailsProvider).interceptedTarget();
        assertTrue(target instanceof InstancePrincipalsAuthenticationDetailsProvider);
    }

    @MockBean(BasicAuthenticationDetailsProvider.class)
    BasicAuthenticationDetailsProvider mockBean() {
        return Mockito.mock(InstancePrincipalsAuthenticationDetailsProvider.class);
    }
}
