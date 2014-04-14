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
7. __IcePick:__ Libería útil para eliminar boilerplate de saveInstance y Restore instance.

Todas las liberías que podrían tener un efecto negativo en el rendimiento (Dagger, Butterkinfe, IcePick) se basan en la generación de código en compilación en lugar de utilizar Reflection, por lo que su impacto es imperceptible.

La decisión de utilizar estas librerías es disminuir el volumen de código repetitivo. El caso de Dagger es especial porque además es muy cómodo para testear los módulos (aunque en este caso no sea necesario por simplicidad) y abstraer la forma concreta en la que se construyen los objetos que dan acceso a la API de dropbox. 

Decisiones de diseño / arquitectura
-------
La aplicación está construida con 2 actividades y los fragmentos necesarios. De esta forma se pueden realizar cambios de contexto (cambios de actividad) simplemente terminando la actividad actual e iniciando la nueva. 

Realizar este cambio es especialmente útil cuando la aplicación depende de estado externo que en este caso es autenticación en Dropbox. Si la sesión expira simplemente se termina la actividad actual y se inicia el Login de nuevo. Por ello la necesidad Otto, el fragmento que produce el error se comunica con la actividad sin necesidad de una acloparlos fuertemente y se reseulve por completo la gestión de estados en cada petición. 


Respecto a la comunicación con la API de Dropbox se ha optado por __no__ utilizar los métodos porporcionado por el SDK ya que siendo esta RestFul existen librerías capaces de hacer el trabajo de forma mucho más eficiente y con funcionalidad añadida (cacncelación, caché, asyncronismo, etc.) que el SDK no ofrece. Utilizar tareas asincronas es problamente la solución más sencilla y al mismo tiempo la peor debido a los leaks que estas producen.

En su lugar se ha utilizado Volley y una extensión propia, Playa, para realizar esta comunicación conseguiendo toda la funcionalidad añadida mencionada anteriormente.

La descarga de ficheros requiere comunicación especial que no se ha implementado ya que no es adecuado realizar la descarga de la misma forma que se realiza una query y existen soluciones más robustas para ello que combinadas con Otto y Dagger producen muy buenos resultados como un IntentService.










