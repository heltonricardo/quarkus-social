package info.helton.quarkus_social.repository;

import javax.enterprise.context.ApplicationScoped;

import info.helton.quarkus_social.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    
}
