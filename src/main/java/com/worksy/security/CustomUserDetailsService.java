package com.worksy.security;

import com.worksy.entity.HRProfile;
import com.worksy.entity.Role;
import com.worksy.entity.User;
import com.worksy.repository.HRProfileRepository;
import com.worksy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Looks up credentials across all three login tables (User, HRProfile, and the
 * seeded Admin user which is itself a User row carrying ROLE_ADMIN).
 * Email uniqueness across the two tables combined is enforced at registration time.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final HRProfileRepository hrProfileRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<String> roles = user.getRoles().stream().map(Role::getName).map(Enum::name).toList();
            String type = roles.contains("ROLE_ADMIN") ? "ADMIN" : "USER";
            return UserPrincipal.create(user.getId(), user.getEmail(), user.getPassword(),
                    user.getFullName(), type, user.isEnabled(), roles);
        }

        var hrOpt = hrProfileRepository.findByEmail(email);
        if (hrOpt.isPresent()) {
            HRProfile hr = hrOpt.get();
            List<String> roles = hr.getRoles().stream().map(Role::getName).map(Enum::name).toList();
            return UserPrincipal.create(hr.getId(), hr.getEmail(), hr.getPassword(),
                    hr.getFullName(), "HR", hr.isEnabled() && hr.isApproved(), roles);
        }

        throw new UsernameNotFoundException("No account found with email: " + email);
    }
}
