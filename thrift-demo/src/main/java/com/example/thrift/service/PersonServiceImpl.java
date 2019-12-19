package com.example.thrift.service;

import com.example.thrift.generated.DataException;
import com.example.thrift.generated.Person;
import com.example.thrift.generated.PersonService;
import org.apache.thrift.TException;

/**
 * Description: java类作用描述
 *
 * @author superjinge
 * @date 2019/12/18
 * @since V1.0
 */
public class PersonServiceImpl implements PersonService.Iface {
    @Override
    public Person getPersonByname(String personName) throws DataException, TException {
        System.out.println("进入 getPersonByname 方法");
        Person person = new Person();
        person.setAge(16);
        person.setName("张三");
        person.setMarried(false);
        return person;
    }

    @Override
    public void savePerson(Person person) throws DataException, TException {
        System.out.println("进入 savePerson 方法");
        System.out.println(person.getName());
        System.out.println(person.getAge());
        System.out.println(person.isMarried());
    }
}
