//package sdk.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping(value = "/api/v1/auth")
//public class AuthController {
//
//    @Autowired(required = false)
//    private TokenUtil tokenUtil;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired(required = false)
//    private AuthenticationManager authenticationManager;
//
//    @PostMapping(value = {"","/"})
//    public JwtResponse signIn(@RequestBody SignInRequest signInRequest) {
//
//        final Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword())
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        UserDetails userDetails = userService.loadUserByUsername(signInRequest.getUsername());
//        String token = tokenUtil.generateToken(userDetails);
//        JwtResponse response = new JwtResponse(token);
//        return response;
//    }
//
//
//
//}
