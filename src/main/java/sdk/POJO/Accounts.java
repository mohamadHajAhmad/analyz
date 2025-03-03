package sdk.POJO;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ACCOUNTS", schema = "billz")
public class Accounts extends AbstractPojo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ACCOUNT_NUMBER")
     private String accountNumber;
    @Column(name = "SETUP_DATE")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date setupDate;

    @Column(name = "FIRST_NAME")
    private String firstName;



    @OneToOne(mappedBy = "accounts",fetch = FetchType.EAGER,cascade = CascadeType.MERGE)
    @JsonIgnore
   private SspUser sspUser;

//    @JsonIgnore
//    @OneToMany(targetEntity = SspUser.class,fetch = FetchType.EAGER,mappedBy = "accounts")
//    @JoinColumn(name = "ACCOUNT_NUMBER", referencedColumnName = "ACCOUNT_NUMBER")
//    private Set<SspUser> seasonalBillingSchedule = new HashSet<SspUser>(0);

    public Accounts() {
    }


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Date getSetupDate() {
        return setupDate;
    }

    public void setSetupDate(Date setupDate) {

        this.setupDate = setupDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public SspUser getSspUser() {
        return sspUser;
    }

    public void setSspUser(SspUser sspUser) {
        this.sspUser = sspUser;
    }


//    public Set<SspUser> getSeasonalBillingSchedule() {
//        return seasonalBillingSchedule;
//    }
//
//    public void setSeasonalBillingSchedule(Set<SspUser> seasonalBillingSchedule) {
//        this.seasonalBillingSchedule = seasonalBillingSchedule;
//    }
}
