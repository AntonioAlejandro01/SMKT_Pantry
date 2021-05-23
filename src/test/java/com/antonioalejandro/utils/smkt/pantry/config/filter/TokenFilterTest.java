package com.antonioalejandro.utils.smkt.pantry.config.filter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import com.antonioalejandro.smkt.pantry.config.filters.TokenFilter;
import com.antonioalejandro.smkt.pantry.service.TokenService;

class TokenFilterTest {

	private static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJBZG1pbiIsInNjb3BlIjpbImZpbGVzLmV4Y2VsIiwidXNlcnMuc3VwZXIiLCJmaWxlcy5wZGYiXSwibmFtZSI6IkFkbWluIiwiZXhwIjoxNjIxMTA1OTQ2LCJhdXRob3JpdGllcyI6WyJTVVBFUkFETUlOIl0sImp0aSI6ImIxM2M2NGU0LWI2YjUtNDhiYy04NTRiLWI2OWQyZTU0MTc2ZCIsImVtYWlsIjoiYWRtaW5AYWRtaW4uY29tIiwiY2xpZW50X2lkIjoic21hcnRraXRjaGVuYXBwIiwibGFzdG5hbWUiOiJBZG1pbiIsInVzZXJuYW1lIjoiQWRtaW4ifQ.YmmIJ_urTUOybmn1G7DQ8XCAteujM2fx7H5exgky_Tc";

	@Mock
	private TokenService service;

	@InjectMocks
	private TokenFilter filter;

	@BeforeEach
	void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testSecureEndpointNotToken() throws Exception {
		var req = mock(HttpServletRequest.class);
		var res = mock(HttpServletResponse.class);
		when(req.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

		when(req.getRequestURI()).thenReturn("/products/");

		assertDoesNotThrow(() -> {
			filter.doFilter(req, res, mock(FilterChain.class));
		});

	}

	@Test
	void testSecureEndpointNotUserId() throws Exception {
		var req = mock(HttpServletRequest.class);
		var res = mock(HttpServletResponse.class);
		when(req.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(TOKEN);
		when(service.getUserId(Mockito.anyString())).thenReturn(Optional.empty());
		when(req.getRequestURI()).thenReturn("/products/");

		assertDoesNotThrow(() -> {
			filter.doFilter(req, res, mock(FilterChain.class));
		});

	}

	@Test
	void testSecureEndpointOk() throws Exception {
		var req = mock(HttpServletRequest.class);
		var res = mock(HttpServletResponse.class);
		when(req.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(TOKEN);
		when(service.getUserId(Mockito.anyString())).thenReturn(Optional.of("Admin"));
		when(req.getRequestURI()).thenReturn("/products/");

		assertDoesNotThrow(() -> {
			filter.doFilter(req, res, mock(FilterChain.class));
		});

	}

	@Test
	void testNotSecureEndpointOk() throws Exception {
		var req = mock(HttpServletRequest.class);
		var res = mock(HttpServletResponse.class);
		when(req.getRequestURI()).thenReturn("/swagger-ui.html");

		assertDoesNotThrow(() -> {
			filter.doFilter(req, res, mock(FilterChain.class));
		});

	}

}
