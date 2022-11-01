package info.helton.quarkus_social.resource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

import info.helton.quarkus_social.constant.Route;
import info.helton.quarkus_social.dto.input.FollowerIDTO;
import info.helton.quarkus_social.dto.output.FollowerODTO;
import info.helton.quarkus_social.dto.output.FollowersODTO;
import info.helton.quarkus_social.error.ResponseError;
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
    final Validator validator;

    @PUT
    @Transactional
    public Response followUser(@RestPath Long userId, FollowerIDTO dto) {
        Set<ConstraintViolation<FollowerIDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            return ResponseError
                    .create("Validation Error", violations)
                    .statusCode(ResponseError.UNPROCESSABLE_ENTITY);
        }
        if (userId.equals(dto.getFollowerId())) {
            return Response.status(Status.CONFLICT).build();
        }
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

    @GET
    public Response listFollowers(@RestPath Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        List<FollowerODTO> followers = repository
                .listAllFollowersOfUser(user)
                .stream()
                .map(FollowerODTO::fromEntity)
                .collect(Collectors.toList());
        FollowersODTO response = FollowersODTO.builder()
                .followersCount(followers.size())
                .followers(followers)
                .build();
        return Response.ok(response).build();
    }

    @DELETE
    @Transactional
    public Response unfollowUser(@RestPath Long userId, @RestQuery Long followerId) {
        User user = userRepository.findById(userId);
        User follower = userRepository.findById(followerId);
        if (user == null || follower == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        repository.deleteByFollowerIdAndUserId(followerId, userId);
        return Response.status(Status.NO_CONTENT).build();
    }
}
