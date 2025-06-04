package de.unibayreuth.se.campuscoffee.api.mapper;

//import de.unibayreuth.se.campuscoffee.api.dtos.ReviewDto;
//import de.unibayreuth.se.campuscoffee.domain.exceptions.PosNotFoundException;
//import de.unibayreuth.se.campuscoffee.domain.exceptions.UserNotFoundException;
//import de.unibayreuth.se.campuscoffee.domain.model.Pos;
//import de.unibayreuth.se.campuscoffee.domain.model.Review;
//import de.unibayreuth.se.campuscoffee.domain.model.User;
//import de.unibayreuth.se.campuscoffee.domain.ports.PosService;
//import de.unibayreuth.se.campuscoffee.domain.ports.UserService;
//import lombok.NoArgsConstructor;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

// TODO: uncomment the mapper after implementing the Review domain class and the ReviewDto

//@Mapper(componentModel = "spring")
//@ConditionalOnMissingBean // prevent IntelliJ warning about duplicate beans
//@NoArgsConstructor
//public abstract class ReviewDtoMapper {
//    @Autowired
//    private PosService posService;
//    @Autowired
//    private UserService userService;
//
//    @Mapping(target = "posId", expression = "java(source.getPos().getId())")
//    @Mapping(target = "authorId", expression = "java(source.getAuthor().getId())")
//    public abstract ReviewDto fromDomain(Review source);
//
//    @Mapping(target = "pos", expression = "java(getPosById(source.getPosId()))")
//    @Mapping(target = "author", expression = "java(getUserById(source.getAuthorId()))")
//    @Mapping(target = "approvalCount", ignore = true)
//    public abstract Review toDomain(ReviewDto source) throws UserNotFoundException, PosNotFoundException;
//
//    protected Pos getPosById(Long posId) throws PosNotFoundException {
//        if (posId == null) {
//            return null;
//        }
//        return posService.getById(posId);
//    }
//
//    protected User getUserById(Long userId) throws UserNotFoundException {
//        if (userId == null) {
//            return null;
//        }
//        return userService.getById(userId);
//    }
//}