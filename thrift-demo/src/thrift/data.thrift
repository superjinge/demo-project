// 包名
 namespace java com.example.thrift.generated
// 类型定义
typedef i16 short
typedef i32 int
typedef i64 long
typedef bool boolean
typedef string String

// 实体定义
struct Person{
//    姓名
    1:optional String name,
    2:optional int age,
    3:optional boolean married
}

// 异常定义
exception DataException{
    1:optional String message,
    2:optional String callStack,
    3:optional String date
}

// service方法定义
service PersonService{
/*  根据名称获取person */
   Person getPersonByname(1:required String personName) throws(1:DataException dataException)
/*保存person*/
   void savePerson(1:required Person person)throws(1:DataException dataException)

}