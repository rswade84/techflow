package org.taskntech.tech_flow.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.taskntech.tech_flow.models.User;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {
}
