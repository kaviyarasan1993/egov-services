/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.user.persistence.entity;

import lombok.*;
import org.egov.user.persistence.entity.enums.BloodGroup;
import org.egov.user.persistence.entity.enums.Gender;
import org.egov.user.persistence.entity.enums.GuardianRelation;
import org.egov.user.persistence.entity.enums.UserType;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "eg_user")
@Inheritance(strategy = InheritanceType.JOINED)
@Cacheable
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractAuditable {

    private static final long serialVersionUID = 1666623645834766468L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "title")
    private String title;

    @Column(name = "password")
    private String password;

    @Column(name = "salutation")
    private String salutation;

    @Column(name = "guardian")
    private String guardian;

    @Column(name = "guardianrelation")
    @Enumerated(EnumType.STRING)
    private GuardianRelation guardianRelation;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Column(name = "mobilenumber")
    private String mobileNumber;

    @Column(name = "emailid")
    private String emailId;

    @Column(name = "altcontactnumber")
    private String altContactNumber;

    @Column(name = "pan")
    private String pan;

    @Column(name = "aadhaarnumber")
    private String aadhaarNumber;


    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.EAGER
    )
    private List<Address> address = new ArrayList<>();

    @Column(name = "active")
    private boolean active;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "eg_userrole", joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "roleid"))
    private Set<Role> roles = new HashSet<>();

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(name = "pwdexpirydate")
    private Date pwdExpiryDate = new Date();

    private String locale = "en_IN";

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column(name = "bloodgroup")
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @Column(name = "identificationmark")
    private String identificationMark;

    @Column(name = "signature")
    private String signature;

    @Column(name = "photo")
    private String photo;

    @Column(name = "accountlocked")
    private boolean accountLocked;

    public DateTime getPwdExpiryDate() {
        return null == pwdExpiryDate ? null : new DateTime(pwdExpiryDate);
    }
}