package info.helton.quarkus_social.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import info.helton.quarkus_social.constants.Route;
import info.helton.quarkus_social.dto.input.UserIDTO;

@Path(Route.USERS)
public class UserResource {

    @POST
    public Response create(UserIDTO dto) {
        return Response.status(Status.CREATED).build();
    }
}
