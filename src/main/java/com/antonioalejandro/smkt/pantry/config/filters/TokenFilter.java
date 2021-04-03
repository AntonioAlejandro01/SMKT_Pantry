package com.antonioalejandro.smkt.pantry.config.filters;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.antonioalejandro.smkt.pantry.service.TokenService;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class TokenFilter.
 */
@Component
@Order(1)
@Slf4j
public class TokenFilter implements Filter {

	/** The token service. */
	@Autowired
	private TokenService tokenService;

	/**
	 * Do filter.
	 *
	 * @param request the request
	 * @param response the response
	 * @param chain the chain
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ServletException the servlet exception
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse myResponse = (HttpServletResponse) response;

		Optional<String> token = Optional.ofNullable(httpRequest.getHeader("Authorization"))
				.map(tok -> tok.split(" ")[1]);

		if (token.isEmpty()) {
			log.info("Request without token");
			myResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
			return;
		}
		HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(httpRequest);
		Optional<String> userId = tokenService.getUserId(token.get());
		if (userId.isEmpty()) {
			log.info("Token is not valid");
			myResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
			return;
		}
		requestWrapper.addHeader("userID",userId.get());
		chain.doFilter(requestWrapper, response);
	}

	/**
	 * The Class HeaderMapRequestWrapper.
	 */
	public class HeaderMapRequestWrapper extends HttpServletRequestWrapper {
		/**
		 * construct a wrapper for this request
		 * 
		 * @param request
		 */
		public HeaderMapRequestWrapper(HttpServletRequest request) {
			super(request);
		}

		/** The header map. */
		private Map<String, String> headerMap = new HashMap<>();

		/**
		 * add a header with given name and value
		 * 
		 * @param name
		 * @param value
		 */
		public void addHeader(String name, String value) {
			headerMap.put(name, value);
		}

		/**
		 * Gets the header.
		 *
		 * @param name the name
		 * @return the header
		 */
		@Override
		public String getHeader(String name) {
			String headerValue = super.getHeader(name);
			if (headerMap.containsKey(name)) {
				headerValue = headerMap.get(name);
			}
			return headerValue;
		}

		/**
		 * get the Header names
		 */
		@Override
		public Enumeration<String> getHeaderNames() {
			List<String> names = Collections.list(super.getHeaderNames());
			for (String name : headerMap.keySet()) {
				names.add(name);
			}
			return Collections.enumeration(names);
		}

		/**
		 * Gets the headers.
		 *
		 * @param name the name
		 * @return the headers
		 */
		@Override
		public Enumeration<String> getHeaders(String name) {
			List<String> values = Collections.list(super.getHeaders(name));
			if (headerMap.containsKey(name)) {
				values.add(headerMap.get(name));
			}
			return Collections.enumeration(values);
		}

	}

}
