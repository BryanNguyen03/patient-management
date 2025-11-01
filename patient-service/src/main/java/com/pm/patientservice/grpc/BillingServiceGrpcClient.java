package com.pm.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    // Synchronous gRPC client stub for the Billing service, used to make RPC calls to the server
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;


    /**
     * Construct new gRPC client for billing service
     * Creates a managed channel to connect to the remote billing service and initializes a blocking stub
     * for synchronous RPC calls
     * @param serverAddress address of billing service gRPC server
     * @param serverPort the port number the billing service will be listening on
     */
    public BillingServiceGrpcClient(@Value("${billing.service.address:localhost}") String serverAddress,
                                    @Value("${billing.service.grpc.port:9001}") int serverPort) {

        log.info("Connecting to billing service GRPC server at {}:{}", serverAddress, serverPort);

        // Use plaintext instead of TLS for internal service-to-service communication
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();

        // use blockingStub so that our client waits until a response is received
        blockingStub = BillingServiceGrpc.newBlockingStub(channel);

    }

    /**
     * Create a new billing account for patient
     * @param patientId ID for patient
     * @param name name of patient
     * @param email email of patient
     * @return BillingResponse containing the result of the account creation
     */
    public BillingResponse createBillingAccount(String patientId, String name, String email) {

        // build gRPC request message with patient details
        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(name)
                .setEmail(email)
                .build();

        // make sync gRPC call, block until response received
        BillingResponse response = blockingStub.createBillingAccount(request);
        log.info("Received response from billing service via GRPC: {}", response);
        return response;
    }

}
