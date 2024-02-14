package com.zoopbike.application.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ObjectMappingService {
    @Autowired
    private ModelMapper modelMapper;
    public <P,E> P entityToPojo(E e,Class<P> pojo){
        P returnpojo =this.modelMapper.map(e, pojo);
        return returnpojo;
    }
    public <P,E> E pojoToentity(P pojo, Class<E>entity){
      E returnentity=  this.modelMapper.map(pojo, entity);
    return returnentity;
    }
    public <C,P> P currentAddressToPermeantAddress(C currentAdd, Class<P>permentAddress){
        P permentAddressReturn=  this.modelMapper.map(currentAdd,permentAddress);
        return permentAddressReturn ;
    }
    public <C,P> C permeantAddressTocurrentAddress(P permentAddress, Class<C>currentAdd){
        C currentAddressReturn=  this.modelMapper.map(permentAddress,currentAdd);
        return currentAddressReturn ;
    }
}
