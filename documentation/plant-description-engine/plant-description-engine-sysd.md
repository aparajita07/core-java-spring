# Plant Description Engine HTTP(S)/JSON - System Description (SysD)

## Abstract
This document describes a system useful for choreographing the consumers and producers in the plant (System of Systems / Local cloud).
In particular the system uses an abstract view of the plant and populates the [Orchestrator] with store rules for each of the consumers.

## Overview

This document describes the HTTP/{TLS}/JSON Plant Description Engine (PDE) Arrowhead Framework system.
This supporting core system has the purpose of choreographing the consumers and producers in the plant (System of Systems / Local cloud).
An abstract view, on which systems the plant contains and how they are connected as consumers and producers, is used to populate the [Orchestrator] with store rules for each of the consumers. The abstract view does not contain any instance specific information, instead meta-data about each system is used to identify the service producers.

The plant description engine (PDE) can be configured, using the [Plant Description Management JSON] service, with several variants of the plant description of which at most one can be active.
The active plant description is used to populate the orchestrator and if no plant description is active the orchestrator does not contain any store rules populated by the PDE. This can be used to establish alternativ plants (plan A, plan B, etc).

The PDE gathers information about the presence of all specified systems in the active plant description. If a system is not present it raises an alarm. If it detects that an unknown system has registered a service in the service registry it also raises an alarm. For a consumer system to be monitored the system must produce the [Monitorable] service and hence also register in the service registry. The [Plant Description Alarm JSON] service can be used to inspect and manage any raised alarms.

Tentatively, in the future the PDE can gather system specific data from all systems in the plant that produces the [Monitorable] service. Furthermore, the PDE could collect information from an [Inventory]. Both of these additional data could then be returned in the Plant description entry for affected Systems. 

## Services

The PDE produces three different services:
 + the [Monitorable JSON] service
 + the [Plant Description Management JSON] service
 + the [Plant Description Alarm JSON] service
 
The PDE consumes the following services:
 + the [Service Discovery] service produced by the [Service Registry] core system
 + the [Orchestration Store Management] service produced by the [Orchestrator] core system
 + the [Orchestration] service produced by the [Orchestrator] core system
 + the [AuthorizationControl] service produced by the [Authorization] core system
 + the [Inventory service] produced by an [Inventory] system (TBD)
 + the [Monitorable JSON] service produced by the systems in the plant (TBD)
    
  
[Authorization]:../../README.md#authorization
[AuthorizationControl]:../../README.md#authorization
[Inventory service]:TBD
[Inventory]:TBD
[Monitorable]:monitorable-sd.md
[Monitorable JSON]:monitorable-idd-http-json.md
[Orchestrator]:../../README.md#orchestrator
[Orchestration]:../../README.md#orchestrator
[Orchestration Store Management]:../../README.md#orchestrator
[Plant Description Alarm]:plant-description-alarm-sd.md
[Plant Description Alarm JSON]:plant-description-alarm-idd-http-json.md
[Plant Description Management]:plant-description-management-sd.md
[Plant Description Management JSON]:plant-description-management-idd-http-json.md
[Service Discovery]:../../README.md#serviceregistry_usecases
[Service Registry]:../../README.md#serviceregistry
  