package eu.arrowhead.common.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import eu.arrowhead.common.database.entity.OrchestratorStore;
import eu.arrowhead.common.database.entity.ServiceDefinition;
import eu.arrowhead.common.database.entity.ServiceInterface;
import eu.arrowhead.common.database.entity.System;

@Repository
public interface OrchestratorStoreRepository extends RefreshableRepository<OrchestratorStore,Long> {
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public Page<OrchestratorStore> findAllByPriority(final int priority, final Pageable pageRequest);
	public Optional<OrchestratorStore> findByConsumerSystemAndServiceDefinitionAndPriority(final System consumerSystem,	final ServiceDefinition serviceDefinition,final int priority);
	public Optional<OrchestratorStore> findByConsumerSystemAndServiceDefinitionAndProviderSystemIdAndServiceInterfaceAndForeign(final System consumerSystem, final ServiceDefinition serviceDefinition,
																																final long providerSystemId, final ServiceInterface serviceInterface,
																																final boolean foreign);
	public Page<OrchestratorStore> findAllByConsumerSystemAndServiceDefinition(final System consumerSystem,	final ServiceDefinition serviceDefinition, final Pageable pageRequest);
	public Page<OrchestratorStore> findAllByConsumerSystemAndServiceDefinitionAndServiceInterface(final System system, final ServiceDefinition serviceDefinition, 
																								  final ServiceInterface validServiceInterface,	final Pageable pageRequest);
	public List<OrchestratorStore> findAllByConsumerSystemAndServiceDefinitionAndServiceInterface(final System system, final ServiceDefinition serviceDefinition, 
																								  final ServiceInterface validServiceInterface, final Sort sortField);
	public List<OrchestratorStore> findAllByConsumerSystemAndServiceDefinitionAndServiceInterface(final System anyConsumerSystemForValidation, final ServiceDefinition serviceDefinition,
																								  final ServiceInterface serviceInterface);
		
}