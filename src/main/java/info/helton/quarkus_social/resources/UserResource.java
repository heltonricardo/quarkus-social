package info.helton.quarkus_social.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import info.helton.quarkus_social.constants.Route;
import info.helton.quarkus_social.dto.input.UserIDTO;

@Path(Route.USERS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    public Response create(UserIDTO dto) {
        return Response.ok(dto).build();
    }

    @GET
    public Response listAll() {
        return Response.ok().build();
    }
}
