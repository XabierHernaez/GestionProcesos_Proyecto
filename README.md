TICKETERA
=============================

Ticketera es una aplicación diseñada para la gestión de tickets de conciertos y eventos especiales. Permite a los usuarios comprar, reservar y administrar sus entradas de manera sencilla y eficiente.

Lanzamiento de la aplicación
-------------------------

Primero, verifica todas las dependencias requeridas especificadas en el archivo pom.xml.

Para la creacion de las tablas de la base de datos, en una terminal de cmd ve a la ruta en el que esta el repositorio
y ejecuta el siguiente comando:

      mysql -u root -p < src\main\resources\dbsetup.sql

Ejecuta el siguiente comando para descargar todas las dependencias y asegurarte de que todo compile correctamente:

      mvn compile

Ahora, inicia el servidor con el siguiente comando:

      mvn spring-boot:run
      mvn exec:java -Dexec.mainClass="com.example.restapi.client.window.WindowLauncher"


otherwise you could use the contents of the file in any other MySQL client you are using.

Now, launch the server using the following command

    mvn spring-boot:run

Si no hay errores, la aplicación estará funcionando en http://localhost:8080/. Puedes presionar Ctrl+C para detener la aplicación.

Pruebas
--------

Para ejecutar las pruebas unitarias, usa el siguiente comando:

    mvn test

Para comprobar el correcto funcionamiento de las pruebas unitarias, usa el siguiente comando:

    mvn verify

Para lanzar las unicamente las pruebas de rendimiento hemos utilizado el comando:

    mvn -Pperformance integration-test 

Y para ejecutar las de integracion se utiliza el comando:

    mvn -Pintegration integration-tes

Documentacion
--------

Para generar la documentacion hay que tener instalado doxygen y configurar el path para su uso
con la ejecucion del siguiente comando se generara la documentacion en target/site/Doxygen

    mvn doxygen:report

Empaquetado de la aplicación
-------------------

La aplicación se puede empaquetar ejecutando el siguiente comando:

    mvn package

See <build> section in pom.xml to see how this command was configured to work.

Packaging the application
-------------------------

Application can be packaged executing the following command

    mvn package

Esto generará un archivo JAR en el directorio target. Luego, el servidor se puede iniciar con:

    java -jar target/ticketera-1.0.jar

Autores
----------

Este proyecto ha sido desarrollado por:

Imanol Suarez

Daniel Bravo

Adrián San José

Xabier Hernaez

Francisco Martin
