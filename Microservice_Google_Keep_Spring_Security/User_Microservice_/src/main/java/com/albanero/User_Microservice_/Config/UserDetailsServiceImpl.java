package com.albanero.User_Microservice_.Config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.albanero.User_Microservice_.Model.Users_;
import com.albanero.User_Microservice_.Repository.User_Repo_;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private User_Repo_ user_Repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<Users_> user = user_Repo.findByUserName(username);

		if (!user.isPresent()) {
			return null;
		}
		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		return customUserDetails;
	}

}
