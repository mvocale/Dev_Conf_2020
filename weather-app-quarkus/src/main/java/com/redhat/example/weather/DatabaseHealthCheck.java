package com.redhat.example.weather;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.net.Socket;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;

/**
 * Health check class used to verify the connection with the database.
 * 
 * @author Mauro Vocale
 * @version 1.0.0 16/01/2020
 */
@Liveness
@ApplicationScoped
public class DatabaseHealthCheck implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        // Check the database host and port: the convention used is that the application can be run
        // into Openshift environment, so there the POSTGRES service environment, or in local environment,
        // so I expected H2 in memory database.
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Database connection health check");
        String hostName = (System.getenv("WEATHER_POSTGRESQL_SERVICE_HOST") != null) ? System.getenv("WEATHER_POSTGRESQL_SERVICE_HOST") : "localhost";
        Integer port = (System.getenv("WEATHER_POSTGRESQL_SERVICE_PORT") != null) ? Integer.parseInt(System.getenv("WEATHER_POSTGRESQL_SERVICE_PORT")) : 8080;

        try {
            pingServer(hostName, port);
            responseBuilder.up();
        } catch (IOException e) {

            responseBuilder.down()
                    .withData("error", e.getMessage());
        }

        return responseBuilder.build();
    }

    private void pingServer(String dbhost, int port) throws IOException {
        Socket socket = new Socket(dbhost, port);
        socket.close();

    }

}
