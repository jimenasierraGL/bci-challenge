package bci.challenge.model.mapper;

import bci.challenge.model.dto.PhoneDto;
import bci.challenge.model.entity.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PhoneMapper {

    PhoneMapper INSTANCE = Mappers.getMapper(PhoneMapper.class);
    List<Phone> mapPhoneDtoListToPhoneList(List<PhoneDto> phone);
}
