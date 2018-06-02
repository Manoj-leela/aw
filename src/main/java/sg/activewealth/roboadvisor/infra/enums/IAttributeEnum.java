package sg.activewealth.roboadvisor.infra.enums;

public interface IAttributeEnum<X, T> {

    public X getValue();

    public T getEnum(X value);

}
