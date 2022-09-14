package info.helton.quarkus_social.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import info.helton.quarkus_social.model.Post;
import info.helton.quarkus_social.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {

    public List<Post> listAllPostOfUser(User user) {
        return list("user", Sort.by("dateTime", Direction.Descending), user);
    }
}
