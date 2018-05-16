package fi.iki.vihonen.sparklambda;

import static spark.Spark.*;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.*;
import com.amazonaws.services.lambda.model.*;
import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {

        // AWS key and secret are expected as command line parameters.
        Options options = new Options();
        options.addRequiredOption("k", "awsAccessKey", true, "AWS Access Key");
        options.addRequiredOption("s", "awsSecretKey", true, "AWS Secret Key");
        options.addRequiredOption("r", "awsRegion", true, "AWS Region");

        String awsAccessKey = "";
        String awsSecretKey = "";
        String awsRegion = "";

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            awsAccessKey = cmd.getOptionValue("k");
            awsSecretKey = cmd.getOptionValue("s");
            awsRegion = cmd.getOptionValue("r");
        }
        catch (ParseException e) {
            System.out.println( "Unexpected exception:" + e.getMessage() );
            System.exit(500);
        }

        AWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        AWSLambda lambdaClient = AWSLambdaClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(awsRegion).build();

        String response = "<ul>";

        for(FunctionConfiguration conf: lambdaClient.listFunctions().getFunctions()) {
            get("/" + conf.getFunctionName(), (req, res) -> {
                InvokeRequest invokeRequest =  new InvokeRequest().withFunctionName(conf.getFunctionName()).withPayload(req.body());
                InvokeResult result = lambdaClient.invoke(invokeRequest);
                return result.getPayload();
            });
            response += "<li><a href=\"" + conf.getFunctionName() + "\">/" +  conf.getFunctionName() + "</a></li>";
        }

        response += "</ul>";

        final String resp = response;

        get("/", (req, res) -> resp);
    }

}
