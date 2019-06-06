package eu.arrowhead.common.database.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;

import eu.arrowhead.common.database.repository.ServiceRegistryRepository;
import eu.arrowhead.common.exception.InvalidParameterException;

@RunWith (SpringRunner.class)
public class ServiceRegistryDBServiceTest {
	
	@InjectMocks
	ServiceRegistryDBService serviceRegistryDBService; 
	
	@Mock
	ServiceRegistryRepository serviceRegistryRepository;
		
	@Test (expected = InvalidParameterException.class)
	public void testGetAllServiceReqistryEntriesWithInvalidSortField() {
		serviceRegistryDBService.getAllServiceReqistryEntries(0, 10, Direction.ASC, "notValid");
	}

}
