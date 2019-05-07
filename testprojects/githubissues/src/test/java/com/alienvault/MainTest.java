package com.alienvault;

import com.alienvault.model.RepositoryRequestObj;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RepositoryQueryUtil.class)
public class MainTest {

	@BeforeEach
	public void setUp() throws Exception {
		PowerMockito.mockStatic(RepositoryQueryUtil.class);
	}

	@AfterEach
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testQuery() {
	    final RepositoryRequestObj value = new RepositoryRequestObj();
		Mockito.when(RepositoryQueryUtil.query(Mockito.any(), Mockito.any())).thenReturn(value);
		
	}

}
