package info.helton.quarkus_social.repository;

import javax.enterprise.context.ApplicationScoped;

import info.helton.quarkus_social.model.Follower;
import info.helton.quarkus_social.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean doesNotFollow(User follower, User user) {
        Parameters params = Parameters.with("follower", follower).and("user", user);
        PanacheQuery<Follower> query = find("follower =: follower and user =: user", params);
        return query.firstResultOptional().isEmpty();
    }
}
