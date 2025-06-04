package de.unibayreuth.se.campuscoffee;

import de.unibayreuth.se.campuscoffee.domain.model.Pos;
import de.unibayreuth.se.campuscoffee.domain.model.Review;
import de.unibayreuth.se.campuscoffee.domain.model.User;
import de.unibayreuth.se.campuscoffee.domain.ports.ReviewService;
import de.unibayreuth.se.campuscoffee.domain.ports.UserService;
import de.unibayreuth.se.campuscoffee.domain.tests.TestFixtures;
import de.unibayreuth.se.campuscoffee.domain.ports.PosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Load initial data into the list via the list service from the business layer.
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Profile("dev")
class LoadInitialData implements InitializingBean {
    private final PosService posService;
    private final UserService userService;
    //private final ReviewService reviewService;

    // TODO: activate loading of review test data after implementing the required classes

    @Override
    public void afterPropertiesSet() {
        log.info("Deleting existing data...");
        //reviewService.clear();
        posService.clear();
        userService.clear();
        log.info("Loading initial data...");
        List<User> users = TestFixtures.createUsers(userService);
        log.info("Created {} users.", users.size());
        List<Pos> posList = TestFixtures.createPos(posService);
        log.info("Created {} POS.", posList.size());
        //List<Review> reviews = TestFixtures.createReviews(reviewService, posService, userService);
        //log.info("Created {} reviews.", reviews.size());
        log.info("Initial data loaded successfully.");
    }
}