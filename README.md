# This is an application built for Quarkus Hackathon

## Environment Requirements

JDK 8 or 11+ installed with JAVA_HOME configured appropriately

Apache Maven 3.6.2+

## Application Build

In terminal, go to the root folder of this project and type:

``mvn package``

This should build everything you need and necessary scripts inside the `target` folder.

## Application Deployment

After the application is built, quarkus should generate a file `manage.sh`.
You need to set up your local `aws cli` to utilize this. You can follow this site `https://docs.aws.amazon.com/polly/latest/dg/setup-aws-cli.html`

Type `aws s3 ls` to test if your `aws cli` is set up properly. It should list everything you have in your s3.

Now follow `https://docs.aws.amazon.com/lambda/latest/dg/lambda-intro-execution-role.html` to create an execution role.

Copy your execution role ARN, either put it as your environment variable or put it inside `manage.sh` as `LAMBDA_ROLE_ARN`.

now write the script `sh manage.sh create` to create your lambda function.
You should get a successful message.

## Set up Alexa Skill Development Console to test
// To do


## Quarkus Related Functionalities

### Logging Ability
Extension has already been added to the project with root logging level set to info.
To generate regular logs in other classes:

```$xslt
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import these libraries;

private static final Logger log = LoggerFactory.getLogger(ShoppingStreamHandler.class);
// Declare and create a Logger using LoggerFactory;


log.error("log test");
// Start logging in your code;
```

### Fault Tolerance Capabilities
Extension for Fault Tolerance was already added. To use Fault Tolerance functionalities, simply use CDI's.
Easy to use CDI's:
```$xslt
@Retry(maxRetries = 3)
// The method would retry 3 times if it fails;
// 
@Timeout(250)
// The method will timeout if not done within 250ms;
```

There are other useful CDI's, checkout Quarkus guide for additional resources.
