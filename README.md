**Serverless Development with Trillo Workbench**

Trillo Workbench is a platform that helps businesses create cloud applications without worrying about the complexities of DevOps, databases, file transfer, and other cloud resources.

Trillo Workbench offers the following features:
* Eliminates the need for DevOps by providing a Kubernetes-based platform.
* Enables the creation of databases and their schemas in a model-driven way.
* Provides an out-of-the-box application for managing files on cloud storage buckets.
* Allows developers to write and debug serverless functions in a local IDE and deploy them to the cloud.

Trillo Workbench offers a seamless development experience for serverless functions.
* Developers can write and debug serverless functions in their local IDE, leveraging source line debugging and breakpoints.
* The functions are executed on the cloud without the developer needing to be aware of the underlying infrastructure.
* Once tested, the functions can be deployed to the cloud via Git or the command line.

![image1.jpg](docs/html/images/image1.jpg)

**Prerequisites for Local Development**

* URL of the Trillo Workbench Runtime on the cloud.
* User ID and password provided by the admin of Trillo Workbench.
* The user should be added to Trillo Workbench using the User Management UI.
* Database tables created and files uploaded using the Trillo Workbench UI.
* Understanding of the anatomy of Trillo Serverless Functions.
* trillo_config.json file with the following information:
    * serverUrl: URL of the Trillo Workbench deployed on the cloud.
    * userId: User ID of the user given by the admin.
    * password: Password of the user.
    * orgName: Name of the workspace (usually "cloud").
    * appName: Name of the application ("main" for the "cloud" workspace).

**Local Development Setup**

1. Clone this repository.
2. Load the project in your IDE using the included POM.
3. Copy the sample_trillo_config.json file to trillo_config.json.
4. Replace the serverUrl, userId, and password values in trillo_config.json.
5. Create the trillo_config.json file if it does not exist.
6. Add the trillo_config.json file to .gitignore to avoid accidental check-in.

**Executing Cloud Functions Locally**

The Run.java class calls cloud functions and executes them. It is called without parameters or with the following parameters:

```
java io.trillo.Run
```

To execute a cloud function, Run calls the executeFunction() method with the following parameters:

```
executeFunction("CustomFunction","./input_files/ParameterInput.json");
```

Here, the “CustomFunction" is the function's name, and "./input_files/ParameterInput.json" is the input file containing the parameters to be passed to the function.

**Writing Serverless Functions**

Serverless functions hosted on Trillo Workbench follow a structured format described in the Trillo Workbench documentation. These functions are written under the "com.serverless.function" package in the local environment.

**Invoking Cloud Services**

Trillo Workbench provides an abstraction layer for several cloud services, including Google Cloud SQL, Google Cloud Storage, BigQuery, and many cloud services by Azure and AWS clouds. These services are available in serverless functions through an SDK (Software Development Kit).

In the local environment, a proxy implementation of the SDK that proxies each call to the Cloud is provided. This allows developers to run programs locally while all cloud services are executed on the cloud.

**Allowed Libraries**

All packages, classes, and methods of the JDK can be used in the code. Several libraries of Apache Commons are also available.

**References**
* The serverless functions hosted on the Trillo Workbench follow a structure defined at https://trillo.gitbook.io/trillo-workbench/serverless-functions.

