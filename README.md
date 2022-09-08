# Introduction to using GitHub Actions Runner on OpenShift.

Welcome to the Introduction to using GitHub Actions Runner on  OpenShift !! 


This demo will show how to use self-Hosted GitHub Actions Runners to build and deploy a Quarkus application. For more information on the code refer [here](docs/app-README.md).

## GitHub Action
[GitHub Action](https://github.com/features/actions), automate, customize and execute your software development workflows right in your repository. You can discover, create and share actions to perform any job you'd like, including CI/CD and combine theses actions in a completely customized workflow.


## GitHub Action Runner
[GitHub Action Runner](https://github.com/actions/runner), is the application that runs a job from a GitHub Actions workflow. It is used by GitHub Actions in the hosted virtual environments, or you can self-host the runner in your own environment.

By default, the infrastructure is provides by GiHub, however, it possible for users to run their own runners, this is call `Self-hosted runners`. They can be almost any physical or virtual machine, with the the runner software supporting many operating systems and architectures.

Benefits:
1. Works with GitHub Enterprise Service.
1. No Usage limits
1. Persistent disk


## Overview

In this demo we will walk you through how to use a self-hosted `GitHub Action Runner` on openshift, to build and deploy code on `Red Hat Openshift`. Wee will also secure the GitHub Action using [Azure Key Vault](https://azure.microsoft.com/en-us/services/key-vault/#product-overview). We will take a look at the self-hosted runner, to run a customize and secure GitHub Actions Workflows using different component from the [Red Hat GitHub Action Page](https://github.com/redhat-actions).


### Prerequisites

* Any Openshift cluster 4.x.
* OpenShift CLI `oc` install and connected to your cluster
* [Helm](https://helm.sh/)
* Access to [GitHub](https://github.com)
* Access to [Quay.io](https://quay.io/)
* Access to [Azure Porttal](https://portal.azure.com/#home)


### Creation of the Azure Key Vault
:warning: TBD

### Creation of the Azure Service Principal to access Key Vault.
:warning: TBD



#### OpenShift Actions runners
The easiest way to add self-hosted runners to your Red Hat OpenShift environment is to use the `OpenShift Actions Runner Installer`.

For this demo we will be using GitHug Actions runners onto an existing OpenShift cluster. Red Hat as developed a set of tools to help installing this.
* [OpenShift Action runner](https://github.com/redhat-actions/openshift-actions-runners) which consist of a set of container images tahat run the GitHub Actions runner
* [OpenShift Runner Chart](https://github.com/redhat-actions/openshift-actions-runner-chart), Helm chart to deploy pods from those images.
* [OpenShift Actions Runner Installer](https://github.com/redhat-actions/openshift-actions-runner-installer), an action to automate the helm install, building the runner mangement into your workflows.

###### OpenShift Action Runner Installer

1. Connect to OpenShift.
2. Create a new project
    ```
    oc new-project github-runner
    ```
3. Follow the steps listed to install [openshift-action-runners](https://github.com/redhat-actions/openshift-actions-runners)


---

### Creation of a service account on OpenShift

When logging in to an OpenShift cluster from an automated environment, it is recommended to use a functional Service Account rather than personal credentials. Refer [here](https://cookbook.openshift.org/accessing-an-openshift-cluster/how-can-i-create-a-service-account-for-scripted-access.html).

Steps: ( They were validated on MacOs/Linux)
1. Define the service account name
    ```
    export SA=github-actions-sa
    ```
1. Create the service account.
    ```
    oc create sa $SA
    ```
1. Find the secrest that were created.
    ```
    export SECRETS=$(oc get sa $SA -o jsonpath='{.secrets[*].name}{"\n"}') && echo $SECRETS
    ```
1. Describe the secret that contains the word `token. We will need this token in the Key Vault later.
    ```
    oc describe secret github-actions-sa-token-[REPLACE_WITH_YOUR_VALUE]
    ```
1. Give write permission to the Service Account
    ```
    oc policy add-role-to-user edit -z $SA
    ```


---

### Adding secret in Azure Key Vault.

We need to create 3 secrets in the azure key vault.

1. `registry-pwd` -> The registry robot connection password.
    Can be found on the registry. in this case we use quay.io
2. `ocp-server` -> The OpenShift server location
    ```
    oc whoami --show-server
    ```
3. `ocp-github-actions-sa-token` -> The service account access token.   
    Retrieve at the previous step. Creation of a service account.

![azurekeyvault](docs/images/azure-key-vault-1.png)

:clipboard: Make sure you have the permission on the user.
>oc policy add-role-to-user edit system:serviceaccount:[NAMESPACE]:[SERVICE_ACCOUNT]

   
 ![all-secret](docs/images/all-secrets.png)
#### Setup the GitHub Action.

1. Access GitHub.com in the proper repository and go in to the Actions tab.
![actiontab](docs/images/actionTab.png)

1. Scroll down to the deployment section and select `OpenShift`. 
![actiontab-1](docs/images/actionTab-deployment.png)

1. Click on `Configure` to edit the yaml
![edit-yaml](docs/images/edit-simpleworkflow.png)


###### Setup the image registry
:warning: for the image registry access I use a Robot that I have created in quay.io instead of my own username/password.

* OPENSHIFT_NAMESPACE: Enter the namespace to use.
* IMAGE_REGISTRY: enter your quay.io repository
* IMAGE_REGISTRY_USER: enter the robot login name
* IMAGE_REGISTRY_PASSWORD: replace by a secret value ${{ secrets.IMAGE_REGISTRY_PASSWORD }} ```

* Click `Start commit`



* The pipeline should start executing and you can check it executing.
![pipeline-execute](docs/images/pipelie-execute.png)

:eyeglasses: You can access the application by clicking on the link highlighted above.

You should now see your apps in OpenShift
![OCP-TOPOLOGY](docs/images/openshift-topology.png)

---