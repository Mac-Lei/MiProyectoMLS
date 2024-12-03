# Evaluación Final Unidad III
# Descripción
Este proyecto es una aplicación Android de un carrito de compra. Dicha aplicación lleva por nombre "PickEasy". Su función es la compra y venta de productos de supermercado.

# Funciones
El Login tiene dos opciones, "Cliente" y "Administrador". Esto lo implementé con SQLite. Al momento de ingresar a la aplicación, el usuario tiene que seleccionar uno de las dos opciones, pero si ingresa credenciales que son de cliente en administrador o viceversa no admite iniciar sesión, tiene que poner las credenciales correctas.
Las tablas productos y compras las hice con Firebase, específicamente con Realtime Database, por ende las compras que hace el cliente y los productos que ingresa el administrador se guardan aquí.

# Explicación "Cliente"
Al iniciar sesión con cliente aparece una ventana con todos los productos que existen. El usuario tiene que seleccionar un producto, poner la cantidad que quiere y después agregarlo, tiene que repetir ese paso hasta que termine de comprar, una vez lista la compra tiene que confirmar con el botón "Confirmar Compra". Una vez que pincha ese botón, sale un resumen y el total de la compra, además de un botón para cerrar la sesión.

# Explicación "Administrador"
Al iniciar sesión con administrador aparece una ventana donde se puede hacer un CRUD, además de una lista con los productos que hay en la base de datos. El administrador tiene que poner todos los campos, incluyendo el ID del producto y luego puede agregarlo. Por último puede hacer cualquier paso del CRUD.

# Evidencia con imágenes
Cuando se inicia el proyecto sale esta ventana. Aquí es donde se selecciona si es Cliente o Administrador: 

# Interfaz Inicio de Sesion:
  <p style="text-align: center;"></p>
  <img src="Capturas/Home.png" alt="" style="display: block; margin: 10px auto;">

# Interfaz Cliente
  <p style="text-align: center;">Esta es la pantalla que aparece al iniciar sesion con cliente</p>
  <img src="Capturas/Cliente.png" alt="" style="display: block; margin: 10px auto;">
  
- Se tiene que seleccionar un producto y luego tiene que ingresar la cantidad de ese producto
  <p style="text-align: center;"></p>
  <img src="Capturas/AgregarCarro.png" alt="" style="display: block; margin: 10px auto;">
  
- Luego se tiene que confirmar la compra y muestra el detalle de esta
  <p style="text-align: center;"></p>
  <img src="Capturas/Detalles.png" alt="" style="display: block; margin: 10px auto;">

# Interfaz Administrador:
  <p style="text-align: center;">Esta es la pantalla que aparece al iniciar sesion con administrador</p>
  <img src="Capturas/Administrador.png" alt="" style="display: block; margin: 10px auto;">
  
- Crear
  <p style="text-align: center;">Agregué las Papas fritas</p>
  <img src="Capturas/Crear.png" alt="" style="display: block; margin: 10px auto;">
  
- Actualizar
<p style="text-align: center;">Actualicé el precio de la Mantequilla, por ende se asume que también se puede seleccionar un elemento. Vendría siendo el leer</p>
  <img src="Capturas/Actualizar.png" alt="" style="display: block; margin: 10px auto;">
  
- Eliminar
<p style="text-align: center;">Eliminé la Sal</p>
  <img src="Capturas/Eliminar.png" alt="" style="display: block; margin: 10px auto;">

# Enviar datos a MQTT:
- En la aplicacion IoT MQTT Panel muestra los datos que envié. No le saco captura a los códigos porque ya están en el GitHub
<p style="text-align: center;"></p>
  <img src="Capturas/mqtt.jpg" alt="" style="display: block; margin: 10px auto;">

# Tablas en Firebase:

  <p style="text-align: center;"></p>
  <img src="Capturas/.png" alt="" style="display: block; margin: 10px auto;">

