  * [Apache Thrift IDL 简单应用](#apache-thrift-idl-简单应用)
         * [1. demo实战:(环境:Windows10,jdk8)](#1-demo实战环境windows10jdk8)
         * [2. 相关文章](#2-相关文章)

# Apache Thrift IDL 简单应用

### 1. demo实战:(环境:Windows10,jdk8)

1. 环境安装:

   * 下载 thrift.exe:

     * http://www.apache.org/dyn/closer.cgi?path=/thrift/0.13.0/thrift-0.13.0.exe

     * 下载完成重命名为thrift.exe

     * 校验thrift -version

       ![image-20191219174409940](Thrift%E7%AE%80%E5%8D%95%E5%BA%94%E7%94%A8.assets/image-20191219174409940.png)

     * 安装完成

   * 配置环境变量:

     * ![image-20191219173311627](Thrift%E7%AE%80%E5%8D%95%E5%BA%94%E7%94%A8.assets/image-20191219173311627.png)![image-20191219173433037](Thrift%E7%AE%80%E5%8D%95%E5%BA%94%E7%94%A8.assets/image-20191219173433037.png)

2. 代码编写:

   1. 新建maven项目

      1. ```xml
          <dependency>
              <groupId>org.apache.thrift</groupId>
              <artifactId>libthrift</artifactId>
              <version>0.13.0</version>
         </dependency>
         ```

         

   2. 编写IDL数据结构

      1. ```java
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
         ```

         

   3. 生成代码命令:

      1. ```shell
         thrift -gen java data.thrift
         ```

   4. 编写简单业务:

      1. 实现xxxService.Iface

         ```java
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
         ```

         

   5. 编写服务端

      ```java
      public class ThriftServer {
          public static void main(String[] args) throws Exception {
              // 非阻塞socket
              TNonblockingServerSocket socket = new TNonblockingServerSocket(8899);
              //  高可用
              THsHaServer.Args arg = new THsHaServer.Args(socket).minWorkerThreads(2).maxWorkerThreads(4);
      
              PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());
      
              arg.protocolFactory(new TCompactProtocol.Factory());
              arg.transportFactory(new TFramedTransport.Factory());
              arg.processorFactory(new TProcessorFactory(processor));
      
              TServer tServer = new THsHaServer(arg);
              System.out.println("Thrift server start");
      
              tServer.serve();
      
          }
      }
      ```

      

   6. 编写客户端代码

      ```java
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
      ```

      ### 2. 相关文章

      * https://juejin.im/post/5b290dbf6fb9a00e5c5f7aaa   Apache Thrift系列详解