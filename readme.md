# Spark Lambda

## Overview

Spark Lambda is a POC for an "API Gateway" for AWS lambda functions that could be useful in some cases where calling lambdas might be otherwise difficult.

## Prerequisites

For your environment you're going to need Java and Maven.

In addition to the environment you need an AWS account with programmatic access and suitable IAM policies attached to it.

You can use for example the following policy as a directly attached inline policy:

```javascript
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "VisualEditor0",
            "Effect": "Allow",
            "Action": [
                "lambda:InvokeFunction",
                "lambda:ListVersionsByFunction",
                "lambda:GetFunction",
                "lambda:ListAliases",
                "lambda:GetFunctionConfiguration"
            ],
            "Resource": "arn:aws:lambda:*:*:function:*"
        },
        {
            "Sid": "VisualEditor1",
            "Effect": "Allow",
            "Action": [
                "lambda:ListFunctions",
                "lambda:ListEventSourceMappings"
            ],
            "Resource": "*"
        }
    ]
}
```

## Instructions

Spark Lambda is a Sparkjava application that is built with Maven and produces a "fat" .jar file.

Execute the following steps in your terminal to launch the application:

```bash
mvn clean package
java -jar target/sparklambda.jar -k your_aws_key_here -s your_aws_secret_here 
```

The application will launch and port will be visible on the output