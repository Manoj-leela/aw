package sg.activewealth.roboadvisor.infra.enums.converter;

import javax.persistence.AttributeConverter;

import sg.activewealth.roboadvisor.infra.enums.IAttributeEnum;

public class EnumAttributeConverter<A, T extends IAttributeEnum<A, T>>
        implements AttributeConverter<T, A> {

    T attribute;


    @Override
    public A convertToDatabaseColumn(T aAttribute) {
        if (aAttribute == null) {
            return null;
        }
        this.attribute = aAttribute;

        return aAttribute.getValue();
    }

    @Override
    public T convertToEntityAttribute(A dbData) {
        if (dbData == null) {
            return null;
        }
        return attribute.getEnum(dbData);
    }


}
