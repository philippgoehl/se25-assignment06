package de.unibayreuth.se.campuscoffee.domain.tests;

import de.unibayreuth.se.campuscoffee.domain.model.*;
import de.unibayreuth.se.campuscoffee.domain.ports.PosService;
import de.unibayreuth.se.campuscoffee.domain.ports.ReviewService;
import de.unibayreuth.se.campuscoffee.domain.ports.UserService;
import org.apache.commons.lang3.SerializationUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TestFixtures {
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2025, 6, 3, 12, 0, 0);

    private static final List<User> USER_LIST = List.of(
            new User(1L, DATE_TIME, DATE_TIME, "jane_doe", "jane.doe@uni-bayreuth.de", "Jane", "Doe"),
            new User(2L, DATE_TIME, DATE_TIME,"maxmustermann", "max.mustermann@campus.de", "Max", "Mustermann"),
            new User(3L, DATE_TIME, DATE_TIME,"student2023", "student2023@study.org", "Student", "Example")
    );

    private static final List<Pos> POS_LIST = List.of(
            new Pos(1L, DATE_TIME, DATE_TIME, "CrazySheep (RW-I)", "Description 1", PosType.CAFE, CampusType.MAIN, "Andreas-Maisel-Weg", "2", 95445, "Bayreuth"),
            new Pos(1L, DATE_TIME, DATE_TIME, "Cafeteria (Mensa)", "Description 1", PosType.CAFE, CampusType.MAIN, "Universitätsstraße", "30", 95447, "Bayreuth"),
            new Pos(1L, DATE_TIME, DATE_TIME, "Lidl (Nürnberger Str.)", "Description 1", PosType.VENDING_MACHINE, CampusType.ZAPF, "Nürnberger Str.", "3a", 95448, "Bayreuth")
    );

    private static final List<Review> REVIEW_LIST = List.of(
            // TODO: create suitable review test fixtures
    );

    public static List<User> getUserList() {
        return USER_LIST.stream()
                .map(SerializationUtils::clone)
                .toList();
    }

    public static List<User> getUserListForInsertion() {
        return getUserList().stream()
                .map(user -> user.toBuilder().id(null).createdAt(null).updatedAt(null).build())
                .toList();
    }

    public static List<Pos> getPosList() {
        return POS_LIST.stream()
                .map(SerializationUtils::clone) // prevent issues when tests modify the fixture objects
                .toList();
    }

    public static List<Pos> getPosListForInsertion() {
        return getPosList().stream()
                .map(user -> user.toBuilder().id(null).createdAt(null).updatedAt(null).build())
                .toList();
    }

    public static List<Review> getReviewList() {
        return REVIEW_LIST.stream()
                .map(SerializationUtils::clone) // prevent issues when tests modify the fixture objects
                .toList();
    }

    public static List<User> createUsers(UserService userService) {
        return getUserListForInsertion().stream()
                .map(userService::upsert)
                .collect(Collectors.toList());
    }

    public static List<Pos> createPos(PosService posService) {
        return getPosListForInsertion().stream()
                .map(posService::upsert)
                .collect(Collectors.toList());
    }

    public static List<Review> createReviews(ReviewService reviewService, PosService posService, UserService userService) {
        List<Pos> posList = posService.getAll();
        List<User> userList = userService.getAll();
        // TODO: create suitable review test fixtures (linking them to the POS and user test fixtures)
        return reviewService.getAll();
    }
}
