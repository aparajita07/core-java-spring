package eu.arrowhead.core.plantdescriptionengine.services.pde_monitor.routehandlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.arrowhead.core.plantdescriptionengine.requestvalidation.BooleanParameter;
import eu.arrowhead.core.plantdescriptionengine.requestvalidation.IntParameter;
import eu.arrowhead.core.plantdescriptionengine.requestvalidation.QueryParamParser;
import eu.arrowhead.core.plantdescriptionengine.requestvalidation.QueryParameter;
import eu.arrowhead.core.plantdescriptionengine.requestvalidation.StringParameter;
import eu.arrowhead.core.plantdescriptionengine.services.pde_mgmt.PlantDescriptionEntryMap;
import eu.arrowhead.core.plantdescriptionengine.services.pde_mgmt.dto.PlantDescriptionEntry;
import eu.arrowhead.core.plantdescriptionengine.services.pde_monitor.MonitorInfo;
import eu.arrowhead.core.plantdescriptionengine.services.pde_monitor.dto.PlantDescriptionEntryDto;
import eu.arrowhead.core.plantdescriptionengine.services.pde_monitor.dto.PlantDescriptionEntryListBuilder;
import eu.arrowhead.core.plantdescriptionengine.utils.DtoUtils;
import se.arkalix.net.http.HttpStatus;
import se.arkalix.net.http.service.HttpRouteHandler;
import se.arkalix.net.http.service.HttpServiceRequest;
import se.arkalix.net.http.service.HttpServiceResponse;
import se.arkalix.util.concurrent.Future;

/**
 * Handles HTTP requests to retrieve all current Plant Description Entries.
 */
public class GetAllPlantDescriptions implements HttpRouteHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetAllPlantDescriptions.class);

    private final PlantDescriptionEntryMap entryMap;
    private final MonitorInfo monitorInfo;

    /**
     * Class constructor
     *
     * @param httpClient  Object for communicating with monitorable services.
     * @param monitorInfo Object that stores information on monitorable systems.
     *
     */
    public GetAllPlantDescriptions(MonitorInfo monitorInfo, PlantDescriptionEntryMap entryMap
    ) {
        Objects.requireNonNull(monitorInfo, "Expected MonitorInfo");
        Objects.requireNonNull(entryMap, "Expected Plant Description Entry Map");

        this.monitorInfo = monitorInfo;
        this.entryMap = entryMap;
    }

    /**
     * Handles an HTTP request to acquire a list of Plant Description Entries
     * present in the PDE.
     *
     * @param request HTTP request object.
     * @param response HTTP response whose body contains a list of Plant
     *                 Description entries.
     */
    @Override
    public Future<?> handle(final HttpServiceRequest request, final HttpServiceResponse response) throws Exception {
        final List<QueryParameter> requiredParameters = null;
        final List<QueryParameter> acceptedParameters = Arrays.asList(
            new IntParameter("page")
                .requires(new IntParameter("item_per_page")),
            new StringParameter("sort_field")
                .legalValues(Arrays.asList("id", "createdAt", "updatedAt")),
            new StringParameter("direction")
                .legalValues(Arrays.asList("ASC", "DESC"))
                .setDefault("ASC"),
            new StringParameter("filter_field")
                .legalValue("active")
                .requires(new BooleanParameter("filter_value"))
        );

        final var parser = new QueryParamParser(requiredParameters, acceptedParameters, request);

        if (parser.hasError()) {
            response.status(HttpStatus.BAD_REQUEST);
            response.body(parser.getErrorMessage());
            logger.error("Encountered the following error(s) while parsing an HTTP request: " +
                parser.getErrorMessage());
            return Future.done();
        }

        var entries = entryMap.getEntries();

        final Optional<String> sortField = parser.getString("sort_field");
        if (sortField.isPresent()) {
            final String sortDirection = parser.getString("direction").get();
            final boolean sortAscending = (sortDirection.equals("ASC") ? true : false);
            PlantDescriptionEntry.sort(entries, sortField.get(), sortAscending);
        }

        final Optional<Integer> page = parser.getInt("page");
        if (page.isPresent()) {
            int itemsPerPage = parser.getInt("item_per_page").get();
            int from = Math.min(page.get() * itemsPerPage, 0);
            int to = Math.min(from + itemsPerPage, entries.size());
            entries = entries.subList(from, to);
        }

        if (parser.getString("filter_field").isPresent()) {
            boolean filterValue = parser.getBoolean("filter_value").get();
            PlantDescriptionEntry.filterOnActive(entries, filterValue);
        }

        // Extend each Plant Description Entry with monitor data retrieved from
        // monitorable services:
        List<PlantDescriptionEntryDto> extendedEntries = new ArrayList<>();
        for (var entry : entries) {
            extendedEntries.add(DtoUtils.extend(entry, monitorInfo));
        }

        response
            .status(HttpStatus.OK)
            .body(new PlantDescriptionEntryListBuilder()
                .data(extendedEntries)
                .count(extendedEntries.size())
                .build());

        return Future.done();
    }

}