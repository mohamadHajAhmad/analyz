package sdk.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sdk.model.ServiceExecutionResult;
import sdk.model.UserModel;
import sdk.service.BlueA3Service;

import java.util.Map;

@RestController
@RequestMapping("/app/auth")
public class Auth2Controller {

    @Autowired
    private BlueA3Service blueA3Service;

    /**
     * @return {@link Long}
     * @DocView.Name get
     */
    @PostMapping("/token")
    public Map<String, Object> get(@RequestBody UserModel userModel) throws Exception {

        return blueA3Service.getTokenForUser("cis-p-T102-0000002",userModel.getUsername(),userModel.getPassword());

    }


    @PostMapping("/logout")
    public ServiceExecutionResult logout(@RequestParam(required = false) String username) throws Exception {
        return blueA3Service.logoutUser(username);
    }

}
