package info.helton.quarkus_social.resource;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestPath;

import info.helton.quarkus_social.constant.Route;
import info.helton.quarkus_social.dto.input.FollowerIDTO;
import info.helton.quarkus_social.model.Follower;
import info.helton.quarkus_social.model.User;
import info.helton.quarkus_social.repository.FollowerRepository;
import info.helton.quarkus_social.repository.UserRepository;
import info.helton.quarkus_social.template.QSResource;
import lombok.RequiredArgsConstructor;

@QSResource
@Path(Route.FOLLOWERS)
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class FollowerResource {

    final FollowerRepository repository;
    final UserRepository userRepository;

    @PUT
    @Transactional
    public Response followUser(@RestPath Long userId, FollowerIDTO dto) {
        User user = userRepository.findById(userId);
        User follower = userRepository.findById(dto.getFollowerId());
        if (user == null || follower == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        if (repository.doesNotFollow(follower, user)) {
            Follower entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);
            repository.persist(entity);
        }
        return Response.status(Status.NO_CONTENT).build();
    }
}
