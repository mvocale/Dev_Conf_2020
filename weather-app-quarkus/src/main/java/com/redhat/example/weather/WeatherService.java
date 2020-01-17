package com.redhat.example.weather;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

@RequestScoped
@Path("weather")
public class WeatherService {


    @Inject
    SelectedCountry selectedCountry;

    @Inject
    EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(name = "performedChecks", description = "How many weather forecast have been performed.")
    @Timed(name = "checksTimer", description = "A measure of how long it takes to perform the weather forecast", unit = MetricUnits.MILLISECONDS)
    public Country getList() {
        return em.find(Country.class,selectedCountry.getCode());
    }

}
