# Regras de Negócio: AlugaSimples

---

## Visão Geral

Este documento descreve as regras de negócio para um sistema de aluguel de carros simplificado, composto por dois microserviços principais: **vehicles** e **book**. O objetivo é gerenciar a frota de veículos e suas respectivas reservas, garantindo a coordenação entre os dois domínios.

---

### 1. Microserviço de Veículos (vehicles)

Este microserviço é a fonte de verdade para todas as informações relacionadas aos veículos da frota e seu status de disponibilidade.

#### 1.1. Cadastro de Veículos

* Um **veículo** é identificado por um `ID` único.
* Cada veículo possui atributos como `id`, `brand`, `year`, `engine`, `status`, `model`.
* Ao ser cadastrado, o **status inicial** de um veículo deve ser `AVAILABLE`.

Exemplo de um veículo pós criação:

```json
 {
   "brand": "Fiat",
   "id": 1,
   "model": "Mobi",
   "status": "AVAILABLE",
   "year": 2022,
   "engine": "1.0"
 }
```

#### 1.2. Status do Veículo

* O status de um veículo pode ser:
  * `AVAILABLE`: O veículo está pronto para ser alugado.
    * `RENTED`: O veículo está atualmente em uma reserva.
    * `UNDER_MAINTENANCE`: O veículo está fora de serviço para reparos ou manutenção.
* **Regra de Transição de Status:**
  * Um veículo só pode ser alterado para `RENTED` se seu status atual for `AVAILABLE`.
    * Um veículo só pode ser alterado para `AVAILABLE` se seu status atual for `RENTED` ou `UNDER_MAINTENANCE`.
    * Um veículo pode ser alterado para `UNDER_MAINTENANCE` a partir de qualquer status.

#### 1.3. Consulta de Veículos

* Deve ser possível **listar todos os veículos** da frota, com seus detalhes e status.
* Deve ser possível **consultar um veículo específico** pelo seu `id`, retornando todos os seus detalhes e status.
* Deve ser possível na consulta do veículo termos o seguinte campo `carTitle` que é composto por `brand`, `model` e `engine`, por exemplo: `Fiat Mobi 1.0`.

#### 1.4. Atualização de Veículos

* Deve ser possível **atualizar atributos** de um veículo.
* Deve ser possível **alterar o status** de um veículo explicitamente, seguindo as regras de transição.

#### 1.5. Exclusão de Veículos

* Um veículo pode ser **removido da frota**.
* **Restrição:** Um veículo **não pode ser removido** se o seu status atual for `ALUGADO`. Ele deve ser `DISPONIVEL` ou `EM_MANUTENCAO` para ser removido.

***

# VehiclesApi

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/BookingsApi-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
