package com.example.applefarm_.user.entitiy;

import com.example.applefarm_.exception.CustomException;
import com.example.applefarm_.exception.ExceptionStatus;
import com.example.applefarm_.registration.entity.Registration;
import com.example.applefarm_.security.config.Timestamp;
import lombok.Getter;
import com.example.applefarm_.user.dto.UserProfileRequestDto;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "users")
@Getter
@NoArgsConstructor
public class User extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;
    @Column(nullable = false, unique = true)
    private String loginId;
    @Column(nullable = false)
    private String loginPassword;
    @Column(nullable = false, unique = true)
    private String nickName;
    @Column(nullable = false)
    private String image;

    private int points;
    @Column(unique = true)
    private String sellerNickname;
    private String sellerImage;
    private String sellerDetails;
    private Long sellerCategory;

    public User(String loginId, String loginPassword, String nickName, String image, UserRoleEnum role) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.nickName = nickName;
        this.image = image;
        this.role = role;
        this.points = 100000;
    }

    public User(String loginId, String loginPassword, String nickName, String image, UserRoleEnum role, String sellerNickname) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.nickName = nickName;
        this.image = image;
        this.role = role;
        this.points = 100000;
        this.sellerNickname = sellerNickname;
    }


    public void changeSellerByCustomer(Registration registration) {  // 커스터머 > 셀러
        this.role = UserRoleEnum.SELLER;
        this.sellerNickname = registration.getSellerNickname();
        this.sellerImage = registration.getSellerImage();
        this.sellerDetails = registration.getSellerDetail();
        this.sellerCategory = registration.getCategory();
    }

    public void changeCustomerBySeller() { // 셀러 > 커스터머
        this.role = UserRoleEnum.CUSTOMER;
    }


    public void updateUserProfile(UserProfileRequestDto userProfileRequestDto) {
        this.nickName = userProfileRequestDto.getNickname();
        this.image = userProfileRequestDto.getImage();
    }

    public void updateSellerProfile(String sellerNickname, String sellerImage, String sellerDetails, Long sellerCategory) {
        this.sellerNickname = sellerNickname;
        this.sellerImage = sellerImage;
        this.sellerDetails = sellerDetails;
        this.sellerCategory = sellerCategory;
    }

    public void payForOrder(int productPrice, int productQuantity) {
        this.points -= productPrice * productQuantity;
        if (this.points < 0) throw new CustomException(ExceptionStatus.Points_IS_LACKING);
    }

    public void receivePayment(int quantity, int productPrice) {
        this.points += productPrice * quantity;
    }

    public void refundPayment(int quantity, int productPrice) {
        this.points += productPrice * quantity;
    }

    public boolean isVaildateRole(UserRoleEnum seller) {
        return this.role.equals(seller);
    }
}
