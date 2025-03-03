package sdk.controller;


//import com.blulogix.accounts.models.AccountModel;

import com.blulogix.accounts.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdk.POJO.Accounts;
import sdk.model.AbstractDataLoaderModel;
import sdk.model.ServiceExecutionResult;
import sdk.service.AccountsService;
import sdk.service.SearchService;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private AccountsService accountService;
//    private OrderService orderService;
   private AccountService accountBluLogixServer;

    @Autowired
    private SearchService searchService;

    /**
     * @return {@link Long}
     * @DocView.Name get
     */
    @GetMapping("/account")
    @RolesAllowed("user")
    public ServiceExecutionResult get() {
        return accountService.get();
    }

    @PostMapping(path = {"/accountNumber"})
    public ServiceExecutionResult get(@RequestBody AbstractDataLoaderModel dataModel)   {
        //orderService.searchOrders(null);
        return accountService.get(dataModel);
    }

    @PostMapping(path = {"/searchAccountInternal"})
    public ServiceExecutionResult get2(@RequestBody AbstractDataLoaderModel dataModel) {

        return searchService.search(Accounts.class, dataModel);
    }

    @GetMapping(path = "/username")
    public ResponseEntity<String> getAuthorizedUserName() {
        return ResponseEntity.ok("Testtt");
    }

//    @PostMapping(path = {"/addAccount"})
//    public ServiceExecutionResult addAccount(@RequestBody AccountModel account) throws ApiException {
//        ServiceExecutionResult result = new ServiceExecutionResult();
//
////        account.setAccountAddress(new AddressModel("USA", "Los Angeles", "CA", "address details1", "90001", "R"));
////
////        account.setBillable(true);
////        account.setAgentUser(true);
////        account.setCreateLoginUser(true);
////
////        ResponseBody<HashMap> apiResponse = AccountService.addAccount(account);
////        result.setMessage(apiResponse.getMessage());
//        return result;
//    }

//    @PostMapping(path = {"/editAccount"})
//    public ServiceExecutionResult editAccount(@RequestBody AccountModel account) throws ApiException {
//        ServiceExecutionResult result = new ServiceExecutionResult();
////        account.setBillable(false);
////        ResponseBody<HashMap> apiResponse = AccountService.editAccount(account);
////        result.setMessage(apiResponse.getMessage());
//        return result;
//    }
//
//    @PostMapping(path = {"/searchAccount"})
//    public ServiceExecutionResult searchAccount(@RequestBody SearchModel searchModel) throws ApiException {
//        ServiceExecutionResult result = new ServiceExecutionResult();
////        var apiResponse = AccountService.searchAccounts(searchModel);
////        Long size = apiResponse.getSize();
////        var accounts = apiResponse.getReturnValue().getData();
////        result.setReturnValue(accounts);
//        return result;
//    }
//
//    @PostMapping(path = {"/testAPI"})
//    public ServiceExecutionResult testAPI(@RequestBody AccountModel account) throws ApiException {
//        ServiceExecutionResult result = new ServiceExecutionResult();
////        var response= InvoicesService.getInvoicePdfFile(9996L);
////        System.out.println(response.getExecutionSuccessful());
////        if(response.getExecutionSuccessful()){
////            byte[] data= (byte[]) response.getReturnValue();
//////            try (FileOutputStream fos = new FileOutputStream(System.getProperty("user.home")+"/Downloads/testNew.pdf" )) {
//////                fos.write(data);
//////
//////            }catch (IOException e) {
//////                throw new RuntimeException(e);
//////            }
////            System.out.println(data);
////            result.setReturnValue(data);
////        }
//
//        return result;
//    }
}
