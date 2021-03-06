bequ
====

Librerias utilizadas
-------

1. __Dagger:__ Injector de dependencies para android, utilizado para injectar toda la funcionalidad requerida de dropbox.
2. __Butterknife:__ Injector de vistas para elimnar boilerplate de findViewById(...).
3. __Otto:__ Bus de eventos para desacoplar la comunicación entre activitdades y fragmentos.
4. __Volley:__ Libería para abstraer la comunicación por Http, gestión de caché, etc.
5. __Playa:__ Extensión de Volley para construir peticiones de forma fluida (builders) y añadir funcionalidad.
6. __Gson:__ Parser de Json.
7. __IcePick:__ Libería útil para eliminar boilerplate de saveInstance y restoreInstance.

Todas las liberías que podrían tener un efecto negativo en el rendimiento (Dagger, Butterkinfe, IcePick) se basan en la generación de código en compilación en lugar de utilizar Reflection, por lo que su impacto es imperceptible.

La decisión de utilizar estas librerías es disminuir el volumen de código repetitivo. El caso de Dagger es especial porque además es muy cómodo para testear los módulos (aunque en este caso no sea necesario por simplicidad) y abstraer la forma concreta en la que se construyen los objetos que dan acceso a la API de dropbox. 

Decisiones de diseño / arquitectura
-------
La aplicación está construida con 2 actividades y los fragmentos necesarios. De esta forma se pueden realizar cambios de contexto (cambios de actividad) simplemente terminando la actividad actual e iniciando la nueva. 

Realizar este cambio es especialmente útil cuando la aplicación depende de estado externo que en este caso es autenticación en Dropbox. Si la sesión expira simplemente se termina la actividad actual y se inicia el Login de nuevo. Por ello la necesidad Otto, el fragmento que produce el error se comunica con la actividad sin necesidad de una acloparlos fuertemente y se reseulve por completo la gestión de estados en cada petición. 


Respecto a la comunicación con la API de Dropbox se ha optado por __no__ utilizar los métodos porporcionado por el SDK ya que siendo esta RestFul existen librerías capaces de hacer el trabajo de forma mucho más eficiente y con funcionalidad añadida (cacncelación, caché, asyncronismo, etc.) el SDK no ofrece. Utilizar tareas asincronas es problamente la solución más sencilla y al mismo tiempo la peor debido a los leaks que estas producen en los cambios de orientación.

En su lugar se ha utilizado Volley y una extensión propia, Playa, para realizar esta comunicación conseguiendo toda la funcionalidad añadida mencionada anteriormente.

La descarga de ficheros requiere comunicación especial que __no se ha implementado__ ya que no es adecuado realizar la descarga de la misma forma que se realiza una query y existen soluciones más robustas que combinadas con Otto y Dagger producen muy buenos resultados como un IntentService.


Otro punto importante es la interacción con el usuario dónde hay que tener en cuenta que nunca se debería bloquear toda la interfaz (por ejemplo, utilizando un diålogo de carga que tapa toda la pantalla). En su lugar se utiliza una vista especial WLoadingLayout, W es de welvi :), que se combina con Playa para gestionar automáticamente la aparición/desparación del contenido que se está cargando así como crear un botón para reintentar la petición si falla por algún motivo.


Partes ignoradas/modificadas del enunciado
-------
1. __Ordenar los archivos por nombre del libro:__ No es posible sin descargar el libro y leer, si existen, sus metadatos. Por el mismo motivo no hay portada de libro, es necesario descargar el fichero.
2. __Buscar epub:__ En su lugar he buscado archivos pdf (se busca por extensión), no hay epbus en mi cuenta de dropbox pero la funcionalidad es exactamente la misma.
3. __Incluir un menu desplegable:__ En su lugar he utilizado una solución propia de android, que son botones en la ActionBar. Un menú lateral tampoco es adecuado porque es una herramienta de navegación, no de interacción con la pantalla.
4. __Doble click para abrir un libro:__ Considero que el doble click no es un comportamiento adecuado para interactuar en android simplemente porque se producen por error frecuentemente al realizar un click normal. Existen alternativas más adecuadas como un long click o un click simple (que es la que se ha utilizado).








