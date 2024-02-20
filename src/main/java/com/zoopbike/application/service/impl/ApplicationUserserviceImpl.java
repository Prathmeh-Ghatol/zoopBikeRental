package com.zoopbike.application.service.impl;

import com.zoopbike.application.dto.ApplicationUserDto;
import com.zoopbike.application.dto.CurrentAddressDto;
import com.zoopbike.application.dto.GenricPage;
import com.zoopbike.application.dto.PermenetAddressDto;
import com.zoopbike.application.entity.ApplicationUser;
import com.zoopbike.application.entity.CurrentAddress;
import com.zoopbike.application.entity.Permanentaddress;
import com.zoopbike.application.exception.ApplicationUserException;
import com.zoopbike.application.exception.BadaddressException;
import com.zoopbike.application.repo.ApplicationUserRepo;
import com.zoopbike.application.repo.CurrentAddressRepo;
import com.zoopbike.application.repo.PermentAddressRepo;
import com.zoopbike.application.service.ApplicationUserService;
import com.zoopbike.application.utils.CacheStore;
import com.zoopbike.application.utils.ObjectMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.zoopbike.application.utils.SqlQuery.findApplicationUserByEmail;

@Service
public class ApplicationUserserviceImpl implements ApplicationUserService {

    private ApplicationUserRepo applicationUserRepo;
    private PermentAddressRepo permentAddressRepo;
    private CurrentAddressRepo currentAddressRepo;
    private ObjectMappingService objectMappingService;

    private CacheStore cacheStore;

    @Autowired
    ApplicationUserserviceImpl(ApplicationUserRepo applicationUserRepo, PermentAddressRepo permentAddressRepo,
                               CurrentAddressRepo currentAddressRepo,
                               ObjectMappingService objectMappingService, CacheStore cacheStore) {

        this.applicationUserRepo = applicationUserRepo;
        this.permentAddressRepo = permentAddressRepo;
        this.currentAddressRepo = currentAddressRepo;
        this.objectMappingService = objectMappingService;
        this.cacheStore = cacheStore;

    }

    @Override
    public ApplicationUserDto registerApplicationUser(ApplicationUserDto applicationUserDto) {
        ApplicationUser applicationUser = this.objectMappingService.pojoToentity(applicationUserDto, ApplicationUser.class);
        System.out.println(applicationUser);
        PermenetAddressDto permenetAddressDto = applicationUserDto.getPermenetAddressDto();
        CurrentAddressDto currentAddressDto = applicationUserDto.getCurrentAddressDto();
        CurrentAddress currentAddress;
        Permanentaddress permanentaddress = null;
        if (currentAddressDto == null && applicationUserDto.getCurrentAddressSameToPermentAddress() == true) {
            if (permenetAddressDto != null) {
                permanentaddress = this.objectMappingService.pojoToentity(permenetAddressDto, Permanentaddress.class);
            }
            currentAddress = this.objectMappingService.permeantAddressTocurrentAddress(permanentaddress, CurrentAddress.class);
            if (currentAddress != null) {
                applicationUser.setCurrentAddress(currentAddress);
                currentAddress.setApplicationUser(applicationUser);


                permanentaddress.setApplicationUser(applicationUser);
                applicationUser.setPermanentaddress(permanentaddress);
            }
        } else if (currentAddressDto == null && applicationUserDto.getCurrentAddressSameToPermentAddress() == false) {
            throw new BadaddressException("Please enter your current address", "currentAddress");
        } else {
            currentAddress = this.objectMappingService.pojoToentity(applicationUserDto.getCurrentAddressDto(), CurrentAddress.class);
            permanentaddress = this.objectMappingService.pojoToentity(permenetAddressDto, Permanentaddress.class);
            permanentaddress.setApplicationUser(applicationUser);
            currentAddress.setApplicationUser(applicationUser);
            applicationUser.setPermanentaddress(permanentaddress);
            applicationUser.setCurrentAddress(currentAddress);
        }

        ApplicationUser applicationUserReturn = this.applicationUserRepo.save(applicationUser);
        ApplicationUserDto applicationUserDto1 = this.objectMappingService.entityToPojo(applicationUserReturn, ApplicationUserDto.class);
        CurrentAddressDto currentAddressDTO = this.objectMappingService.entityToPojo(applicationUserReturn.getCurrentAddress(), CurrentAddressDto.class);
        PermenetAddressDto permanentaddressDTO = this.objectMappingService.entityToPojo(applicationUserReturn.getPermanentaddress(), PermenetAddressDto.class);
        applicationUserDto1.setCurrentAddressDto(currentAddressDTO);
        applicationUserDto1.setPermenetAddressDto(permanentaddressDTO);
        this.cacheStore.applicationUserMap.put(applicationUserDto1.getEmail(), applicationUserReturn);
        return applicationUserDto1;
    }

    public ApplicationUserDto update(String email,  ApplicationUserDto applicationUserDto) {
        ApplicationUser applicationUser = this.applicationUserRepo.findApplicationUserByEmail(email);
        if (applicationUser == null) {
            throw new ApplicationUserException("User Not found with this email", email);
        }
        CurrentAddress currentAddress=null;
        Permanentaddress permanentaddress=null;
        applicationUser.setApplication_Username(applicationUserDto.getApplication_Username());
        applicationUser.setCellNumber(applicationUserDto.getCellNumber());
        currentAddress = this.objectMappingService.pojoToentity(applicationUserDto.getCurrentAddressDto(), CurrentAddress.class);
        applicationUser.setCurrentAddress(currentAddress);
        permanentaddress = this.objectMappingService.pojoToentity(applicationUserDto.getPermenetAddressDto(), Permanentaddress.class);
        applicationUser.setPermanentaddress(permanentaddress);
        applicationUser = this.applicationUserRepo.save(applicationUser);
        this.cacheStore.applicationUserMap.put(email, applicationUser);
        ApplicationUserDto applicationUserDtoReturn=objectMappingService.entityToPojo(applicationUser, ApplicationUserDto.class);
        CurrentAddressDto currentAddressDto=this.objectMappingService.entityToPojo(applicationUser.getCurrentAddress(), CurrentAddressDto.class);

        PermenetAddressDto permenetAddressDto=this.objectMappingService.entityToPojo(applicationUser.getPermanentaddress(), PermenetAddressDto.class);
        applicationUserDtoReturn.setCurrentAddressDto(currentAddressDto);
        applicationUserDtoReturn.setPermenetAddressDto(permenetAddressDto);
        return applicationUserDto;
    }

