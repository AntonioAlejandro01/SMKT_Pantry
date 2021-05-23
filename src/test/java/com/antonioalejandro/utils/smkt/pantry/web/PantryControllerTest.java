package com.antonioalejandro.utils.smkt.pantry.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.antonioalejandro.smkt.pantry.model.Product;
import com.antonioalejandro.smkt.pantry.model.dto.ProductDTO;
import com.antonioalejandro.smkt.pantry.model.exceptions.PantryException;
import com.antonioalejandro.smkt.pantry.service.impl.ProductServiceImpl;
import com.antonioalejandro.smkt.pantry.utils.Validations;
import com.antonioalejandro.smkt.pantry.web.PantryController;

class PantryControllerTest {

	@Mock
	private ProductServiceImpl service;

	@Mock
	private Validations validations;

	@InjectMocks
	private PantryController controller;

	@BeforeEach
	void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetAllOk() throws Exception {
		when(service.all(Mockito.anyString())).thenReturn(Optional.of(List.of(new Product())));
		assertEquals(HttpStatus.OK, controller.getAll("Admin").getStatusCode());

	}

	@Test
	void testGetAllNoContent() throws Exception {
		when(service.all(Mockito.anyString())).thenReturn(Optional.of(List.of()));
		assertEquals(HttpStatus.NO_CONTENT, controller.getAll("Admin").getStatusCode());

	}

	@Test
	void testById() throws Exception {
		when(service.byId(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(new Product()));
		assertEquals(HttpStatus.OK, controller.getById("Admin", UUID.randomUUID().toString()).getStatusCode());
	}

	@Test
	void testGetCategories() throws Exception {
		assertEquals(HttpStatus.OK, controller.getCategories().getStatusCode());
	}

	@Test
	void testSearch() throws Exception {
		when(service.byFilter(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenReturn(Optional.of(List.of(new Product())));
		assertEquals(HttpStatus.OK, controller.search("Admin", "Filter", "Value").getStatusCode());
	}

	@Test
	void testGetFilter() throws Exception {
		assertEquals(HttpStatus.OK, controller.getFilter().getStatusCode());
	}

	@Test
	void testExcelEmpty() throws Exception {
		when(service.getExcel(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(new byte[] {}));
		assertEquals(HttpStatus.NO_CONTENT, controller.getExcel("Admin", "TOKEN").getStatusCode());
	}

	@Test
	void testExcel() throws Exception {
		when(service.getExcel(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(new byte[] { 12 }));
		assertEquals(HttpStatus.OK, controller.getExcel("Admin", "TOKEN").getStatusCode());
	}

	@Test
	void testPostProduct() throws Exception {
		when(service.add(Mockito.anyString(), Mockito.any(ProductDTO.class))).thenReturn(Optional.of(new Product()));
		assertEquals(HttpStatus.OK, controller.postProduct("Admin", new ProductDTO()).getStatusCode());
	}

	@Test
	void testPutProduct() throws Exception {
		when(service.update(Mockito.anyString(), Mockito.anyString(), Mockito.any(ProductDTO.class)))
				.thenReturn(Optional.of(new Product()));
		assertEquals(HttpStatus.OK, controller.putProduct("Admin", "Id", new ProductDTO()).getStatusCode());
	}

	@Test
	void testAddAmount() throws Exception {
		doNothing().when(service).addAmount(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
		assertEquals(HttpStatus.NO_CONTENT, controller.addAmountToProduct("userId", "Id", 0).getStatusCode());
	}

	@Test
	void testRemoveAmount() throws Exception {
		doNothing().when(service).removeAmount(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
		assertEquals(HttpStatus.NO_CONTENT, controller.removeAmountToProduct("UserID", "Id", 0).getStatusCode());
	}
	
	@Test
	void testDeleteProduct() throws Exception {
		doNothing().when(service).delete(Mockito.anyString(), Mockito.anyString());
		assertEquals(HttpStatus.ACCEPTED, controller.deleteProduct("UserID", "Id").getStatusCode());
	}
	
	
	@Test
	void testHandlerErrorService() throws Exception {
		var error = new PantryException(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		var response = controller.handleErrorService(error);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertTrue(response.getBody().contains(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
	}

}
