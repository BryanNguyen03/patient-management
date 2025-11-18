#!/bin/bash

set -e # Stop script if any command fails

aws --endpoint-url=http://localhost:4566 cloudformation deploy \
    --stack-name patient-management \
    --template-file "./cdk.out/localstack.template.json"

# elastic load balancer for our API gateway
aws --endpoint-url=http://localhost:4566 elbv2 describe-load-balancers \
    --query "LoadBalancers[0].DNSName" --output text
