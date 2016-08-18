The plugin is build targeting Web 8.1.1 or above, once the plugin is built it most likely will work with any 8.x version of SDL Web.

# Functionality


# Install Maven dependencies
In order to build this plugin you will need to have the dependencies for the Deployer installed. You can do this by providing the Jar files from your local SDL Web distribution layout 
and installing them as Maven dependencies in your local maven repository.

```
mvn install:install-file -Dfile=cd_deployer-8.1.1-1016.jar -DgroupId=com.sdl.delivery -DartifactId=cd_deployer -Dversion=8.1.1 -Dpackaging=jar
mvn install:install-file -Dfile=deployer-api-8.1.1-1016.jar -DgroupId=com.sdl.delivery -DartifactId=deployer-api -Dversion=8.1.1 -Dpackaging=jar

mvn install:install-file -Dfile=cd_model-8.1.1-1008.jar -DgroupId=com.tridion -DartifactId=cd_model -Dversion=8.1.1 -Dpackaging=jar

mvn install:install-file -Dfile=cd_common_config_legacy-8.1.1-1005.jar -DgroupId=com.tridion -DartifactId=cd_common_config_legacy -Dversion=8.1.1 -Dpackaging=jar


mvn install:install-file -Dfile=cd_core-8.1.1-1010.jar -DgroupId=com.tridion -DartifactId=cd_core -Dversion=8.1.1 -Dpackaging=jar

mvn install:install-file -Dfile=cd_common_util-8.1.1-1006.jar -DgroupId=com.tridion -DartifactId=cd_common_util -Dversion=8.1.1 -Dpackaging=jar
```

Please make sure to replace the filename with the actual filename your are installing, in above case jar files with exact versions are used, pending on your SDL Web 8.x version the filenames
might be called slightly differently.