package eu.arrowhead.core.plantdescriptionengine.services.pde_monitor.routehandlers;

import eu.arrowhead.core.plantdescriptionengine.dto.ErrorMessage;
import eu.arrowhead.core.plantdescriptionengine.services.pde_monitor.dto.PdeAlarmUpdateDto;
import eu.arrowhead.core.plantdescriptionengine.utils.Locator;
import se.arkalix.dto.DtoEncoding;
import se.arkalix.net.http.HttpStatus;
import se.arkalix.net.http.service.HttpRouteHandler;
import se.arkalix.net.http.service.HttpServiceRequest;
import se.arkalix.net.http.service.HttpServiceResponse;
import se.arkalix.util.concurrent.Future;

/**
 * Handles HTTP requests to update PDE Alarms.
 */
public class UpdatePdeAlarm implements HttpRouteHandler {

    /**
     * Handles an HTTP request to update the Alarm specified by the id parameter
     * with the information in the request body.
     *
     * @param request  HTTP request containing a PlantDescriptionUpdate.
     * @param response HTTP response containing the updated entry.
     */
    @Override
    public Future<?> handle(final HttpServiceRequest request, final HttpServiceResponse response) throws Exception {
        final var alarmManager = Locator.getAlarmManager();
        return request
            .bodyAs(PdeAlarmUpdateDto.class)
            .map(newFields -> {
                final String idString = request.pathParameter(0);
                int id;

                try {
                    id = Integer.parseInt(idString);
                } catch (final NumberFormatException e) {
                    response.status(HttpStatus.BAD_REQUEST);
                    response.body(DtoEncoding.JSON, ErrorMessage.of("'" + idString + "' is not a valid PDE Alarm ID."));
                    return response.status(HttpStatus.BAD_REQUEST);
                }

                boolean alarmNotFound = false;

                if (newFields.acknowledged().isPresent()) {
                    try {
                        alarmManager.setAcknowledged(id, newFields.acknowledged().get());
                    } catch (IllegalArgumentException e) {
                        alarmNotFound = true;
                    }
                }

                final var alarm = alarmManager.getAlarmDto(id);
                if (alarm == null) {
                    alarmNotFound = true;
                }

                if (alarmNotFound) {
                    return response
                        .status(HttpStatus.NOT_FOUND)
                        .body(ErrorMessage.of("PDE Alarm with ID '" + idString + "' not found."));
                } else {
                    return response
                        .status(HttpStatus.OK)
                        .body(alarmManager.getAlarmDto(id));
                }

            });
    }
}