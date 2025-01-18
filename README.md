# BuyRecipes

This is an extension to an existing e-commerce site that allows users to buy products that are listed in an online recipe.

## Feature

Exposes REST Endpoints:

- GET /recipes
- GET /carts/:id
- POST /carts/:id/add_recipe
- DELETE /carts/:id/recipes/:id

## Getting Started

To build and run the application, follow these steps:

1. **Clone the repository:**

   ```shell
   git clone git@github.com:Teagan42/BuyRecipes
   cd BuyRecipes
   ```

2. **Build the project:**

   ```shell
   ./gradlew build
   ```

3. **Run the application:**
   ```shell
   ./gradlew run
   ```

## Running Tests

To run the unit tests, use the following command:

```shell
./gradlew test
```

## Dependencies

This project uses Gradle for dependency management. You can add dependencies in the `build.gradle.kts` file.

## License

This project is licensed under the MIT License. See the LICENSE file for more details.
