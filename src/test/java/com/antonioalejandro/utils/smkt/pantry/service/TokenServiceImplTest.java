package com.antonioalejandro.utils.smkt.pantry.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import com.antonioalejandro.smkt.pantry.service.impl.TokenServiceImpl;

class TokenServiceImplTest {
	@Mock
	private DiscoveryClient client;

	@InjectMocks
	private TokenServiceImpl service;

	@BeforeEach
	void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetUserId() throws Exception {
		var instance = mock(ServiceInstance.class);
		when(instance.getPort()).thenReturn(8000);
		when(instance.getHost()).thenReturn("127.0.0.1");
		when(client.getInstances(Mockito.any())).thenReturn(Arrays.asList(instance));
		assertTrue(service.getUserId("").isEmpty());
	}
}
