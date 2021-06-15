package org.milosz.friendsreactive.repository;

import org.milosz.friendsreactive.model.Friend;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendsRepository extends ReactiveMongoRepository<Friend, String> {
}
