package info.helton.quarkus_social.repository;

import javax.enterprise.context.ApplicationScoped;

import info.helton.quarkus_social.model.Post;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {

}
