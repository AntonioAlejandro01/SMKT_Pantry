package com.antonioalejandro.utils.smkt.pantry.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import com.antonioalejandro.smkt.pantry.db.PantryDatabaseImpl;
import com.antonioalejandro.smkt.pantry.model.Category;
import com.antonioalejandro.smkt.pantry.model.Product;
import com.antonioalejandro.smkt.pantry.model.dto.ProductDTO;
import com.antonioalejandro.smkt.pantry.model.exceptions.PantryException;
import com.antonioalejandro.smkt.pantry.service.impl.ProductServiceImpl;

class ProductServiceTest {

	@Mock
	private PantryDatabaseImpl db;

	@Mock
	private DiscoveryClient client;

	@InjectMocks
	private ProductServiceImpl service;

	@BeforeEach
	void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testAll() throws Exception {
		when(db.findAll(Mockito.anyString())).thenReturn(Optional.empty());
		assertTrue(service.all("Admin").isEmpty());
	}

	@Test
	void testById() throws Exception {
		when(db.findById(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());
		assertTrue(service.byId("Admin", UUID.randomUUID().toString()).isEmpty());
	}

	@Test
	void testbyFilter() throws Exception {
		when(db.findByName(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());
		assertTrue(service.byFilter("Admin", "NAME", "TEST").isEmpty());
	}

	@Test
	void testAddCategoryNotValid() throws Exception {
		var product = new ProductDTO();
		product.setCategory(0); // categories valid are [1,5]

		assertThrows(PantryException.class, () -> {
			service.add("ADMIN", product);
		});

	}

	@Test
	void testAdd() throws Exception {
		when(db.insertProduct(Mockito.any(Product.class))).thenReturn(Optional.empty());
		var product = new ProductDTO();
		product.setCategory(1);

		assertTrue(service.add("ADMIN", product).isEmpty());

	}

	@Test
	void testUpdateCategoryNotValid() throws Exception {
		var product = new ProductDTO();
		product.setCategory(0); // categories valid are [1,5]

		assertThrows(PantryException.class, () -> {
			service.update("ADMIN", "ID", product);
		});

	}

	@Test
	void testUpdate() throws Exception {
		when(db.updateProduct(Mockito.anyString(), Mockito.anyString(), Mockito.any(Product.class)))
				.thenReturn(Optional.empty());
		var product = new ProductDTO();
		product.setCategory(1);

		assertTrue(service.update("ADMIN", "ID", product).isEmpty());

	}

	@Test
	void testAddAmountError() throws Exception {
		when(db.addAmountById(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(false);
		assertThrows(PantryException.class, () -> {
			service.addAmount("USERID", "ID", 1);
		});

	}

	@Test
	void testAddAmount() throws Exception {
		when(db.addAmountById(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(true);
		assertDoesNotThrow(() -> {
			service.addAmount("USERID", "ID", 1);
		});
	}

	@Test
	void testRemoveAmountError() throws Exception {
		when(db.removeAmountById(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(false);
		assertThrows(PantryException.class, () -> {
			service.removeAmount("USERID", "ID", 1);
		});

	}

	@Test
	void testRemoveAmount() throws Exception {
		when(db.removeAmountById(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(true);
		assertDoesNotThrow(() -> {
			service.removeAmount("USERID", "ID", 1);
		});
	}

	@Test
	void testDeleteError() throws Exception {
		when(db.deleteProduct(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		assertThrows(PantryException.class, () -> {
			service.delete("USERID", "ID");
		});
	}

	@Test
	void testDelete() throws Exception {
		when(db.deleteProduct(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		assertDoesNotThrow(() -> {
			service.delete("USERID", "ID");
		});
	}

	@Test
	void testGetExcelEmptyProducts() throws Exception {
		when(db.findAll(Mockito.anyString())).thenReturn(Optional.empty());
		assertThrows(PantryException.class, () -> {
			service.getExcel("USERID", "TOKEN");
		});
	}

	@Test
	void testGetExcelEmptyListProducts() throws Exception {
		when(db.findAll(Mockito.anyString())).thenReturn(Optional.of(List.of()));
		assertThrows(PantryException.class, () -> {
			service.getExcel("USERID", "TOKEN");
		});
	}

	@Test
	void testGetExcelServiceUnavailable() throws Exception {
		var product = new Product();

		product.setAmount(1);
		product.setCategory(new Category(1, "CATEGORY"));
		product.setCodeKey("CODEKEY");
		product.setId("ID");
		product.setName("NAME");
		product.setPrice(0.23d);

		when(db.findAll(Mockito.anyString())).thenReturn(Optional.of(List.of(product)));
		when(client.getInstances(Mockito.anyString())).thenReturn(new ArrayList<>());
		assertThrows(PantryException.class, () -> {
			service.getExcel("USERID", "TOKEN");
		});
	}

	@Test
	void testGetExcelErrorMapper() throws Exception {
		when(db.findAll(Mockito.anyString())).thenReturn(Optional.of(List.of(new Product())));
		assertThrows(PantryException.class, () -> {
			service.getExcel("USERID", "TOKEN");
		});
	}

	@Test
	void testGetExcel() throws Exception {
		var product = new Product();

		product.setAmount(1);
		product.setCategory(new Category(1, "CATEGORY"));
		product.setCodeKey("CODEKEY");
		product.setId("ID");
		product.setName("NAME");
		product.setPrice(0.23d);

		var instance = mock(ServiceInstance.class);
		when(instance.getPort()).thenReturn(8000);
		when(instance.getHost()).thenReturn("127.0.0.1");
		when(client.getInstances(Mockito.any())).thenReturn(Arrays.asList(instance));
		when(db.findAll(Mockito.anyString())).thenReturn(Optional.of(Arrays.asList(product)));
		assertThrows(PantryException.class, () -> {
			service.getExcel("USERID", "TOKEN");
		});
	}
}
