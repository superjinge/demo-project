package com.example.thrift.application;

import com.example.thrift.generated.PersonService;
import com.example.thrift.service.PersonServiceImpl;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;

/**
 * Description: Thrift server  服务器
 *
 * @author superjinge
 * @date 2019/12/18
 * @since V1.0
 */
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
