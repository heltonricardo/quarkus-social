package info.helton.quarkus_social.resources;

import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestPath;

import info.helton.quarkus_social.constants.Route;
import info.helton.quarkus_social.dto.input.UserIDTO;
import info.helton.quarkus_social.models.User;
import info.helton.quarkus_social.templates.QSResource;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

@QSResource
@Path(Route.USERS)
public class UserResource {

    @GET
    public Response listAll() {
        PanacheQuery<User> query = User.findAll();
        return Response.ok(query.list()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@RestPath Long id) {
        User query = User.findById(id);
        return query != null
                ? Response.ok(query).build()
                : Response.status(Status.NOT_FOUND).build();
    }

    @POST
    @Transactional
    public Response create(UserIDTO dto) {
        User user = new User();
        user.name = dto.getName();
        user.age = dto.getAge();
        user.persist();
        return Response.ok(user).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@RestPath Long id, UserIDTO dto) {
        User entity = User.findById(id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        entity.age = dto.getAge();
        entity.name = dto.getName();
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteUser(@RestPath Long id) {
        boolean deleted = User.deleteById(id);
        return deleted
                ? Response.ok().build()
                : Response.status(Status.NOT_FOUND).build();
    }
}
