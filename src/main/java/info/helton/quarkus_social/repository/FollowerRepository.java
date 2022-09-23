package info.helton.quarkus_social.repository;

import javax.enterprise.context.ApplicationScoped;

import info.helton.quarkus_social.model.Follower;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {
    
}
