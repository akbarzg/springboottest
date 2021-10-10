package lu.lllc.security.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lu.lllc.security.entity.Role;
import lu.lllc.security.entity.User;
import lu.lllc.security.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("Start of loadUserByUsername()... ");
		
		
		User user = userRepository.findByName(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("Unknown user");
		}
		

		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		
		for (Role role : user.getRoles()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
		}
		

		
		UserDetails userDetails = new org.springframework.security.core.userdetails.User(
				user.getName(), user.getPassword(), user.getActive(), true, true, true,grantedAuthorities);
		
		InputStream resourceAsStream = this.getClass().getResourceAsStream("/test.properties");
		System.out.println("resourceAsStream: "+resourceAsStream);
		Properties props = new Properties();
		try {
			props.load(resourceAsStream);
			String propVal = props.getProperty("my.prop.name");
			System.out.println(" Prop my.prop.name:======> "+propVal);
		} catch (IOException e) {
			System.out.println("Error occured while reading properties file: Error: "+ e);
		}
		System.out.println("END OF loadUserByUsername. Returning...");
		return userDetails;
	}

}
