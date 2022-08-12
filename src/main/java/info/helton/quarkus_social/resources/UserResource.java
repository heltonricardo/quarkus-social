package info.helton.quarkus_social.resources;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import info.helton.quarkus_social.constants.Route;
import info.helton.quarkus_social.dto.input.UserIDTO;
import info.helton.quarkus_social.models.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

@Path(Route.USERS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    @Transactional
    public Response create(UserIDTO dto) {
        User user = new User();
        user.name = dto.getName();
        user.age = dto.getAge();
        user.persist();
        return Response.ok(user).build();
    }

    @GET
    public Response listAll() {
        PanacheQuery<User> query = User.findAll();
        return Response.ok(query.list()).build();
    }
}
