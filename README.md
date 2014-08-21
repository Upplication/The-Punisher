The-Punisher
============
[![Build Status](https://travis-ci.org/Upplication/The-Punisher.svg?branch=jarnaiz)](https://travis-ci.org/Upplication/The-Punisher) [![Coverage Status](https://coveralls.io/repos/Upplication/The-Punisher/badge.png?branch=jarnaiz)](https://coveralls.io/r/Upplication/The-Punisher?branch=jarnaiz)

- [x] Como Administrador quiero añadir un castigo mediante un título y una descripción.
 - [x] Quiero crear un castigo con titulo hola y descripción adiós y que devuelva un objeto castigo con titulo hola y descripción adiós.
 - [x] Quiero crear un castigo con titulo pepe y descripción mundo y que devuelva un objeto castigo con titulo pepe y descripción mundo.:
 - [x] Como Creador quiero que una vez añadido un titulo y una descripción se muestre un mensaje de todo ha ido bien.
 - [x] Como Creador quiero persistir en una BD el castigo con titulo, descripción y un id único.
 - [x] Como Creador quiero guardar el castigo con titulo jamón y el sistema me devuelve el castigo con titulo jamón y un id único
 - [ ] Como Creador quiero ver una pantalla donde introducir el título y la descripción y un botón enviar para guardar el objeto
  - [x] Como Creador visualizo un campo título.
  - [x] Como Creador visualizo un campo descripción
  - [x] Como Creador tengo un botón enviar
  - [ ] Como Creador si no relleno el campo título y pulso en envíar se muestra un mensaje indicando que el título es obligatorio
  - [ ] Como Creador si no relleno el campo descripción y pulso en envíar se muestra un mensaje indicando que la descripción es obligatorio. ¿javascript testing?
  - [ ] Como Creador si relleno un título y una descripción y pulso en envíar y no existe ya un título igual el sistema me indica que se ha añadido correctamente y se me muestra el castigo en la lista de castigos. ¿javascript testing?
- [ ] Como Administrador quiero ver un listado de castigos con título y descripción ordenados por orden alfabético del título.
 - [ ] Como Creador entro en la pantalla de administración de castigos y visualizo los castigos guardados anteriormente. ¿javascript testing?
 - [x] Devuelvo los resultados como JSON con el campo titulo, descripción e id.
 - [x] Devuelvo los resultados como ordenados por titulo alfabeticamente
 - [x] Devuelvo los resultados anteriormente persistidos.
- [ ] Como Administrador quiero eliminar un castigo del listado para que no se muestre en la ruleta
- [ ] Como Administrador quiero modificar un castigo del listado.
- [ ] Como Administrador no puedo añadir un castigo con un título repetido.
- [ ] Como Administrador no puedo añadir un castigo con un título descripción vacíos.
- [ ] Como Administrador no puedo añadir un castigo con un título o descripción de más de 100 caracteres.

## Notas

Algunos tests son técnicos y esos esta bien apuntarlos dentro de tu libreta de tests, pero crearía dos libretas: la libreta donde esta todo para que no se te olvide testear nada y una segunda libreta que es donde se apuntan los tests de aceptación que se puede reutilizar como documentación. Esta segunda libreta se podría automaticar con concordion u otros.

Al haber decidido que el post de guardar punishment y el listado de punishment es JSON hace que no pueda testear facilmente los tests de aceptación como: 'Como Creador si relleno un título y una descripción y pulso en envíar y no existe ya un título igual el sistema me indica que se ha añadido correctamente y se me muestra el castigo en la lista de castigos.' ya que debería procesar javascript o checkear el marcado de javascript. No pasa "nada" porque puedo testear todas las piezas (la llamada ajax y el marcado esencial del html que lo invoca) pero si hubiera sido todo request/response podría testear todo más facil con el stack de spring.
