package tech.buildrun.springsecurity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tech.buildrun.springsecurity.controller.dto.CreateUserDto;
import tech.buildrun.springsecurity.controller.dto.FollowResponseDto;
import tech.buildrun.springsecurity.controller.dto.FollowUserDto;
import tech.buildrun.springsecurity.controller.dto.ProfileDto;
import tech.buildrun.springsecurity.entities.Role;
import tech.buildrun.springsecurity.entities.User;
import tech.buildrun.springsecurity.repository.RoleRepository;
import tech.buildrun.springsecurity.repository.UserRepository;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(RoleRepository roleRepository,
                          UserRepository userRepository,
                          BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto) {
        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());
        var userFromDb = userRepository.findByUsername(dto.username());
        if(userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        User newUser = new User();
        newUser.setUsername(dto.username());
        newUser.setPassword(passwordEncoder.encode(dto.password()));
        newUser.setRoles(Set.of(basicRole));
        userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }

    @GetMapping("users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> User(@RequestParam String username,
                                           JwtAuthenticationToken token) throws AuthenticationException {
        var userFromDb = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var followersDto = userFromDb.getFollowers().stream()
                .map(f -> new FollowResponseDto(
                        f.getFollower().getUserId(),
                        f.getFollower().getUsername()
                ))
                .toList();

        var followingDto = userFromDb.getFollowing().stream()
                .map(f -> new FollowResponseDto(
                        f.getFollowed().getUserId(),
                        f.getFollowed().getUsername()
                ))
                .toList();

        var res = new ProfileDto(userFromDb.getUsername(),
                followersDto,
                followingDto,
                userFromDb.getPosts());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/profile/me")
    public ResponseEntity<ProfileDto> currentUser(JwtAuthenticationToken token) throws AuthenticationException {
        var userFromDb = userRepository.findById(UUID.fromString(token.getName()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        var followersDto = userFromDb.getFollowers().stream()
                .map(f -> new FollowResponseDto(
                        f.getFollower().getUserId(),
                        f.getFollower().getUsername()
                ))
                .toList();

        var followingDto = userFromDb.getFollowing().stream()
                .map(f -> new FollowResponseDto(
                        f.getFollowed().getUserId(),
                        f.getFollowed().getUsername()
                ))
                .toList();

        var res = new ProfileDto(userFromDb.getUsername(),
                followersDto,
                followingDto,
                userFromDb.getPosts());
        return ResponseEntity.ok(res);
    }
}
