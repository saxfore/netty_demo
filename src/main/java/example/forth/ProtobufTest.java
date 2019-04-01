package example.forth;

import com.google.protobuf.InvalidProtocolBufferException;
import protobuf.DataInfo;

public class ProtobufTest {

    public static void main(String[] args) throws InvalidProtocolBufferException {

        DataInfo.Person person = DataInfo.Person.newBuilder()
                .setName("John Wang")
                .setEmail("John@163.com")
                .setId(432500)
                .addPhoneNumber(DataInfo.Person.PhoneNumber.newBuilder()
                        .setNumber("176****7574")
                        .setType(DataInfo.Person.PhoneType.WORK)
                        .build())
                .addPhoneNumber(DataInfo.Person.PhoneNumber.newBuilder()
                        .setNumber("139****9471")
                        .setType(DataInfo.Person.PhoneType.HOME)
                        .build())
                .build();
        byte[] personByte = person.toByteArray();

        DataInfo.Person parsePerson = DataInfo.Person.parseFrom(personByte);
        System.out.println("parsePerson = [" + parsePerson + "]");

    }

}
