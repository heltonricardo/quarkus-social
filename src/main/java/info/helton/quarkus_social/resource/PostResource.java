package info.helton.quarkus_social.resource;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Validator;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestPath;

import info.helton.quarkus_social.constant.Route;
import info.helton.quarkus_social.dto.input.PostIDTO;
import info.helton.quarkus_social.model.User;
import info.helton.quarkus_social.repository.PostRepository;
import info.helton.quarkus_social.repository.UserRepository;
import info.helton.quarkus_social.template.QSResource;
import lombok.RequiredArgsConstructor;

@QSResource
@Path(Route.POSTS)
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class PostResource {

    final PostRepository repository;
    final UserRepository userRepository;
    final Validator validator;

    @GET
    public Response listAll(@RestPath Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }

    @POST
    @Transactional
    public Response create(@RestPath Long userId, PostIDTO dto) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }
}