    public Boolean deRegisterApplicationUser(String email) {
        ApplicationUser applicationUser = this.applicationUserRepo.findApplicationUserByEmail(email);
        Boolean deleteConfirmationFlag = false;
        if (applicationUser == null) {
            throw new ApplicationUserException("User Not found with this email", email);
        }
        this.applicationUserRepo.delete(applicationUser);

        ApplicationUser applicationUserAfterDelete = this.applicationUserRepo.findApplicationUserByEmail(email);

        if (applicationUserAfterDelete == null) {
            deleteConfirmationFlag = true;
        }
        this.cacheStore.applicationUserMap.remove(email);
        return deleteConfirmationFlag;

    }

    public ApplicationUserDto getApplicationUserByEmailorId(String email, UUID uuid) {
        ApplicationUserDto applicationUserDto = null;
        ApplicationUser applicationUser = null;
        if (email != null) {
            applicationUser = this.cacheStore.applicationUserMap.get(email);
            if (applicationUser != null) {
                applicationUserDto = this.objectMappingService.entityToPojo(applicationUser, ApplicationUserDto.class);
                CurrentAddressDto currentAddressDto = this.objectMappingService.entityToPojo(applicationUser.getCurrentAddress(), CurrentAddressDto.class);
                PermenetAddressDto permanentaddressDto = this.objectMappingService.entityToPojo(applicationUser.getPermanentaddress(), PermenetAddressDto.class);
                applicationUserDto.setCurrentAddressDto(currentAddressDto);
                applicationUserDto.setPermenetAddressDto(permanentaddressDto);
                System.out.println("******************comming from map");
                return applicationUserDto;

            }
            applicationUser = this.applicationUserRepo.findApplicationUserByEmail(email);
            if (applicationUser != null) {
                applicationUserDto = this.objectMappingService.entityToPojo(applicationUser, ApplicationUserDto.class);
                CurrentAddressDto currentAddressDto = this.objectMappingService.entityToPojo(applicationUser.getCurrentAddress(), CurrentAddressDto.class);
                PermenetAddressDto permanentaddressDto = this.objectMappingService.entityToPojo(applicationUser.getPermanentaddress(), PermenetAddressDto.class);
                applicationUserDto.setCurrentAddressDto(currentAddressDto);
                applicationUserDto.setPermenetAddressDto(permanentaddressDto);
                System.out.println("******************comming from repo");
                return applicationUserDto;
            } else {
                throw new ApplicationUserException("User not found !!", email);
            }
        }

        Optional<ApplicationUser> applicationUser_obj2 = this.applicationUserRepo.findById(uuid);
        if (applicationUser_obj2 == null) {
            throw new ApplicationUserException("User not found !!", " " + uuid);
        }
        applicationUser = applicationUser_obj2.get();
        applicationUserDto = this.objectMappingService.entityToPojo(applicationUser, ApplicationUserDto.class);
        CurrentAddressDto currentAddressDto = this.objectMappingService.entityToPojo(applicationUser.getCurrentAddress(), CurrentAddressDto.class);
        PermenetAddressDto permanentaddressDto = this.objectMappingService.entityToPojo(applicationUser.getPermanentaddress(), PermenetAddressDto.class);
        applicationUserDto.setCurrentAddressDto(currentAddressDto);
        applicationUserDto.setPermenetAddressDto(permanentaddressDto);
        return applicationUserDto;

    }

    public GenricPage<ApplicationUserDto> getAllUser(int pageNo, int pageSize){

        Pageable pageable= PageRequest.of(pageNo, pageSize);
        Page<ApplicationUser>page=this.applicationUserRepo.getAllapplicationUser(pageable);
        List<ApplicationUser>applicationUsers=page.getContent();
        System.out.println(applicationUsers);
        List<ApplicationUserDto> allApplicationUserDto= applicationUsers.stream().map(applicationUser -> {
                CurrentAddressDto currentAddressDto = objectMappingService.entityToPojo(applicationUser.getCurrentAddress(), CurrentAddressDto.class);
                PermenetAddressDto permenetAddressDto = objectMappingService.entityToPojo(applicationUser.getPermanentaddress(), PermenetAddressDto.class);
                ApplicationUserDto applicationUserDto = objectMappingService.entityToPojo(applicationUser, ApplicationUserDto.class);
                applicationUserDto.setCurrentAddressDto(currentAddressDto);
                applicationUserDto.setPermenetAddressDto(permenetAddressDto);
                return applicationUserDto;
            }).collect(Collectors.toList());

        GenricPage<ApplicationUserDto>applicationUserGenricPage=new GenricPage<>();
        applicationUserGenricPage.setContent(allApplicationUserDto);
        applicationUserGenricPage.setIsLastPage(page.isLast());
        applicationUserGenricPage.setPageNo(page.getNumber());
        applicationUserGenricPage.setTotalPage(page.getTotalPages());
        applicationUserGenricPage.setPageSize(page.getSize());
        return applicationUserGenricPage;


    }

}