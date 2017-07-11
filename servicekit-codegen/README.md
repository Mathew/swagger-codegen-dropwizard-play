Servicekit-codegen
------------------


Intro
-----

An opinionated codegen module for swagger.
Uses Dropwizard as a base.
Uses swagger.yaml as the source to generate files from.


Usage
-----

Only edit your interfaces and models through the swagger.yaml file associated with your project.
Don't use the swagger online editor, it doesn't know how to handle refs, instead edit the files locally using an editor and check them in the swagger docs viewer.

The first time you run the codegen this will generate a bunch of application files.  It also generates a .swagger-codegen-ignore file to prevent overwriting classes you may change.  The only items generated after the initial run should be the interfaces and models unless you modify this file.

See the servicekit-service-example pomfile for an example.


Interfaces
----------

Implement the generated ones.  As per the JAXRS spec add the jax annotations again on the implementation (specifically the class level PATH one).


