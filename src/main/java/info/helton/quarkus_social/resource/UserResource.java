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
import info.helton.quarkus_social.error.ResponseError;
import info.helton.quarkus_social.model.User;
import info.helton.quarkus_social.repository.UserRepository;
import info.helton.quarkus_social.template.QSResource;
import lombok.RequiredArgsConstructor;

@QSResource
@Path(Route.USERS)
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class UserResource {

    final UserRepository repository;
    final Validator validator;

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
            return ResponseError
                    .create("Validation Error", violations)
                    .statusCode(ResponseError.UNPROCESSABLE_ENTITY);
        }
        User user = User.builder()
                .name(dto.getName())
                .age(dto.getAge())
                .build();
        repository.persist(user);
        return Response.status(Status.CREATED).entity(user).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@RestPath Long id, UserIDTO dto) {
        User user = repository.findById(id);
        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        user.setAge(dto.getAge());
        user.setName(dto.getName());
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@RestPath Long id) {
        boolean deleted = repository.deleteById(id);
        return deleted
                ? Response.noContent().build()
                : Response.status(Status.NOT_FOUND).build();
    }
}
