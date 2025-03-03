//package sdk.security;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import org.springframework.stereotype.Service;
//import sdk.POJO.SspUser;
//import sdk.dao.AccountDAO;
//
//@Service
//public class UserService implements UserDetailsService {
//
//
////    @Bean
////    private PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
//      @Autowired
//      private AccountDAO accountDAO;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
////        if (username.equals("user"))
////            return User.withUsername("user")
////                    .password("{noop}password")
////                    .roles("USER")
////                    .build();
////        else if (username.equals("admin"))
////            return User.withUsername("user")
////                    .password("{noop}password")
////                    .roles("USER")
////                    .build();
////        else
////            return User.withUsername("user5")
////                    .password("{noop}password")
////                    .roles("USER")
////                    .build();
//
//        //SspUser user = (SspUser)accountDAO.find(SspUser.class,6145);
//        SspUser user=new SspUser();
//        user.setUsername("user2");
//        user.setPassword("{noop}password");
//        return UserDetailsImpl.build(user);
//    }
//
//
//}
