package com.example.thrift.application;

import com.example.thrift.generated.Person;
import com.example.thrift.generated.PersonService;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Description: java类作用描述
 *
 * @author superjinge
 * @date 2019/12/18
 * @since V1.0
 */
public class ThriftClient {
    public static void main(String[] args) {
        TTransport tTransport = new TFramedTransport(new TSocket("localhost", 8899), 600);
        TProtocol protocol = new TCompactProtocol(tTransport);
        PersonService.Client client = new PersonService.Client(protocol);

        try {
            System.out.println("Thrift client start");
            tTransport.open();
            Person personzhang = client.getPersonByname("张三");

            System.out.println(personzhang.getName());
            System.out.println(personzhang.getAge());
            System.out.println(personzhang.isMarried());
            System.out.println("=============");
            Person person = new Person();
            person.setAge(22);
            person.setName("李四");
            person.setMarried(true);
            client.savePerson(person);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            tTransport.close();
        }
    }
}
