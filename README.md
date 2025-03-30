TICKETERA
=============================

Ticketera es una aplicación diseñada para la gestión de tickets de conciertos y eventos especiales. Permite a los usuarios comprar, reservar y administrar sus entradas de manera sencilla y eficiente.

Lanzamiento de la aplicación
-------------------------

Primero, verifica todas las dependencias requeridas especificadas en el archivo pom.xml.

Ejecuta el siguiente comando para descargar todas las dependencias y asegurarte de que todo compile correctamente:

      mvn compile

Ahora, inicia el servidor con el siguiente comando:

      mvn spring-boot:run

otherwise you could use the contents of the file in any other MySQL client you are using.

Now, launch the server using the following command

    mvn spring-boot:run

Si no hay errores, la aplicación estará funcionando en http://localhost:8080/. Puedes presionar Ctrl+C para detener la aplicación.

Pruebas
--------

Para ejecutar las pruebas unitarias, usa el siguiente comando:

    mvn test

Empaquetado de la aplicación
-------------------

La aplicación se puede empaquetar ejecutando el siguiente comando:

    mvn package

See <build> section in *pom.xml* to see how this command was configured to work.

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
