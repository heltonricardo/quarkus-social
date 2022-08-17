package info.helton.quarkus_social.resource;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestPath;

import info.helton.quarkus_social.constant.Route;
import info.helton.quarkus_social.dto.input.UserIDTO;
import info.helton.quarkus_social.model.User;
import info.helton.quarkus_social.repository.UserRepository;
import info.helton.quarkus_social.template.QSResource;

@QSResource
@Path(Route.USERS)
public class UserResource {

    final UserRepository repository;
    final Validator validator;
    
    @Inject
    public UserResource(UserRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @GET
    public Response listAll() {
        List<User> query = repository.listAll();
        return Response.ok(query).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@RestPath Long id) {
        User query = repository.findById(id);
        return query != null
                ? Response.ok(query).build()
                : Response.status(Status.NOT_FOUND).build();
    }

    @POST
    @Transactional
    public Response create(UserIDTO dto) {
        Set<ConstraintViolation<UserIDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            String message = violations.stream().findAny().get().getMessage();
            return Response.status(Status.BAD_REQUEST).entity(message).build();
        }
        User user = new User();
        user.name = dto.getName();
        user.age = dto.getAge();
        repository.persist(user);
        return Response.ok(user).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@RestPath Long id, UserIDTO dto) {
        User entity = repository.findById(id);
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
    public Response delete(@RestPath Long id) {
        boolean deleted = repository.deleteById(id);
        return deleted
                ? Response.ok().build()
                : Response.status(Status.NOT_FOUND).build();
    }
}
