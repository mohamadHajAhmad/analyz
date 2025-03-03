package sdk.POJO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import org.hibernate.annotations.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.GrantedAuthority;

import javax.management.relation.Role;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Ssp_User",schema = "billz")
public class SspUser extends AbstractPojo implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "ACCOUNT_NUMBER",insertable = false,updatable = false)
    private String accountNumber;

    @Column(name = "APP_CODE")
    private String password;


    @Column(name = "EMAIL_ADDRESS")
    private String email;

    @OneToOne(fetch = FetchType.EAGER)
   // @ManyToOne(fetch = FetchType.EAGER,targetEntity =Accounts.class )
   // @JoinColumn(name = "ACCOUNT_NUMBER", referencedColumnName = "ACCOUNT_NUMBER")
    @JsonIgnore
    @JoinColumnsOrFormulas({
//@JoinColumnOrFormula(formula=@JoinFormula(value="(select s.USERNAME from ssp_user  s where s.USERNAME='Agility Communic_ations')", referencedColumnName="FIRST_NAME")),
@JoinColumnOrFormula(column = @JoinColumn(name = "ACCOUNT_NUMBER", referencedColumnName="ACCOUNT_NUMBER",insertable = false,updatable = false))

})
//    @JoinColumns({@JoinColumn(name = "ACCOUNT_NUMBER", referencedColumnName = "ACCOUNT_NUMBER",insertable = false,updatable = false),
//            @JoinColumn(name = "USERNAME", referencedColumnName = "FIRST_NAME",insertable = false,updatable = false)})
    private Accounts accounts;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Accounts getAccounts() {
        return accounts;
    }

    public void setAccounts(Accounts accounts) {
        this.accounts = accounts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
