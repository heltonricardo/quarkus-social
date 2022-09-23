package info.helton.quarkus_social.resource;

import javax.inject.Inject;
import javax.ws.rs.Path;

import info.helton.quarkus_social.constant.Route;
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

    
}
