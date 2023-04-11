package com.example.petgram.security.jwt;

import com.example.petgram.model.User;
import com.example.petgram.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {


    @Autowired
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + username));
        return JwtUserFactory.create(user);
    }

//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        Optional<User> user = userRepository.findByUsername(userName);
//        if (user.isEmpty()) {
//            log.error("User not found in the database");
//            throw new UsernameNotFoundException("User not found in the database");
//        } else {
//            log.info("User found in the database: {}", userName);
//        }
//        List<String> listRoles = new ArrayList<>();
//        listRoles.add(user.get().getRole().name());
//        return new UserPrincipal(get());
//    }


//    @Transactional
//    public UserDetails loadUserByEmail(String email)
//            throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new UsernameNotFoundException("User not found with email : " + email)
//                );
//        List<String> listRoles = new ArrayList<>();
//        listRoles.add(user.getRole().name());
//        return new UserPrincipal(user);
//    }
//
//    @Transactional
//    public UserDetails loadUserById(String id) {
//        User user = userRepository.findById(id).orElseThrow(
//                () ->  new ResourceNotFoundException("User", "id", id)
//        );
//
//        return new UserPrincipal(user);
//    }


}
