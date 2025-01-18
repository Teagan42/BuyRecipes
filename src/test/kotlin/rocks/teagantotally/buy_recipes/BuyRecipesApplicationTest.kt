package rocks.teagantotally.buy_recipes

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import rocks.teagantotally.buy_recipes.data.entities.Cart
import rocks.teagantotally.buy_recipes.data.entities.Recipe
import rocks.teagantotally.buy_recipes.data.repositories.CartRepository
import rocks.teagantotally.buy_recipes.data.repositories.ProductRepository
import rocks.teagantotally.buy_recipes.data.repositories.RecipeRepository

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BuyRecipesApplicationTest {

  @LocalServerPort var port: Int? = null
  @Autowired lateinit var restTemplate: TestRestTemplate
  @Autowired lateinit var recipeRepository: RecipeRepository
  @Autowired lateinit var productRepository: ProductRepository
  @Autowired lateinit var cartRepository: CartRepository

  @Test
  fun `application loads context successfully`() {
    // This test will fail if the application context cannot start
  }

  @Test
  fun `should return all recipes`() {
    val entity = restTemplate.getForEntity("/recipes", List::class.java)
    assertEquals(HttpStatus.OK, entity.statusCode)
    assertEquals(1, entity.body.size)
    entity.body.forEach { recipe ->
      val recipeMap = recipe as Map<*, *>
      assertEquals(1, recipeMap["id"])
      assertEquals("Banana Pudding", recipeMap["name"])
      val items = recipeMap["items"] as List<*>
      assertEquals(3, items.size)
      items.forEach { product ->
        val productMap = product as Map<*, *>
        when (productMap["id"]) {
          1 -> {
            assertEquals("Banana", productMap["name"])
            assertEquals(67, productMap["priceInCents"])
          }
          2 -> {
            assertEquals("Nilla Wafers", productMap["name"])
            assertEquals(499, productMap["priceInCents"])
          }
          3 -> {
            assertEquals("Vanilla Pudding", productMap["name"])
            assertEquals(99, productMap["priceInCents"])
          }
          else -> fail("Unexpected product id: ${productMap["id"]}")
        }
      }
    }
  }

  @Test
  fun `should return the requested recipe`() {
    val entity = restTemplate.getForEntity("/recipes/1", Recipe::class.java)
    assertEquals(HttpStatus.OK, entity.statusCode)
    val recipe = entity.body as Recipe
    assertEquals(1, recipe.id)
    assertEquals("Banana Pudding", recipe.name)
    val items = recipe.items
    assertEquals(3, items.size)
    items.forEach { product ->
      when (product.id) {
        1 -> {
          assertEquals("Banana", product.name)
          assertEquals(67, product.priceInCents)
        }
        2 -> {
          assertEquals("Nilla Wafers", product.name)
          assertEquals(499, product.priceInCents)
        }
        3 -> {
          assertEquals("Vanilla Pudding", product.name)
          assertEquals(99, product.priceInCents)
        }
        else -> fail("Unexpected product id: ${product.id}")
      }
    }
  }

  @Test
  fun `should return the requested cart`() {
    val entity = restTemplate.getForEntity("/carts/1", Cart::class.java)
    assertEquals(HttpStatus.OK, entity.statusCode)
    val cart = entity.body as Cart
    assertEquals(1, cart.id)
    assertEquals(0, cart.items.size)
  }

  @Test
  fun `should add the requested recipe to a cart`() {
    val request = recipeRepository.findAll().get(0)
    val entity = restTemplate.postForEntity("/carts/1/add_recipe", request, Cart::class.java)
    assertEquals(HttpStatus.OK, entity.statusCode)
    val cart = entity.body as Cart
    assertEquals(1, cart.id)
    assertEquals(request.items.size, cart.items.size)
    cart.items.forEach { product ->
      when (product.id) {
        1 -> {
          assertEquals("Banana", product.name)
          assertEquals(67, product.priceInCents)
        }
        2 -> {
          assertEquals("Nilla Wafers", product.name)
          assertEquals(499, product.priceInCents)
        }
        3 -> {
          assertEquals("Vanilla Pudding", product.name)
          assertEquals(99, product.priceInCents)
        }
        else -> fail("Unexpected product id: ${product.id}")
      }
    }
    assertEquals(cart.items.sumOf { it.priceInCents }, request.items.sumOf { it.priceInCents })
  }

  @Test
  fun `should remove the requested recipe from a cart`() {
    `should add the requested recipe to a cart`()
    val request = recipeRepository.findAll().get(0)
    restTemplate.delete("/carts/1/recipes/1")
    val entity = restTemplate.getForEntity("/carts/1", Cart::class.java)
    assertEquals(HttpStatus.OK, entity.statusCode)
    val cart = entity.body as Cart
    assertEquals(1, cart.id)
    assertEquals(0, cart.items.size)
  }

  @Test
  @Sql(
          statements =
                  [
                          "INSERT INTO products (name, price_in_cents) VALUES ('Apple', 25),('Cinnamon', 10),('Sugar', 5)",
                          "INSERT INTO recipes (name) VALUES ('Apple Pie')",
                          "INSERT INTO recipe_items (recipe_id, product_id) VALUES (2, 4),(2, 5),(2, 6)",
                          "INSERT INTO carts (id) VALUES (1)",
                          "INSERT INTO cart_items (cart_id, product_id) VALUES (1, 4),(1, 5),(1, 6)",
                  ],
          executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
  )
  @Sql(
          statements =
                  [
                          "DELETE FROM recipe_items WHERE recipe_id = 2",
                          "DELETE FROM recipes WHERE id = 2",
                          "DELETE FROM products WHERE id IN (4, 5, 6)",
                          "DELETE FROM cart_items WHERE cart_id = 1",
                  ],
          executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
  )
  fun `should remove the requested recipe from a cart with other recipes`() {
    `should add the requested recipe to a cart`()
    var entity = restTemplate.getForEntity("/carts/1", Cart::class.java)
    assertEquals(HttpStatus.OK, entity.statusCode)
    var cart = entity.body as Cart
    assertEquals(3, cart.items.size)

    val request = recipeRepository.findAll().get(0)
    restTemplate.delete("/carts/1/recipes/${request.id}")

    entity = restTemplate.getForEntity("/carts/1", Cart::class.java)

    assertEquals(HttpStatus.OK, entity.statusCode)

    cart = entity.body as Cart

    assertEquals(1, cart.id)

    assertEquals(3, cart.items.size)
  }
}
