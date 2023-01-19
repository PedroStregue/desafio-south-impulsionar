# DESAFIO SOUTH IMPULSIONAR

O projeto 3 do Projeto Impulsionar consiste de duas aplicações: 
 - A aplicação principal,PRODUTO-API, uma rest API que também consome mensagens publicadas pela segunda aplicação.
 - A aplicação producer,PRODUCER, que produz mensagens e as publica para o consumidor da Produto-api

# ENDPOINTS

Produto-Api:

GET http://localhost:8080/products : Retorna todos os registros da lista.

GET http://localhost:8080/products/{id} : Retorna um registro especifíco da lista filtrado pelo ID.

POST http://localhost:8080/products : Adiciona um produto na lista.

POST http://localhost:8080/products/import : Consome um arquivo CSV e adiciona seus produtos na lista.

PUT http://localhost:8080/products/{id} : Edita um produto da lista filtrado pelo ID.

PUT http://localhost:8080/products/change-stock/1 : Atualiza a quantidade em estoque de um produto filtrado pelo ID.

DELETE http://localhost:8080/products/{id} : Exclui um produto da lista filtrado pelo ID.

Producer:

GET http://localhost:8081/products : Retorna todos os registros da lista, através de uma requisição utilizando o restTemplate.

GET http://localhost:8081/products/{id} : Retorna um registro especifíco da lista filtrado pelo ID, através de uma requisição utilizando o restTemplate

POST http://localhost:8081/products : Adiciona um produto na lista, publicando uma mensagem na fila, que é consumida na aplicação Produto-Api

POST http://localhost:8081/products/import : Consome um arquivo CSV e adiciona seus produtos na lista, publicando N mensagens na fila, são consumidas na aplicação Produto-Api

PUT http://localhost:8081/products/{id} : Edita um produto da lista filtrado pelo ID, e envia o novo corpo do produto em uma mensagem para a Produto-api

PUT http://localhost:8081/products/change-stock/1 : Atualiza a quantidade em estoque de um produto filtrado pelo ID, publicando uma mensagem na fila e sendo consumida pela produto-api

DELETE http://localhost:8081/products/{id} : Exclui um produto da lista filtrado pelo ID, envia o ID para a aplicação Produto-api através de mensagens.
