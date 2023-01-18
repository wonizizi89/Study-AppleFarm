package com.example.applefarm_.admin.service;


import com.example.applefarm_.exception.CustomException;
import com.example.applefarm_.exception.ExceptionStatus;
import com.example.applefarm_.registration.dto.RegistrationResponseDto;
import com.example.applefarm_.registration.entity.Registration;
import com.example.applefarm_.registration.repository.RegistrationRepository;
import com.example.applefarm_.user.dto.UserResponseDto;
import com.example.applefarm_.user.entitiy.User;
import com.example.applefarm_.user.entitiy.UserRoleEnum;
import com.example.applefarm_.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.applefarm_.user.entitiy.UserRoleEnum.CUSTOMER;
import static com.example.applefarm_.user.entitiy.UserRoleEnum.SELLER;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RegistrationRepository registrationRepository;


    @Override
    @Transactional
    public List<UserResponseDto> findCustomerList(int pageChoice) throws IllegalArgumentException {
        Page<User> users = userRepository.findAllByRole(CUSTOMER, pageableSetting(pageChoice));
        List<UserResponseDto> customerResult = users.stream().map(UserResponseDto::new).collect(Collectors.toList());
        return customerResult;
    }
    @Override
    @Transactional
    public List<UserResponseDto> findSellerList(int pageChoice) throws IllegalArgumentException {
        Page<User> sellers = userRepository.findAllByRole(SELLER, pageableSetting(pageChoice));
        List<UserResponseDto> sellerResult = sellers.stream().map(UserResponseDto::new).collect(Collectors.toList());
        return sellerResult;
    }

    private Pageable pageableSetting(int pageChoice) {
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction,"id");
        Pageable pageable = PageRequest.of(pageChoice-1,4,sort);
        return pageable;
    }

    @Override
    @Transactional
    public void modifideroleCustomer(Long id) throws IllegalArgumentException {
        User user = userRepository.findById(id).orElseThrow(
                () ->  new CustomException(ExceptionStatus.DOESN_NOT_USER)
        );
        if(user.getRole() == CUSTOMER){
            Registration registration = registrationRepository.findByUserId(id).orElseThrow(
                    () -> new CustomException(ExceptionStatus.REQURES_IS_EMPTY)
            );
            user.changeSellerByCustomer(registration);
        }else {
            throw new CustomException(ExceptionStatus.NOT_CUSTOMER);
        }
    }
    @Override
    @Transactional
    public void modifideroleSeller(Long id) throws IllegalArgumentException {
        User user = userRepository.findById(id).orElseThrow(
                () ->  new CustomException(ExceptionStatus.DOESN_NOT_USER)
        );
        if(user.getRole() == SELLER){
            user.changeCustomerBySeller();
        }else {throw new CustomException(ExceptionStatus.NOT_SELLER);}
    }

    @Override
    @Transactional
    public List<RegistrationResponseDto> findRegistrationList() {
        List<Registration> registrations = registrationRepository.findAll();
        List<RegistrationResponseDto> registrationResult = new ArrayList<>();
        for (Registration registration: registrations) {
            registrationResult.add(new RegistrationResponseDto(registration));
        }
        return registrationResult;
    };
}